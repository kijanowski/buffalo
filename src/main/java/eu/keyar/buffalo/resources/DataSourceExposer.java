package eu.keyar.buffalo.resources;

import eu.keyar.buffalo.management.ModelControllerClientProvider;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Exposes metrics of the ExampleDS data source
 */
@Path("/datasource")
public class DataSourceExposer extends ModelControllerClientProvider {

    private ModelNode listDataSourceMetrics(String dataSource, String descriptor) {
        String host = System.getProperty("jboss.bind.address.management", "localhost");
        ModelControllerClient client = createClient();

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

        try {
            ModelNode returnVal = client.execute(op);
            client.close();
            return returnVal.get("result");
        } catch (IOException ioe) {
            throw new RuntimeException("Could not collect metrics.", ioe);
        }
    }

    @GET
    @Path("/{dataSource}/{descriptor}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listDeployedDataSource(@PathParam("dataSource") String dataSource,
                                         @PathParam("descriptor") String descriptor) {
        return listDataSourceMetrics(dataSource, descriptor).toJSONString(false);
    }

    @GET
    @Path("/{dataSource}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listInternalDataSource(@PathParam("dataSource") String dataSource) {
        return listDataSourceMetrics(dataSource, null).toJSONString(false);
    }

}