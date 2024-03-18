package mdt.aas.model;

import java.util.Collections;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class ModelReference extends Reference {
	public ModelReference() {
		super(ReferenceTypes.ModelReference, Collections.emptyList());
	}
	public ModelReference(Key key) {
		super(ReferenceTypes.ModelReference, key);
	}
	
	public static class ReferableReference extends ModelReference {
		public ReferableReference() { }
		public ReferableReference(String key) {
			super(new Key(KeyTypes.Referable, key));
		}
	}
	
	public static class AssetAdministrationShellReference extends ModelReference {
		public AssetAdministrationShellReference() { }
		public AssetAdministrationShellReference(String key) {
			super(new Key(KeyTypes.AssetAdministrationShell, key));
		}
	}
	
	public static class SubmodelReference extends ModelReference {
		public SubmodelReference() { }
		public SubmodelReference(String key) {
			super(new Key(KeyTypes.Submodel, key));
		}
	}
}
