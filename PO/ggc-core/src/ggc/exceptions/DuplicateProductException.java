package ggc.exceptions;


/* Exception to when there is a duplicate product. */
public class DuplicateProductException extends Exception {

    /** Class serial number. */
    private static final long serialVersionUID = 202110271722L;
  
    /** Product id. */
    private String _id;
  
    /** @param id the duplicated id */
    public DuplicateProductException(String id) {
      _id = id;
    }
  
    /**
     * @return the invalid Product id.
     */
    public String getProductid() {
      return _id;
    }
  
  }
