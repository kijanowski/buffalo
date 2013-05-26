package eu.keyar.buffalo.resources.model.messaging;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MessagingResource {

    // queue/topic metrics
    @SerializedName("delivering-count")
    private String deliveringCount;
    @SerializedName("message-count")
    private String messageCount;
    @SerializedName("messages-added")
    private String messagesAdded;

    // queue metrics
    @SerializedName("consumer-count")
    private String consumerCount;
    @SerializedName("scheduled-count")
    private String scheduledCount;

    // topic metrics
    @SerializedName("subscription-count")
    private String subscriptionCount;
    @SerializedName("durable-message-count")
    private String durableMessageCount;
    @SerializedName("non-durable-message-count")
    private String nonDurableMessageCount;
    @SerializedName("durable-subscription-count")
    private String durableSubscriptionCount;
    @SerializedName("non-durable-subscription-count")
    private String nonDurableSubscriptionCount;

}