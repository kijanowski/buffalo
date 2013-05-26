package eu.keyar.buffalo.resources;

import com.google.gson.Gson;
import eu.keyar.buffalo.management.OperationExecutor;
import eu.keyar.buffalo.resources.model.messaging.MessagingResource;
import org.jboss.dmr.ModelNode;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/messaging")
public class JMSExposer {

    @Inject
    OperationExecutor executor;

    @GET
    @Path("{server}/{type}/{name}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listJVMMemoryMetrics(@PathParam("server") String server, @PathParam("type") String type,
                                       @PathParam("name") String name) {
        final ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("recursive").set("true");
        op.get("include-runtime").set("true");

        ModelNode address = op.get("address");
        address.add("subsystem", "messaging");
        address.add("hornetq-server", server);
        address.add("jms-" + type, name);

        ModelNode returnVal = executor.execute(op);
        String json = returnVal.get("result").toJSONString(false);
        MessagingResource que = new Gson().fromJson(json, MessagingResource.class);

        return new Gson().toJson(que);
    }

}
