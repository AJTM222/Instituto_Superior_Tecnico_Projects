package ggc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Class Acquisition is a subclass of a Transaction and it's instantiated
 * when the Warehouse wants to buy a Product.
 */
public class Acquisition extends Transaction {

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;
    
    /** Acquisition's partner. */
    private String _partnerid;

    /** Acquisition's product. */
    private String _productid;

    /** Acquisition's product amount */
    private int _amount;

    /**
     * Create an Acquisition.
     *
     * @param id
     *            acquisition id.
     * @param partnerid
     *            partner id.
     * @param productid
     *            product id.
     * @param amount
     *            product amount.
     *
     */
    public Acquisition(int id, String partnerid, String productid, int amount){
        super(id);
        _partnerid = partnerid;
        _productid = productid;
        _amount = amount;
    }

    /**
     * @return the Acquisition's partner id.
     */
    public String getpartnerid() {
        return _partnerid;
    }

    /**
     * @return the Acquisition's product id.
     */
    public String getproductid() {
        return _productid;
    }

    /**
     * @return the Acquisition's amount id.
     */
    public int getamount() {
        return _amount;
    }


    /**
     * Pays the Acquisition.
     * Empty because Acquisitions are paid instantanly on the Warehouse.
     */
    @Override
    public void pay() {}

    /**
     * Accepts a transaction visitor.
     */
    @Override
    public void accept(TransactionVisitor visitor) {
        visitor.visit(this);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        int b = (int)Math.round(getBasePrice());
        return "COMPRA|" + getid()+ "|" + getpartnerid() + "|" + getproductid() + "|" + getamount() + "|" + b + "|" + getpayday();
    }

}