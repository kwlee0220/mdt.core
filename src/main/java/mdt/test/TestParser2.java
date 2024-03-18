package mdt.test;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonDeserializer;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class TestParser2 {
	public static final void main(String... args) throws Exception {
		
		JsonDeserializer deser = new JsonDeserializer();

		File jsonFile;
		
		jsonFile = new File("aas_docs/AssetAdministrationShell.json");
		AssetAdministrationShell aas = deser.read(new FileInputStream(jsonFile), AssetAdministrationShell.class);
		System.out.println(aas);

		jsonFile = new File("aas_docs/Submodel.json");
		Submodel submodel = deser.read(new FileInputStream(jsonFile), Submodel.class);
		System.out.println(submodel);

		jsonFile = new File("aas_docs/MotorAAS.json");
		Environment env = deser.read(new FileInputStream(jsonFile), Environment.class);
		System.out.println(env);
	}
}
