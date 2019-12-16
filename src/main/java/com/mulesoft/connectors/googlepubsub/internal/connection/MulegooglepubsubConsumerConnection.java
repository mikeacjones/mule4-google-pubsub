package com.mulesoft.connectors.googlepubsub.internal.connection;


import com.mulesoft.connectors.googlepubsub.internal.service.MulegooglepubsubConsumer;

public final class MulegooglepubsubConsumerConnection {
	
private MulegooglepubsubConsumer consumer;
	

	public MulegooglepubsubConsumerConnection(MulegooglepubsubConsumer consumer) {
		super();
		this.consumer = consumer;
	}

	public MulegooglepubsubConsumer getConsumer() {
		return consumer;
	}

	public void setconsumer(MulegooglepubsubConsumer consumer) {
		this.consumer = consumer;
	}



}
