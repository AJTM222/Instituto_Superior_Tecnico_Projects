package ggc.exceptions;

/**
 * Product is not valid.
 */
public class NotValidProductException extends Exception {

  /** Class serial number. */
  private static final long serialVersionUID = 202110271722L;

  /** The not valid id. */
  private String _id;

  /**
   * @param id the not valid id.
   */
  public NotValidProductException(String id) {
    _id = id;
  }

  /**
   * @return the id.
   */
  public String getId() {
    return _id;
  }

}