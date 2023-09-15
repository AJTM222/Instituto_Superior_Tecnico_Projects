package ggc;

import java.io.Serializable;

/**
 * Interface used to declare the visit operations.
 */
public interface TransactionVisitor extends Serializable {

    /**
     * Visit an acquisition.
     * @param acquisition
     *          acquisition being visited.
     */
    void visit(Acquisition acquisition);

    /**
     * Visit a sale.
     * @param sale
     *          sale being visited.
     */
    void visit(Sale sale);
    
    /**
     * Visit a breakdown.
     * @param breakdown
     *          breakdown being visited.
     */
    void visit(Breakdown breakdown);

}