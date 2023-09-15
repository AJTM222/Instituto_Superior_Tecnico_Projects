package ggc;

import java.io.Serializable;

/**
 * Interface that allows the Transactions to be visited by some Visitor.
 */
public interface TransactionVisitable extends Serializable {
    /**
     * Accepts the visitor.
     * @param visitor
     *          visitor that is being accepted.
     */
    void accept(TransactionVisitor visitor);
}