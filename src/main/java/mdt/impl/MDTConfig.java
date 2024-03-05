package mdt.impl;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import utils.jdbc.JdbcProcessor;

import mdt.docker.DockerConfig;
import mdt.harbor.HarborConfig;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
public final class MDTConfig {
	private static final Logger s_logger = LoggerFactory.getLogger(MDTConfig.class);

	private DockerConfig m_dockerConfig;
	private HarborConfig m_harborConfig;
	private JdbcProcessor.Configuration m_jdbcConfig;
	
	public HarborConfig getHarborConfig() {
		return m_harborConfig;
	}
	public void setHarborConfig(HarborConfig harborConfig) {
		m_harborConfig = harborConfig;
	}
	
	public JdbcProcessor.Configuration getJdbcConfig() {
		return m_jdbcConfig;
	}
	public void setJdbcConfig(JdbcProcessor.Configuration jdbcConfig) {
		m_jdbcConfig = jdbcConfig;
	}
	
	public DockerConfig getDockerConfig() {
		return m_dockerConfig;
	}
	public void setDockerConfig(DockerConfig dockerConfig) {
		m_dockerConfig = dockerConfig;
	}
	
	public JdbcProcessor newJdbcProcessor() {
		return JdbcProcessor.create(m_jdbcConfig);
	}
	
	public static MDTConfig load(Path configFile) throws IOException {
		if ( s_logger.isInfoEnabled() ) {
			s_logger.info("reading a configuration from {}", configFile);
		}
		
		Yaml yaml = new Yaml(new Constructor(MDTConfig.class, new LoaderOptions()));
		MDTConfig config = yaml.load(new FileReader(configFile.toFile()));
		
		return config;
	}
}
	
