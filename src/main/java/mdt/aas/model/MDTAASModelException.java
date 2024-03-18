package mdt.aas.model;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTAASModelException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MDTAASModelException(String details, Throwable cause) {
		super(details, cause);
	}
	
	public MDTAASModelException(String details) {
		super(details);
	}
	
	public MDTAASModelException(Throwable cause) {
		super(cause);
	}
}
