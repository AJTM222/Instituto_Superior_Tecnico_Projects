package ggc;

import java.io.Serializable;
import java.lang.Math;
import java.nio.channels.spi.AsynchronousChannelProvider;

public class Batch implements Comparable<Batch>, Serializable{

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;

    /* id from the partner */
    private String _partnerId;

    /* id from the product */
    private String _productId;

    /* price per unit*/
    private double _price;

    /* available stock*/
    private int _stock;


    /**
     * Create a batch.
     *
     * @param idproduct
     *            product id.
     * @param idpartner
     *            partner id.
     * @param price
     *            batch price per unit.
     * @param stock
     *            product stock.
     *
     */
    public Batch(String productId, String partnerId, double price, int stock){
        _productId = productId;
        _partnerId = partnerId;
        _price = price;
        _stock = stock;
    }
    
    /**
     * @return batch owner partner.
     */  
    public String getpartnerId(){
        return _partnerId;
    }
    
    /**
     * @return Batch's product.
     */
    public String getproductId(){
        return _productId;
    }

    /**
     * @return price per unit.
     */
    public Double getprice(){
        return _price;
    }
    
    /**
     * @return available stock.
     */
    public int getstock(){
        return _stock;
    }

    /**
     * @param stock
     * 	 new value of stock.
     */
    public void setstock(int stock){
        _stock = stock;
    }
    
    /** 
     * @see  java.util.comparator#compare()
     */
    @Override
    public int compareTo(Batch other) {
        int i = _productId.compareTo(other.getproductId());
        if (i != 0) return i;
        
        i = _partnerId.compareTo(other.getpartnerId());
        if (i != 0) return i;

        i = Double.compare(_price,other.getprice());
        if (i != 0) return i;
        
        return Integer.compare(_stock, other.getstock());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        int p = (int)Math.round(getprice());
        return getproductId() + "|" + getpartnerId() +"|" + p + "|" + getstock();
    }
}
