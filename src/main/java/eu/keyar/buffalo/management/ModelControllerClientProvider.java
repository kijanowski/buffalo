package eu.keyar.buffalo.management;

import org.jboss.as.controller.client.ModelControllerClient;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("unused")
public class ModelControllerClientProvider {

    @Produces
    public ModelControllerClient createClient() {

        InetAddress host;
        String hostName = System.getProperty("jboss.bind.address.management", "localhost");

        try {
            host = InetAddress.getByName(hostName);
        } catch (UnknownHostException uhe) {
            throw new RuntimeException("Could not get address for " + hostName, uhe);
        }

        final CallbackHandler callbackHandler = new CallbackHandler() {

            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                for (Callback current : callbacks) {
                    if (current instanceof RealmCallback) {
                        RealmCallback rcb = (RealmCallback) current;
                        rcb.setText(rcb.getDefaultText());
                    } else {
                        throw new UnsupportedCallbackException(current);
                    }
                }
            }
        };

        return ModelControllerClient.Factory.create(host, 9999, callbackHandler);
    }

    public void close(@Disposes ModelControllerClient client) {
        if (client != null)
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException("Something went terribly wrong.", e);
            }

    }
}
