package ggc.exceptions;

/**
 * Class that represents an invalid date to advance.
 */
public class InvalidDaysInputException extends Exception {

  /** Class serial number. */
  private static final long serialVersionUID = 202110271722L;

  /** Wrong days input. */
  private int _days;

  /** @param days invalid days to report. */
  public InvalidDaysInputException(int days) {
    _days = days;
  }

  /**
   * @return the invalid advance date days.
   */
  public int getInvalidDays() {
      return _days;
  }
}
