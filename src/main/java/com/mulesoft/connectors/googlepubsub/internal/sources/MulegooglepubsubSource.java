package com.mulesoft.connectors.googlepubsub.internal.sources;

import java.io.InputStream;

import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;

import com.mulesoft.connectors.googlepubsub.api.MulegooglepubsubMessageAttributes;
import com.mulesoft.connectors.googlepubsub.internal.connection.MulegooglepubsubConsumerConnection;

@DisplayName("Google PubSub Consumer")
@Alias("consumer")
@MediaType(MediaType.ANY)
public class MulegooglepubsubSource extends Source<InputStream, MulegooglepubsubMessageAttributes> {
	@Connection
	private ConnectionProvider<MulegooglepubsubConsumerConnection> mulegooglepubsubConnectionProvider;
	
	private MulegooglepubsubConsumerConnection mulegooglepubsubConnection;
	@Override
	public void onStart(SourceCallback<InputStream,MulegooglepubsubMessageAttributes> sourceCallBack) throws MuleException {

		mulegooglepubsubConnection = mulegooglepubsubConnectionProvider.connect();
		//mulegooglepubsubConnection.getSubscriber().startAsync();
		mulegooglepubsubConnection.getConsumer().run(sourceCallBack);
		
	}

	@Override
	public void onStop() {
		mulegooglepubsubConnectionProvider.disconnect(mulegooglepubsubConnection);
		
	}

}
