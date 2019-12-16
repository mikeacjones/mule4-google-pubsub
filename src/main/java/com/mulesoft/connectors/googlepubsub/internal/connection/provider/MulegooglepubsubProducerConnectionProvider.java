package com.mulesoft.connectors.googlepubsub.internal.connection.provider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Duration;

import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectTopicName;
import com.mulesoft.connectors.googlepubsub.internal.connection.MulegooglepubsubProducerConnection;



@Alias("googlepubsub-producer-connection")
@DisplayName("Google PubSub Producer Connection")
public class MulegooglepubsubProducerConnectionProvider implements PoolingConnectionProvider<MulegooglepubsubProducerConnection> {

  private final Logger LOGGER = LoggerFactory.getLogger(MulegooglepubsubProducerConnectionProvider.class);

  @Parameter
  private String credentialFilePath;
  @Parameter
  private String topicName;
 // @Parameter
  //@Optional(defaultValue="false")
  //private boolean enableMessageOrdering = false;
 @Parameter
 @Optional(defaultValue="5000")
 private Long requestBytesThreshold = 5000L; // default : 1 byte
 @Parameter
 @Optional(defaultValue="10")
 private Long messageCountBatchSize= 10L; // default : 1 message
 @Parameter
 private String projectId;
 @Parameter
 @Optional(defaultValue="1")
 private int threadCount=1;
 @Parameter
 @Optional(defaultValue="100")
 private long retryDelay = 100;
 @Parameter
 @Optional(defaultValue="2.0")
 private double retryDelayMultiplier = 2.0; 
 @Parameter
 @Optional(defaultValue="60")
 private long maxRetryDelay=60;
 @Parameter
 @Optional(defaultValue="1")
 private long initialRpcTimeout=1;
 @Parameter
 @Optional(defaultValue="1.0")
 private double rpcTimeoutMultiplier=1.0;
 @Parameter
 @Optional(defaultValue="600")
 private long maxRpcTimeout=600;
 @Parameter
 @Optional(defaultValue="600")
 private long totalTimeout=600;
 
 
 

  @Override
  public MulegooglepubsubProducerConnection connect() throws ConnectionException {
	  
	  GoogleCredentials credentials;
	  Publisher publisher = null;
	try {
		
		 RetrySettings retrySettings =
			        RetrySettings.newBuilder()
			            .setInitialRetryDelay(Duration.ofMillis(retryDelay))
			            .setRetryDelayMultiplier(retryDelayMultiplier)
			            .setMaxRetryDelay(Duration.ofSeconds(maxRetryDelay))
			            .setInitialRpcTimeout(Duration.ofSeconds(initialRpcTimeout))
			            .setRpcTimeoutMultiplier(rpcTimeoutMultiplier)
			            .setMaxRpcTimeout(Duration.ofSeconds(maxRpcTimeout))
			            .setTotalTimeout(Duration.ofSeconds(totalTimeout))
			            .build();
		BatchingSettings batchingSettings =  BatchingSettings.newBuilder().
									setRequestByteThreshold(requestBytesThreshold).
									setElementCountThreshold(messageCountBatchSize).
									build();
		credentials = GoogleCredentials.fromStream(new FileInputStream(credentialFilePath));
		 ProjectTopicName topic = ProjectTopicName.of(projectId, topicName);
		 ExecutorProvider executorProvider = InstantiatingExecutorProvider.newBuilder().
				 	setExecutorThreadCount(threadCount)
				 	.build();
		publisher = Publisher.newBuilder(topic)
					.setCredentialsProvider(FixedCredentialsProvider.create(credentials))
					//.setEnableMessageOrdering(enableMessageOrdering)
					.setBatchingSettings(batchingSettings)
					.setExecutorProvider(executorProvider)
					.setRetrySettings(retrySettings)
					.build();
	} catch (FileNotFoundException e) {
		LOGGER.error(e.getMessage());
		throw new ConnectionException(e.getCause());
	} catch (IOException e) {
		LOGGER.error(e.getMessage());
		throw new ConnectionException(e.getCause());
	}
	  
    return new MulegooglepubsubProducerConnection(publisher,threadCount);
  }

  @Override
  public void disconnect(MulegooglepubsubProducerConnection connection) {
    try {
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(MulegooglepubsubProducerConnection ProducerConnection) {
    return ConnectionValidationResult.success();
  }
}
