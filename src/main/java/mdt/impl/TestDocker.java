package mdt.impl;

import java.nio.file.Path;
import java.nio.file.Paths;

import mdt.docker.MDTDocker;
import mdt.harbor.HarborConfig;


/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class TestDocker {
	public static final void main(String... args) throws Exception {
		Path configPath = Paths.get("mdt_configs.yaml");
		MDTConfig config = MDTConfig.load(configPath);
		
		HarborConfig harbor = config.getHarborConfig();
		MDTDocker docker = MDTDocker.get(config.getDockerConfig(), harbor);
		
		docker.close();
	}
}
