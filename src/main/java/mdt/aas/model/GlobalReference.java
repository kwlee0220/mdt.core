package mdt.aas.model;

import java.util.Arrays;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class GlobalReference extends Reference {
	public GlobalReference() {
		m_referenceType = ReferenceTypes.GlobalReference;
	}
	
	public static GlobalReference fromKey(Key key) {
		GlobalReference ref = new GlobalReference();
		ref.setType(ReferenceTypes.GlobalReference);
		ref.setKeys(Arrays.asList(key));
		
		return ref;
	}
}
