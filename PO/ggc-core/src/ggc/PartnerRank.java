package ggc;

import java.io.Serializable;

public abstract class PartnerRank implements Serializable {

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;
    
    /** The partner assossiated with the rank. */
    protected Partner _partner;

    /**
     * Initializes Partner's rank.
     * 
     * @param partner
     *          partner whose rank is being instantiated.
     */
    public PartnerRank(Partner partner) {
        _partner = partner;
    }

    /** Period P1 Discount. */
    public double p1Discount() {
        return 0.9;
    }

    /**
     * Period P2 Discount.
     *
     * @param dayslate
     *          difference between current date and limit date.
     * @return reduced price to multiply to base sale price relative to period P2.
     */
    public abstract double p2Discount(int dayslate);

    /**
     * Period P3 Discount.
     * 
     * @param dayslate
     *          difference between current date and limit date.
     * @return reduced price to multiply to base sale price relative to period P3.
     */
    public abstract double p3Discount(int dayslate);

    /**
     * Period P4 Discount.
     * 
     * @param dayslate
     *          difference between current date and limit date.
     * @return reduced price to multiply to base sale price relative to period P4.
     */
    public abstract double p4Discount(int dayslate);
    
    /**
     * Pay the sale.
     * 
     * @param sale
     *          sale that will be paid.
     */
    public abstract void pay(Sale sale);

    /**
     * Pay the breakdown.
     * 
     * @param breakdown
     *          breakdown that will be paid.
     */
    public abstract void paybreakdown(Breakdown breakdown);
    
}
