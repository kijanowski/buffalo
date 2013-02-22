package eu.keyar.buffalo.resources;

import com.google.gson.Gson;
import eu.keyar.buffalo.management.OperationExecutor;
import eu.keyar.buffalo.resources.model.jvm.Threading;
import org.jboss.dmr.ModelNode;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/jvm")
public class JVMExposer {

    @Inject
    OperationExecutor executor;

    private static double MB = 1024. * 1024.;

    @GET
    @Path("memory/{type}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listJVMMemoryMetrics(@PathParam("type") String type) {
        final ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("recursive").set("true");
        op.get("include-runtime").set("true");

        ModelNode address = op.get("address");
        address.add("core-service", "platform-mbean");
        address.add("type", "memory");

        ModelNode returnVal = executor.execute(op);

        return returnVal.get("result").get(type).toJSONString(false);
    }

    @GET
    @Path("memory-pool/{name}/{type}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listJVMMemoryPoolMetrics(@PathParam("name") String name, @PathParam("type") String type) {
        final ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("recursive").set("true");
        op.get("include-runtime").set("true");

        ModelNode address = op.get("address");
        address.add("core-service", "platform-mbean");
        address.add("type", "memory-pool");
        address.add("name", name);

        ModelNode returnVal = executor.execute(op);

        String json = returnVal.get("result").toJSONString(false);
        Map<String, Object> pool = new Gson().fromJson(json, Map.class);

        if (pool.get(type) instanceof Map) {
            Map<String, Double> stats = (Map<String, Double>) pool.get(type);
            for (String key : stats.keySet()) {
                stats.put(key, stats.get(key) / MB);

            }
        }
        return new Gson().toJson(pool.get(type));
    }

    @GET
    @Path("threading")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listJVMThreadMetrics() {
        final ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("recursive").set("true");
        op.get("include-runtime").set("true");

        ModelNode address = op.get("address");
        address.add("core-service", "platform-mbean");
        address.add("type", "threading");

        ModelNode returnVal = executor.execute(op);

        String json = returnVal.get("result").toJSONString(false);
        Threading threading = new Gson().fromJson(json, Threading.class);
        return new Gson().toJson(threading);
    }

}
