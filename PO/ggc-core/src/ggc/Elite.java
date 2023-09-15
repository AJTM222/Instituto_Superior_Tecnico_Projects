package ggc;

import ggc.PartnerRank;

public class Elite extends PartnerRank {
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202110271722L;
    
    /**
     * Constructor.
     * 
     * @param partner
     *          partner being set as Normal status.
     */
    public Elite(Partner partner) {
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
        return 0.9;
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
        return 0.95;
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
        return 1.0;
    }

    /**
     * Elite Partner pay sale. No rank upgrades possible, just downgrades.
     * Partner has his rank downgraded if he pays 15 or more days 
     * after its limit.
     * @param sale
     *          sale that will be paid.
     */
    @Override
    public void pay(Sale sale) {
    	int daysToPay = sale.getLimitDateGap();
    	if (daysToPay >= 0) {
    		 _partner.setpoints(_partner.getpoints() + 10 * (int) sale.getTotalPrice());
    	}
    	else if (daysToPay < -15) {
    		 _partner.setrank(new Selection( _partner));
    		_partner.setpoints(_partner.getpoints() / 4 );
    	}
    }
    
    /**
     * Elite Partner pay breakdown. 
     *
     * @param breakdown
     *          breakdown that will be paid.
     */
    @Override
    public void paybreakdown(Breakdown breakdown){
        int p = (int)Math.round(breakdown.getpaidprice());
        _partner.setpoints(_partner.getpoints() + 10 * p);
    }
    
    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "ELITE";
    }

}
