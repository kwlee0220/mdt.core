package mdt.harbor;


/**
 *
 * @author Kang-Woo Lee (ETRI)
*/
public class HarborConfig {
	private String m_endpoint;
	private String m_userId;
	private String m_password;
	private String m_email = "mdt@etri.re.kr";
	
	public String getEndpoint() {
		return m_endpoint;
	}
	public void setEndpoint(String ep) {
		m_endpoint = ep;
	}
	
	public String getUserId() {
		return m_userId;
	}
	public void setUserId(String id) {
		m_userId = id;
	}
	
	public String getPassword() {
		return m_password;
	}
	public void setPassword(String password) {
		m_password = password;
	}
	
	public String getEmail() {
		return m_email;
	}
	public void setEmail(String email) {
		m_email = email;
	}
}