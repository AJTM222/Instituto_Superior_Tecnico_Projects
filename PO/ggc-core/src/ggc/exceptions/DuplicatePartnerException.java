package ggc.exceptions;


/* Exception to when there is a duplicate partner. */
public class DuplicatePartnerException extends Exception {

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;
  
    /** Partner id. */
    private String _id;
  
    /** @param id the duplicated id */
    public DuplicatePartnerException(String id) {
      _id = id;
    }
  
    /**
     * @return the invalid Partner id.
     */
    public String getPartnerid() {
      return _id;
    }
  
  }
