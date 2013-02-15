package eu.keyar.buffalo.resources.model.datasource;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Data Source metrics
 */
public class DataSource {

    @SerializedName("jndi-name")
    private String jndiName;

    private Map<String, Map<String, String>> statistics;

    public String getJndiName() {
        return jndiName;
    }

    public Map<String, Map<String, String>> getStatistics() {
        return statistics;
    }
}
