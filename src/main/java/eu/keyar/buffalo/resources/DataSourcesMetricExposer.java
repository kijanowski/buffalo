package eu.keyar.buffalo.resources;

import com.google.gson.Gson;
import eu.keyar.buffalo.management.OperationExecutor;
import eu.keyar.buffalo.resources.model.datasource.DataSources;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.dmr.ModelNode;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Exposes a particular metric from all available data sources in the form:
 * {"DsName1" : "value1", "DsName2" : "value2"}
 */
@Path("/datasources")
public class DataSourcesMetricExposer {

    private Map<String, String> dataSourceMetric = new HashMap<String, String>();

    @Inject
    OperationExecutor executor;

    @GET
    @Path("/{metric}")
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    public String listDeployedDataSource(@PathParam("metric") String metricName) {
        return listDataSourcesMetric(metricName);
    }

    private String listDataSourcesMetric(String metricName) {

        // read configured data sources
        getDataSourceMetric(metricName, null);

        // read deployed data sources
        getExternalMetrics(metricName);

        return new Gson().toJson(dataSourceMetric);
    }


    private void getExternalMetrics(String metricName) {

        final ModelNode op = new ModelNode();
        op.get(ClientConstants.OP).set("read-children-names");
        op.get("child-type").set(ClientConstants.DEPLOYMENT);

        ModelNode returnVal = executor.execute(op);

        for (ModelNode deployment : returnVal.get("result").asList()) {
            if (deployment.asString().endsWith("-ds.xml")) {
                getDataSourceMetric(metricName, deployment.asString());
            }
        }
    }

    private void getDataSourceMetric(String metricName, String descriptor) {

        final ModelNode op = new ModelNode();
        op.get("operation").set("read-resource");
        op.get("recursive").set("true");
        op.get("include-runtime").set("true");

        ModelNode address = op.get("address");

        if (descriptor != null) {
            address.add("deployment", descriptor);
        }

        address.add("subsystem", "datasources");

        ModelNode returnVal = executor.execute(op);

        String json = returnVal.get("result").toJSONString(false);
        DataSources configuredDataSources = new Gson().fromJson(json, DataSources.class);

        for (String dsName : configuredDataSources.getDataSources().keySet()) {
            dataSourceMetric.put(dsName, configuredDataSources.getDataSources().get(dsName).getStatistics().get("pool").get(metricName));
        }
    }

}