package eu.keyar.buffalo.management;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

import javax.inject.Inject;
import java.io.IOException;

public class OperationExecutor {

    @Inject
    ModelControllerClient client;

    public ModelNode execute(ModelNode op) {
        try {
            ModelNode result = client.execute(op);
            // Check to make sure there is an outcome
            if (result.hasDefined(ClientConstants.OUTCOME)) {
                if (result.get(ClientConstants.OUTCOME).asString().equals(ClientConstants.SUCCESS)) {
                    return result;
                } else if (result.get(ClientConstants.OUTCOME).asString().equals("failed")) {
                    throw new IllegalStateException(String.format("A failure occurred when executing the operation %s. Error: %s", op,
                            (result.hasDefined(ClientConstants.FAILURE_DESCRIPTION) ? result.get(ClientConstants.FAILURE_DESCRIPTION).asString() : "Unknown")));
                } else {
                    throw new IllegalStateException(String.format("An unsupported OUTCOME was returned: %s", result.get(ClientConstants.OUTCOME).asString()));
                }
            } else {
                throw new IllegalStateException(String.format("An unexpected response was found executing the operation %s. Result: %s", op, result));
            }

        } catch (IOException e) {
            throw new IllegalStateException(String.format("Could not execute operation '%s'", op), e);
        }
    }
}
