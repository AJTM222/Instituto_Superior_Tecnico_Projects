package ggc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Class Breakdown is a subclass of a Transaction and it's instantiated
 * when a Partner asks for the Breakdown of a Warehouse´s Product.
 */
public class Breakdown extends Transaction {

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;
    
    /** Breakdown's partner. */
    private Partner _partner;

    /** Breakdown's product. */
    private Product _product;

    /** Breakdown's product amount */
    private int _amount;

    /** Breakdown's paid price */
    private double _paidprice;

    /**
     * Create an Breakdown.
     *
     * @param id
     *            Breakdown id.
     * @param partner
     *            partner id.
     * @param product
     *            product id.
     * @param amount
     *            product amount.
     * @param paidprice
     *            price paid.
     *
     */
    public Breakdown(int id, Partner partner, Product product, int amount, double paidprice){
        super(id);
        _partner = partner;
        _product = product;
        _amount = amount;
        _paidprice = paidprice;
    }

    /**
     * @return the Breakdown's partner id.
     */
    public Partner getpartner() {
        return _partner;
    }

    /**
     * @return the Breakdown's product id.
     */
    public Product getproduct() {
        return _product;
    }

    /**
     * @return the Breakdown's amount id.
     */
    public int getamount() {
        return _amount;
    }

    /**
     * @return the Breakdown's price id.
     */
    public double getpaidprice() {
       return _paidprice;
    }


    /**
     * Pays the Breakdown.
     * Empty because Breakdowns are paid instantanly on the Warehouse.
     */
    @Override
    public void pay(){}

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
        int p = (int)Math.round(getpaidprice());
        return "DESAGREGAÇÃO" +"|" + getid()+ "|" + getpartner().getid() + "|" + getproduct().getid() + "|" + getamount() + "|" + b + "|"+ p +"|"+ getpayday() +"|"+ getproduct().receiptToString(getamount());
    }

}