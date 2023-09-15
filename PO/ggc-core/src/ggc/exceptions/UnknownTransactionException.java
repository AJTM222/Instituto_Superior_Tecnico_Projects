package ggc.exceptions;

/**
 * Transaction does not exist.
 */
public class UnknownTransactionException extends Exception {

  /** Class serial number. */
  private static final long serialVersionUID = 202110271722L;

  /** The unknown id. */
  private int _id;

  /**
   * @param id the unknown id.
   */
  public UnknownTransactionException(int id) {
    _id = id;
  }

  /**
   * @return the id.
   */
  public int getId() {
    return _id;
  }

}