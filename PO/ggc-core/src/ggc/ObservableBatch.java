package ggc;

import java.io.Serializable;

public interface ObservableBatch extends Serializable {
    /**
     * Registers an Observer (Partner) to a Product.
     * 
     * @param observer
     *          Partner being added as an Observer.
     */
    public void registerObserver(BatchObserver observer);

    /**
     * Removes an Observer (Partner) from a Product.
     * Partner no longer wants to receive notifications about the Observable (Product).
     * 
     * @param observer
     *          Partner being removed as an Observer.
     */
    public void removeObserver(BatchObserver observer);

    /**
     * Notify all observers when the Observable's state has changed.
     * 
     * @param type
     *          Observable's new state.
     *          can be either NEW or BARGAIN.
     * @param price
     * 	 price that will be shown in the notification.
     */
    public void notifyObservers(String type, double price);
}
