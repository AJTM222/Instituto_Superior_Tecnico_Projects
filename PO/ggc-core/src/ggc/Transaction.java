package ggc;

public abstract class Transaction implements TransactionVisitable {
    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;
    
    /** Transaction's  id. */
    private int _id;

    /** Transaction's base price (before taxes). */
    private double _basePrice = 0;

    /** Transaction's date for payment. */
    private int _payday;

    /** Transaction's paid status */
    private boolean _ispaid = false;

    /** Warehouse's date. */
    private int _date;

    /**
     * Create a Transaction.
     * 
     * @param id
     *          transaction id.
     */
    public Transaction(int id) {
        _id = id;
    }

    /**
     * @return transaction's id
     */
    public int getid() {
        return _id;
    }

    /**
     * @return the transaction's payment date.
     */
    public int getpayday() {
        return _payday;
    }
    
    /**
     * Returns transaction paid status.
     * 
     * @return true if transaction is paid; false, otherwise.
     */
    public boolean getispaid() {
        return _ispaid;
    }

    /**
     * Sets transaction payment date and transaction as paid.
     * 
     * @param date
     *          date of payment.
     */
    public void setPaymentDate(int date) {
        _payday = date;
        _ispaid = true;
    }

    /**
     * Updates transaction with current Warehouse date.
     * @param date
     *          Warehouse's date.
     */
    public void setdate(int date) {
        _date = date;
    }

    /**
     * Returns last saved Warehouse date on transcation.
     * 
     * @return last Warehouse date recorded.
     */
    public int getdate() {
        return _date;
    }

    /**
     * @return the transaction's base price (before discounts/penaltys).
     */
    public double getBasePrice() {
        return _basePrice;
    }

    /**
     * Sets transaction's base price.
     */
    public void setBasePrice(double basePrice) {
        _basePrice = basePrice;
    }

    /**
     * Pays the transaction.
     */
    public abstract void pay();

}