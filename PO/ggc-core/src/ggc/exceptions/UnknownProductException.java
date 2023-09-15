package ggc.exceptions;

/**
 * Product does not exist.
 */
public class UnknownProductException extends Exception {

  /** Class serial number. */
  private static final long serialVersionUID = 202110271722L;

  /** The unknown id. */
  private String _id;

  /**
   * @param id the unknown id.
   */
  public UnknownProductException(String id) {
    _id = id;
  }

  /**
   * @return the id.
   */
  public String getId() {
    return _id;
  }

}