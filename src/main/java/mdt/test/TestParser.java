package mdt.test;

import java.io.File;

import mdt.aas.AssetAdministrationShellParser;
import mdt.aas.model.submodel.Submodel;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class TestParser {
	public static final void main(String... args) throws Exception {
//		AssetAdministrationShell result
//			= AssetAdministrationShellParser.parseJson(new File("aas_docs/AssetAdministrationShell.json"),
//														AssetAdministrationShell.class);
//		Submodel result
//			= AssetAdministrationShellParser.parseJson(new File("aas_docs/Submodel.json"), Submodel.class);

		Submodel result
			= AssetAdministrationShellParser.parseJson(new File("aas_docs/Environment-CustomDataSpec.json"),
														Submodel.class);
//		Environment result
//			= AssetAdministrationShellParser.parseJson(new File("aas_docs/MotorAAS.json"), Environment.class);
		
		System.out.println(result);
//		System.out.println(shell.getIdShort());
//		System.out.println(shell.getAssetInformation().getAssetKind());
//		System.out.println(shell.getAssetInformation().getGlobalAssetId());
//		System.out.println(shell.getDescription());
//		System.out.println("submodels: " + shell.getSubmodelIds());
	}
}
