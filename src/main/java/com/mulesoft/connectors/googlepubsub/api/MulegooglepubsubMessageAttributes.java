package com.mulesoft.connectors.googlepubsub.api;

import java.io.Serializable;
import java.util.Map;

public class MulegooglepubsubMessageAttributes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7922724350694572286L;
	
	public MulegooglepubsubMessageAttributes(String messageId, Map<String, String> attributes, String publishTime) {
		super();
		this.messageId = messageId;
		this.attributes = attributes;
		this.publishTime = publishTime;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Map<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	private String messageId;
	private Map<String,String> attributes;
	private String publishTime;
	

}
