package com.mulesoft.connectors.googlepubsub.internal.config;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import com.mulesoft.connectors.googlepubsub.internal.connection.provider.MulegooglepubsubProducerConnectionProvider;
import com.mulesoft.connectors.googlepubsub.internal.operation.MulegooglepubsubOperations;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(MulegooglepubsubOperations.class)
@ConnectionProviders(MulegooglepubsubProducerConnectionProvider.class)
@DisplayName("Google PubSub Producer Configuration")
public class MulegooglepubsubProducerConfiguration {

 /* @Parameter
  private String credentialsFilePath;

public String getCredentialsFilePath() {
	return credentialsFilePath;
}

public void setCredentialsFilePath(String credentialsFilePath) {
	this.credentialsFilePath = credentialsFilePath;
}
*/

}
