package eu.keyar.buffalo.resources.model.datasource;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Bag for storing internal and deployed data source names/descriptors
 */
public class DataSources {

    @SerializedName("data-source")
    private Map<String, DataSource> dataSources;

    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }
}
