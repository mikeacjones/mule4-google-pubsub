package com.mulesoft.connectors.googlepubsub.internal;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

import com.mulesoft.connectors.googlepubsub.internal.config.MulegooglepubsubConsumerConfiguration;
import com.mulesoft.connectors.googlepubsub.internal.config.MulegooglepubsubProducerConfiguration;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "mule-googlepubsub")
@Extension(name = "Google PubSub")
@Configurations({MulegooglepubsubConsumerConfiguration.class,MulegooglepubsubProducerConfiguration.class})
public class MulegooglepubsubExtension {

}
