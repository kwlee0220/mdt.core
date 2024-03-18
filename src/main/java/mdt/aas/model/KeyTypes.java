package mdt.aas.model;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public enum KeyTypes {
	// AasReferables
	Referable,

	// GenericFragmentKeys
	FragmentReference,

	// GenericGloballyIdentifiables
	GlobalReference,
	
	// AasIdentifiables
	AssetAdministrationShell,
	ConceptDescription,
	Identifiable,
	Submodel,
	
	// AasSubmodelElements
	AnnotatedRelationshipElement,
	BasicEventElement,
	Blob,
	Capability,
	DataElement,
	Entity,
	EventElement,
	File,
	MultiLanguageProperty,
	Property,
	Operation,
	Range,
	RelationshipElement,
	ReferenceElement,
	SubmodelElement,
	SubmodelElementCollection,
	SubmodelElementList,
}
