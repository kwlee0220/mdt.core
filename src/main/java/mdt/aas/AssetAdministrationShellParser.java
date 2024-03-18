package mdt.aas;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import mdt.aas.model.GlobalReference;
import mdt.aas.model.MDTAASModelException;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class AssetAdministrationShellParser {
	public static <T> T parseJson(File jsonFile, Class<T> cls) throws MDTAASModelException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			SimpleModule module = new SimpleModule();
			module.addDeserializer(GlobalReference.class, new GlobalReferenceDeserializer());
			mapper.registerModule(module);

			return mapper.readValue(jsonFile, cls);
		}
		catch ( Exception e ) {
			throw new MDTAASModelException("fails to parse Json: file=" + jsonFile);
		}
	}
}
