package mdt.harbor;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTHarborException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MDTHarborException(String details, Throwable cause) {
		super(details, cause);
	}
	
	public MDTHarborException(String details) {
		super(details);
	}
}
