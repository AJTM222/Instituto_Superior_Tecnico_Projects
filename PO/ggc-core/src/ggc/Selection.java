package ggc;

import ggc.PartnerRank;

public class Selection extends PartnerRank {
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202110271722L;
    
    /**
     * Constructor.
     * 
     * @param partner
     *          partner being set as Normal status.
     */
    public Selection(Partner partner) {
        super(partner);
    }

    /**
     * Normal Partner P2 Discount.
     * 
     * @param dayslate
     *          difference between current date and limit date.
     * @return reduced price to multiply to base sale price relative to period P2.
     */
    @Override
    public double p2Discount(int dayslate) {
        if(dayslate >= 2){
            return 0.95;
        }
        return 1.0;
    }

    /**
     * Normal Client P3 Discount.
     * 
     * @param dayslate
     *          difference between current date and limit date.
     * @return reduced price to multiply to base sale price relative to period P3.
     */
    @Override
    public double p3Discount(int dayslate) {
        if(dayslate == 1){
            return 1.0;
        }
        return 1.0 + 0.02 * dayslate;
    }

    /**
     * Normal Client P4 Discount.
     * 
     * @param dayslate
     *          difference between current date and limit date.
     * @return reduced price to multiply to base sale price relative to period P4.
     */
    @Override
    public double p4Discount(int dayslate) {
        return 1.0 + 0.05 * dayslate;
    }
    
    /**
     * Selection Partner pay sale. Partner can upgrade rank if paid within
     * the payment date limit and accumulate more than 25000 points.
     * Partner can also downgrade rank if payment is 2 or more days
     * days after its limit.
     *
     * @param sale
     *          sale that will be paid.
     */
    @Override
    public void pay(Sale sale) {
    	int daysToPay = sale.getLimitDateGap();
    	if (daysToPay < -2) {
    		 _partner.setpoints(_partner.getpoints() / 10);
    		 _partner.setrank(new Normal( _partner));
    	}
    	else if (daysToPay >= 0){
    		_partner.setpoints(_partner.getpoints() + 10 * (int) sale.getTotalPrice());
        
    	    if (_partner.getpoints() > 25000){
    	    _partner.setrank(new Elite(_partner));
    	    }
        }
    }

    /**
     * Selection Partner pay breakdown. Partner can upgrade rank if
     * has accumulated more than 25000 points with this transaction.
     *
     * @param breakdown
     *          breakdown that will be paid.
     */
    @Override
    public void paybreakdown(Breakdown breakdown){
        int p = (int)Math.round(breakdown.getpaidprice());
        _partner.setpoints(_partner.getpoints() + 10 * p);
        if (_partner.getpoints() > 25000){
            _partner.setrank(new Elite(_partner));
        }
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "SELECTION";
    }

}
