package com.mulesoft.connectors.googlepubsub.internal.service;

import java.io.IOException;
import java.io.InputStream;

import org.mule.runtime.extension.api.runtime.source.SourceCallback;

import com.google.api.gax.batching.FlowControlSettings;
import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.mulesoft.connectors.googlepubsub.api.MulegooglepubsubMessageAttributes;

public class MulegooglepubsubConsumer {
	private String topic;
	private String projectId;
	private String subscriptionId;
	private GoogleCredentials credentials;
	private Subscriber subscriber;
	private int threadCount=1;
	private long maxOutstandingElementCount=10_000L;
	private long maxOutstandingRequestBytes=1_000_000_000L;
	
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

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public void run(final SourceCallback<InputStream,MulegooglepubsubMessageAttributes> callback ) {
		FixedCredentialsProvider credProv = FixedCredentialsProvider.create(getCredentials());
		SubscriptionAdminClient subscriptionAdminClient = null;
		try {
			 subscriptionAdminClient = SubscriptionAdminClient.create(SubscriptionAdminSettings.newBuilder().setCredentialsProvider(credProv).build());		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProjectTopicName topicName = ProjectTopicName.of(getProjectId(),getTopic()) ;
		ProjectSubscriptionName subscriptionName =  ProjectSubscriptionName.of(getProjectId(), getSubscriptionId());
		Subscription subscription = subscriptionAdminClient.getSubscription(subscriptionName);
		if(subscription == null)
			subscription = subscriptionAdminClient.createSubscription( subscriptionName, topicName, PushConfig.getDefaultInstance(), 0);		
		
		
		
		ExecutorProvider executorProvider = InstantiatingExecutorProvider.newBuilder().setExecutorThreadCount(getThreadCount()).build();
		FlowControlSettings flowControlSettings =
		        FlowControlSettings.newBuilder()
		            .setMaxOutstandingElementCount(getMaxOutstandingElementCount())
		            .setMaxOutstandingRequestBytes(getMaxOutstandingRequestBytes())
		            .build();
		setSubscriber(Subscriber.newBuilder(subscriptionName,new MulegooglepubsubMessageReceiver(callback))
				 .setCredentialsProvider(credProv)
				 .setExecutorProvider(executorProvider)
				 .setFlowControlSettings(flowControlSettings)
				 .build());
		getSubscriber().startAsync().awaitRunning();
	}
	
	public void shutdown() {
		if(getSubscriber() != null)
			getSubscriber().stopAsync().awaitRunning();
	}

	public MulegooglepubsubConsumer(String topic, String projectId,String subscriptionId, GoogleCredentials credentials, int threadCount, long maxOutstandingElementCount,long maxOutstandingRequestBytes ) {
		super();
		this.topic = topic;
		this.projectId=projectId;
		this.subscriptionId = subscriptionId;
		this.credentials = credentials;
		this.threadCount= threadCount;
		this.maxOutstandingElementCount = maxOutstandingElementCount;
		this.maxOutstandingRequestBytes = maxOutstandingRequestBytes;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public GoogleCredentials getCredentials() {
		return credentials;
	}

	public void setCredentials(GoogleCredentials credentials) {
		this.credentials = credentials;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
