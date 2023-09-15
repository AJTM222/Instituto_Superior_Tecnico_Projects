package ggc;

import java.io.Serializable;

public interface BatchObserver extends Serializable {
    /**
     * Adds a notification to array of Partner notifications.
     * 
     * @param type
     *          notification type.
     * @param productId
     *          product Id.
     * @param price
     *          product price.
     */
    void update(String type, String productId, int price);
}

