package mdt.kubernetes;


/**
 *
 * @author Kang-Woo Lee (ETRI)
*/
public class KubernetesConfig {
	private String m_host;
	private int m_port;
	
	public String getHost() {
		return m_host;
	}
	public void setHost(String host) {
		m_host = host;
	}
	
	public int getPort() {
		return m_port;
	}
	public void setPort(int port) {
		m_port = port;
	}
}