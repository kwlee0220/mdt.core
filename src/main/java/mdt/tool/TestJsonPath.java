package mdt.tool;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class TestJsonPath {
	public static final void main(String... args) throws Exception {
		DocumentContext aas = JsonPath.parse("aas_heater.json");
		
		Object out = aas.read("$.modelType");
		System.out.println(out);
	}
}
