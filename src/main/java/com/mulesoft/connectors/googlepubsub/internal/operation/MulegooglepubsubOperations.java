package com.mulesoft.connectors.googlepubsub.internal.operation;


import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Executors;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.process.CompletionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.mulesoft.connectors.googlepubsub.internal.connection.MulegooglepubsubProducerConnection;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class MulegooglepubsubOperations {
	private final Logger LOGGER = LoggerFactory.getLogger(MulegooglepubsubOperations.class);


	@MediaType("application/json")
	//public String publishMessage(@Connection MulegooglepubsubProducerConnection googlePubSubConnection, @Alias("Message") @Content(primary=true) InputStream message)
		public void publishMessage(@Connection MulegooglepubsubProducerConnection googlePubSubConnection, @Alias("Message") @Content(primary=true) InputStream message, @Content @Optional @DisplayName("Message Attributes") Map<String,String> attribs, CompletionCallback<String,String> callback){	
		//String messageId = null;
		try {
		
			PubsubMessage pubsubMsg = PubsubMessage.newBuilder().setData(ByteString.readFrom(message)).putAllAttributes(attribs).build();
			 ApiFuture<String> messageIdFuture = googlePubSubConnection.getPublisher().publish(pubsubMsg);
			//  messageId = messageIdFuture.get();
			 ApiFutures.addCallback(
				        messageIdFuture,
				        new ApiFutureCallback<String>() {
				          public void onSuccess(String messageId) {
				            //System.out.println("published with message id: " + messageId);
				        	  	LOGGER.debug("published with message id: " + messageId);
				            callback.success(Result.<String,String>builder().output(messageId).attributes("").build());
				          }

				          public void onFailure(Throwable t) {
				        	  LOGGER.error("failed to publish: " + t);
				        //    System.out.println("failed to publish: " + t);
				            callback.error(t);
				          }
				        },
				        //Executors.newSingleThreadExecutor());
				        Executors.newCachedThreadPool());
			 
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//return "{\"messageId\":" + messageId + "}";
		
	}
  
}
