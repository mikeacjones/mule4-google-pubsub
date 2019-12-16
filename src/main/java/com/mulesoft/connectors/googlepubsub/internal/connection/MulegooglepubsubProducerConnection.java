package com.mulesoft.connectors.googlepubsub.internal.connection;

import java.util.concurrent.TimeUnit;

import com.google.cloud.pubsub.v1.Publisher;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class MulegooglepubsubProducerConnection {

  private  Publisher publisher;
  private int threadCount;



public MulegooglepubsubProducerConnection(Publisher publisher, int threadCount) {
    setPublisher(publisher);
    setThreadCount(threadCount);
  }

  public void invalidate() {
    // do something to invalidate this connection!
	  getPublisher().shutdown();
	  try {
		getPublisher().awaitTermination(1, TimeUnit.MINUTES);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

public Publisher getPublisher() {
	return this.publisher;
}
public void setPublisher(Publisher publisher) {
	this.publisher = publisher;
}

public int getThreadCount() {
	return threadCount;
}

public void setThreadCount(int threadCount) {
	this.threadCount = threadCount;
}
}
