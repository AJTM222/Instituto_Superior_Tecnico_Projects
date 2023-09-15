package ggc.exceptions;

/**
 * Partner does not exist.
 */
public class UnknownPartnerException extends Exception {

  /** Class serial number. */
  private static final long serialVersionUID = 202110271722L;

  /** The unknown id. */
  private String _id;

  /**
   * @param id the unknown id.
   */
  public UnknownPartnerException(String id) {
    _id = id;
  }

  /**
   * @return the id.
   */
  public String getId() {
    return _id;
  }

}
