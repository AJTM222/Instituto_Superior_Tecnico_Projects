package ggc;

import java.io.Serializable;

public interface NotificationDeliveryMode extends Serializable {
 
    /**
     * Add notification to pending notifications.
     * 
     * @param type
     *          notification type (NEW || BARGAIN)
     * @param productId
     *          product Id.
     * @param price
     *          product price.
     */
    public Notification deliverNotification(String type, String productId, int price);
}
