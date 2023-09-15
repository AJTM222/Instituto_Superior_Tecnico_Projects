package ggc;

import java.io.Serializable;

public class Notification implements Serializable {

    /** Serial number for serialization. */
    private static final long serialVersionUID = 202110271722L;
    
    /** Notification delivery mode. */
    private String _deliveryMode;

    /** Type of notification -  NEW or BARGAIN */
    private String _type;

    /** Notification's product ID. */
    private String _productId;

    /** Notification's price. */
    private int _price;

    /**
     * Constructor.
     * 
     * @param deliveryMode
     *          delivery mode.
     * @param type
     *          type of notifcation.
     * @param productId
     *          notification's product Id.
     * @param price
     *          notifications's product price.
     */
    public Notification(String deliveryMode, String type, String productId, int price) {
        _deliveryMode = deliveryMode;
        _type = type;
        _productId = productId;
        _price = price;
    }

    /**
     * @return notification's delivery mode.
     */
    public String getDeliveryModeName() {
        return _deliveryMode;
    }

    /**
     * @return notification's type.
     */
    public String getType() {
        return _type;
    }

    /**
     * @return notification's product Id.
     */
    public String getProductId() {
        return _productId;
    }

    /**
     * @return notification's product price.
     */
    public int getProductPrice() {
        return _price;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getDeliveryModeName() + getType() + "|" + getProductId() + "|" + getProductPrice();
    }
}
