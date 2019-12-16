package com.mulesoft.connectors.googlepubsub.internal.config;

import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import com.mulesoft.connectors.googlepubsub.internal.connection.provider.MulegooglepubsubConsumerConnectionProvider;
import com.mulesoft.connectors.googlepubsub.internal.sources.MulegooglepubsubSource;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */

@ConnectionProviders(MulegooglepubsubConsumerConnectionProvider.class)
@Configuration(name="google-pubsub-consumer")
@DisplayName("Google PubSub Consumer Configuration")
@Sources(MulegooglepubsubSource.class)
public class MulegooglepubsubConsumerConfiguration {


}
