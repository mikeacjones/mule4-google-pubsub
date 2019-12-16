package com.mulesoft.connectors.googlepubsub.internal.connection.provider;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.auth.oauth2.GoogleCredentials;
import com.mulesoft.connectors.googlepubsub.internal.connection.MulegooglepubsubConsumerConnection;
import com.mulesoft.connectors.googlepubsub.internal.service.MulegooglepubsubConsumer;

@Alias("googlepubsub-consumer-connection")
@DisplayName("Google PubSub Consumer Connection")
public class MulegooglepubsubConsumerConnectionProvider implements PoolingConnectionProvider<MulegooglepubsubConsumerConnection> {

  private final Logger LOGGER = LoggerFactory.getLogger(MulegooglepubsubConsumerConnectionProvider.class);

  @Parameter
  private String credentialFilePath;
  @Parameter
  private String topic;
 @Parameter
 private String projectId;
 @Parameter
 private String subscriptionId;
 @Parameter
 @Optional(defaultValue="1")
 private int threadCount;
 @Parameter
 @Optional(defaultValue="10000")
 private long maxOutstandingElementCount;
 @Parameter
 @Optional(defaultValue="1000000000")
 private long maxOutstandingRequestBytes;
 

  @Override
  public MulegooglepubsubConsumerConnection connect() throws ConnectionException {
	  
	  GoogleCredentials credentials = null;
	//  Subscriber subscriber = null;
		try {
			credentials = GoogleCredentials.fromStream(new FileInputStream(getCredentialFilePath()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage());
			throw new ConnectionException(e.getCause());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage());
			throw new ConnectionException(e.getCause());
		}
		 //ProjectTopicName topicName = ProjectTopicName.of(projectId, topic);
		/* subscriber = Subscriber.newBuilder(subscriptionId,new MulegooglepubsubMessageReceiver())
				 .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
				 .build();*/
	  
    return new MulegooglepubsubConsumerConnection(new MulegooglepubsubConsumer(getTopic(),getProjectId(),getSubscriptionId(),credentials,getThreadCount(),getMaxOutstandingElementCount(),getMaxOutstandingRequestBytes()));
  }

  public int getThreadCount() {
	return threadCount;
}

public void setThreadCount(int threadCount) {
	this.threadCount = threadCount;
}

public long getMaxOutstandingElementCount() {
	return maxOutstandingElementCount;
}

public void setMaxOutstandingElementCount(long maxOutstandingElementCount) {
	this.maxOutstandingElementCount = maxOutstandingElementCount;
}

public long getMaxOutstandingRequestBytes() {
	return maxOutstandingRequestBytes;
}

public void setMaxOutstandingRequestBytes(long maxOutstandingRequestBytes) {
	this.maxOutstandingRequestBytes = maxOutstandingRequestBytes;
}

public String getCredentialFilePath() {
	return credentialFilePath;
}

public void setCredentialFilePath(String credentialFilePath) {
	this.credentialFilePath = credentialFilePath;
}

public String getTopic() {
	return topic;
}

public void setTopic(String topic) {
	this.topic = topic;
}

public String getProjectId() {
	return projectId;
}

public void setProjectId(String projectId) {
	this.projectId = projectId;
}

public String getSubscriptionId() {
	return subscriptionId;
}

public void setSubscriptionId(String subscriptionId) {
	this.subscriptionId = subscriptionId;
}

@Override
  public void disconnect(MulegooglepubsubConsumerConnection connection) {
    try {
      connection.getConsumer().shutdown();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(MulegooglepubsubConsumerConnection connection) {
    return ConnectionValidationResult.success();
  }
}
