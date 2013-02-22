package eu.keyar.buffalo.resources.model.jvm;

import com.google.gson.annotations.SerializedName;

public class Threading {

    @SerializedName("thread-count")
    private String threadCount;
    @SerializedName("peak-thread-count")
    private String peakThreadCount;
    @SerializedName("total-started-thread-count")
    private String totalStartedThreadCount;
    @SerializedName("daemon-thread-count")
    private String daemonThreadCount;
    @SerializedName("current-thread-cpu-time")
    private String currentThreadCPUTime;
    @SerializedName("current-thread-user-time")
    private String currentThreadUserTime;


}
