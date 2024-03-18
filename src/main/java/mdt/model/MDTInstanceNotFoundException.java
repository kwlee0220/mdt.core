package mdt.model;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceNotFoundException extends MDTInstanceManagerException {
	private static final long serialVersionUID = 1L;

	public MDTInstanceNotFoundException(String details, Throwable cause) {
		super(details, cause);
	}
	
	public MDTInstanceNotFoundException(String details) {
		super(details);
	}
	
	public MDTInstanceNotFoundException(Throwable cause) {
		super(cause);
	}
}
