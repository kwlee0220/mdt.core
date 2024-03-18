package mdt.model;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceExistsException extends MDTInstanceManagerException {
	private static final long serialVersionUID = 1L;

	public MDTInstanceExistsException(String details, Throwable cause) {
		super(details, cause);
	}
	
	public MDTInstanceExistsException(String details) {
		super(details);
	}
	
	public MDTInstanceExistsException(Throwable cause) {
		super(cause);
	}
}
