package eu.keyar.buffalo.resources;

import eu.keyar.buffalo.management.ModelControllerClientProvider;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.InetAddress;

/**
 * Exposes metrics of the ExampleDS data source
 */
@Path("/datasource")
public class DataSourceExposer extends ModelControllerClientProvider {

    private ModelNode listDataSourceMetrics(String dataSource, String descriptor) throws Exception {
        String host = System.getProperty("jboss.bind.address.management", "localhost");
        ModelControllerClient client = createClient(InetAddress.getByName(host), 9999);

        ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("recursive").set("true");
        op.get("include-runtime").set("true");

        ModelNode address = op.get("address");

        if (descriptor != null) {
            address.add("deployment", descriptor);
        }

        address.add("subsystem", "datasources");

        if (descriptor != null) {
            address.add("data-source", "java:jboss/datasources/" + dataSource);
        } else {
            address.add("data-source", dataSource);
        }

        address.add("statistics", "pool");

        ModelNode returnVal = client.execute(op);

        client.close();
        return returnVal.get("result");
    }

    @GET
    @Path("/{dataSource}/{descriptor}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listDeployedDataSource(@PathParam("dataSource") String dataSource,
                                         @PathParam("descriptor") String descriptor) throws Exception {
        return listDataSourceMetrics(dataSource, descriptor).toJSONString(false);
    }

    @GET
    @Path("/{dataSource}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listInternalDataSource(@PathParam("dataSource") String dataSource) throws Exception {
        return listDataSourceMetrics(dataSource, null).toJSONString(false);
    }

}