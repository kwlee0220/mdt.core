package mdt.model;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class SubmodelExistsException extends MDTInstanceManagerException {
	private static final long serialVersionUID = 1L;

	public SubmodelExistsException(String details, Throwable cause) {
		super(details, cause);
	}
	
	public SubmodelExistsException(String details) {
		super(details);
	}
	
	public SubmodelExistsException(Throwable cause) {
		super(cause);
	}
}
