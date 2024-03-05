package mdt.impl;

import java.io.File;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import mdt.aas.AssetAdministrationShell;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class TestRepository {
	public static final void main(String... args) throws Exception {
		MDTConfig config = MDTConfig.load(Paths.get("mdt_configs.yaml"));
		
		ObjectMapper mapper = new ObjectMapper();
		AssetAdministrationShell shell = mapper.readValue(new File("aas_heater.json"), AssetAdministrationShell.class);

		System.out.println(shell);
	}
}
