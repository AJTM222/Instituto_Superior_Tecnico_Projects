package ggc;

/**
 * This class is a concrete visitor, where orders and sales update
 * company's total balance, by overriding each visit method.
 */
public class BalanceVisitor implements TransactionVisitor {
    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;

    /** Warehouse balance. */
    private double _balance = 0;

    /**
     * Visites an acquisition and decreases the warehouse balance.
     * 
     * @param acquisition
     *          acquisition being visited.
     */
    @Override
	public void visit(Acquisition acquisition) {
        _balance -= acquisition.getBasePrice();
	}

    /**
     * Visits an acquisition and increases the warehouse balance.
     * 
     * @param acquisition
     *          acquisition being visited.
     */
	@Override
	public void visit(Breakdown breakdown) {
        _balance += breakdown.getpaidprice();
    }

    /**
     * Visits a sale and increases the warehouse balance.
     * 
     * @param sale
     *          sale being visited.
     */
	@Override
	public void visit(Sale sale) {
        _balance += sale.getTotalPrice();
    }

    /**
     * Returns Warehouse balance.
     * 
     * @return warehouse's balance.
     */
    public int getTotalBalance() {
        return (int)Math.round(_balance);
    }
    
}