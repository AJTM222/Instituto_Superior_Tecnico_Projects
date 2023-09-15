package ggc;

import ggc.PartnerRank;
import ggc.Selection;

public class Normal extends PartnerRank{
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202110271722L;
    
    /**
     * Constructor.
     * 
     * @param partner
     *          partner being set as Normal status.
     */
    public Normal(Partner partner){
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
    public double p2Discount(int dayslate){
        return 1.0;
    }

    /**
     * Normal Partner P3 Discount.
     * 
     * @param dayslate
     *          difference between current date and limit date.
     * @return reduced price to multiply to base sale price relative to period P3.
     */
    @Override
    public double p3Discount(int dayslate){
        return 1.0 + 0.05 * dayslate;
    }

    /**
     * Normal Partner P4 Discount.
     * 
     * @param dayslate
     *          difference between current date and limit date.
     * @return reduced price to multiply to base sale price relative to period P4.
     */
    @Override
    public double p4Discount(int dayslate){
        return 1.0 + 0.10 * dayslate;
    }

    /**
     * Normal Partner pay sale.
     * Partner can rank up to Selection or Elite, depending on total points adquired.
     * 
     * @param sale
     *          sale being paid.
     */
    @Override
    public void pay(Sale sale){
        int paymentGap = sale.getLimitDateGap();
        if (paymentGap >= 0){
            _partner.setpoints(_partner.getpoints() + 10 * (int) sale.getpaidprice());
        }
        else{
            _partner.setpoints(0);
        }
        if (_partner.getpoints() > 25000){
            _partner.setrank(new Elite(_partner));
        }
        else if (_partner.getpoints() > 2000){
            _partner.setrank(new Selection(_partner));
        }
    }
    
    /**
     * Normal Partner pay breakdown. Partner can upgrade to selection rank 
     *  if has accumulated more than 2000 points with this transaction.
     *  He can also upgrade to Elite if has more than 25000 points.
     * @param breakdown
     *          breakdown that will be paid.
     */
    @Override
    public void paybreakdown(Breakdown breakdown){
        int p = (int)Math.round(breakdown.getpaidprice());
        _partner.setpoints(_partner.getpoints() + 10 * p);
        if(_partner.getpoints() > 25000){
            _partner.setrank(new Elite(_partner));
        }
        else if (_partner.getpoints() > 2000){
            _partner.setrank(new Selection(_partner));
        }
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString(){
        return "NORMAL";
    }

}
