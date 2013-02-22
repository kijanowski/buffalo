package eu.keyar.buffalo.resources;

import com.google.gson.Gson;
import eu.keyar.buffalo.management.OperationExecutor;
import eu.keyar.buffalo.resources.model.connector.WebConnector;
import org.jboss.dmr.ModelNode;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/connector")
public class HttpConnectorExposer {

    @Inject
    OperationExecutor executor;

    @GET
    @Path("{type}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listJVMMemoryMetrics(@PathParam("type") String type) {
        final ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("recursive").set("true");
        op.get("include-runtime").set("true");

        ModelNode address = op.get("address");
        address.add("subsystem", "web");
        address.add("connector", type);

        ModelNode returnVal = executor.execute(op);
        String json = returnVal.get("result").toJSONString(false);
        WebConnector connector = new Gson().fromJson(json, WebConnector.class);

        return new Gson().toJson(connector);
    }

}
