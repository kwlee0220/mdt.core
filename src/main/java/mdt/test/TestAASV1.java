package mdt.test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import utils.stream.FStream;

import mdt.impl.MDTConfig;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class TestAASV1 {
	public static final void main(String... args) throws Exception {
		MDTConfig config = MDTConfig.load(Paths.get("mdt_configs.yaml"));

//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode root = mapper.readTree(new File("aas_docs/aas_heater.json"));
		
		Object result;

		DocumentContext ctxt = JsonPath.parse(new File("aas_docs/env_inspection.json"));
		String aasId = null;
		try {
			aasId = ctxt.read("$.assetAdministrationShells[0].id", String.class);
		}
		catch ( PathNotFoundException expected ) {
			aasId = ctxt.read("$.assetAdministrationShells[0].identification.id", String.class);
		}
		System.out.println(aasId);
		
		List<String> submodelIdList = FStream.from(ctxt.read("$.assetAdministrationShells[0].submodels..keys..value", List.class))
											.castSafely(String.class)
											.toList();

		String prefix = aasId + "/";
		int prefixLen = prefix.length();
		List<String> submodelIdShorts
				= FStream.from(submodelIdList)
						.map(id -> id.startsWith(prefix) ? id.substring(prefixLen) : id)
						.toList();
		System.out.println(submodelIdShorts);
		
//		ObjectMapper mapper = new ObjectMapper();
//		AssetAdministrationShellImplV1 shell = mapper.readValue(new File("aas_docs/aas_heater.json"),
//															AssetAdministrationShellImplV1.class);

//		System.out.println(shell);
//		System.out.println("id=" + shell.getId());
//		System.out.println(shell.getSubmodelShortIds(shell.getId() + "/"));
	}
}
