package ggc.exceptions;

public class NotEnoughProductException extends Exception {
    
    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;

    /** Product id. */
    private String _id;

    /** Requested amount. */
    private int _requested;

    /** Available amount. */
    private int _available;

    /** 
     * @param id the requested id
     * @param requested
     * @param available
     */
    public NotEnoughProductException(String id, int requested, int available) {
    _id = id;
    _requested = requested;
    _available = available;
    }

    /**
    * @return the product id.
    */
    public String getProductid() { return _id; }

    /**
    * @return the amount of requested product.
    */
    public int getRequested() { return _requested; }

    /**
    * @return the amount of available product.
    */
    public int getAvailable() { return _available; }
}