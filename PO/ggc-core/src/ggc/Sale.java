package ggc;

/**
 * Class Sale is a subclass of a Transaction and it's instantiated
 * when the Store sells products to costumers (Partners).
 */
public class Sale extends Transaction {
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202012040059L;
    
    /** Partner who made the Store sale. */
    private Partner _partner;

    /** Product sold to Partner. */
    private Product _product;

    /** Limit date to sale payment. */
    private int _limitDate;

    /** Amount of Product demanded by Partner. */
    private int _productQuantity;

    /** Total paid sale price. */
    private double _paidprice;

    /**
     * Create sale.
     * 
     * @param id
     *          transaction ID.
     * @param partner
     *          Partner.
     * @param product
     *          product bought.
     * @param limitDate
     *          limit date to pay (no fines)
     * @param amount
     *          qty of product bought.
     */
    public Sale(int id, Partner partner, Product product, int limitDate, int amount){
        super(id);
        _partner = partner;
        _product = product; 
        _limitDate = limitDate;
        _productQuantity = amount;
    }

    /**
     * @return the sale's total price (after taxes).
     * the value returned from this funtion depends on the Partner rank
     * and the date where the payment was made.
     */
    public double getTotalPrice(){
        if (getispaid() == true) {
            return _paidprice;
        }
        int N = _product.getN();
        int gap = getLimitDateGap();
        if (gap >= 0) {
            if (gap >= N) {
                return getBasePrice() * _partner.getrank().p1Discount();
            } 
            else {
                return getBasePrice() * _partner.getrank().p2Discount(gap);
            }
        } 
        else {
            if (gap >= -N) {
                return getBasePrice() * _partner.getrank().p3Discount(-gap);
            } 
            else {
                return getBasePrice() * _partner.getrank().p4Discount(-gap);
            }
        }
    }

    /**
     * @return the sale's Partner.
     */
    public Partner getPartner(){
        return _partner;
    }

    /**
     * @return the sale's product.
     */
    public Product getProduct(){
        return _product;
    }

    /**
     * @return the sale's limit payment date
     */
    public int getLimitDate(){
        return _limitDate;
    }

    /**
     * @return the sale's amount of products
     */
    public int getProductQuantity(){
        return _productQuantity;
    }

    /**
     * Returns the difference between limit payment date and store's current date.
     * 
     * @return >= 0 if paid on time ; < 0 otherwise
     */
    public int getLimitDateGap(){
        return _limitDate - getdate();
    }

    /**
     * @return the price paid.
     */
    public double getpaidprice(){
        return _paidprice;
    }

    /**
     * Updates sale paidprice and paymentDate and makes Partner pay the sale.
     */
    @Override
    public void pay(){
        _paidprice = getTotalPrice();
        setPaymentDate(getdate());
        _partner.paySale(this);
    }

    @Override
    public void accept(TransactionVisitor visitor){
        visitor.visit(this);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString(){
        int b = (int)Math.round(getBasePrice());
        int t = (int)Math.round(getTotalPrice());
        return "VENDA|" + getid() + "|" + getPartner().getid() + "|" + getProduct().getid() + "|" + getProductQuantity() + "|" + b + "|" + t + "|" + getLimitDate()+ (getispaid() == true ? "|" + getpayday() : "");
    }
}
