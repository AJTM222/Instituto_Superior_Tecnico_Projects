package ggc;

public class DefaultDeliveryMode implements NotificationDeliveryMode {

    /**
     * Add notification to pending notifications.
     * 
     * @param type
     *          notification event (NEW || BARGAIN)
     * @param productId
     *          product Id.
     * @param price
     *          product price.
     */
    @Override
    public Notification deliverNotification(String type, String productId, int price) {
        return new Notification("", type, productId, price);
    }
 }
