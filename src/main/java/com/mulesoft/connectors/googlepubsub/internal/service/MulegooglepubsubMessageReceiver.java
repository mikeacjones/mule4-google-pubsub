package com.mulesoft.connectors.googlepubsub.internal.service;

import java.io.InputStream;

import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import com.mulesoft.connectors.googlepubsub.api.MulegooglepubsubMessageAttributes;

public class MulegooglepubsubMessageReceiver implements MessageReceiver {

	private SourceCallback<InputStream,MulegooglepubsubMessageAttributes> callback;
	
	
	public MulegooglepubsubMessageReceiver(SourceCallback<InputStream, MulegooglepubsubMessageAttributes> callback) {
		super();
		this.callback = callback;
	}


	@Override
	public void receiveMessage(PubsubMessage msg, AckReplyConsumer consumer) {
		MulegooglepubsubMessageAttributes msgAttr = new MulegooglepubsubMessageAttributes(msg.getMessageId(),msg.getAttributesMap(),Long.toString(msg.getPublishTime().getSeconds()));
		Result<InputStream,MulegooglepubsubMessageAttributes> result =  Result.<InputStream,MulegooglepubsubMessageAttributes>builder().output(msg.getData().newInput()).attributes(msgAttr).build();
		getCallback().handle(result);
		consumer.ack();
		
	}


	public SourceCallback<InputStream,MulegooglepubsubMessageAttributes> getCallback() {
		return callback;
	}


	public void setCallback(SourceCallback<InputStream,MulegooglepubsubMessageAttributes> callback) {
		this.callback = callback;
	}
	
	

}
