package mdt.aas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LangString {
	@JsonProperty("language") private String m_language;
	@JsonProperty("text") private String m_text;
	
	@JsonProperty("language")
	public String getLanguage() {
		return m_language;
	}
	@JsonProperty("language")
	public void setLanguage(String language) {
		m_language = language;
	}
	
	@JsonProperty("text")
	public String getText() {
		return m_text;
	}
	@JsonProperty("text")
	public void setText(String text) {
		m_text = text;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s", m_language, m_text);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(m_text, m_language);
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( this == null || getClass() != LangString.class ) {
			return false;
		}
		
		LangString other = (LangString)obj;
		return Objects.equal(m_text, other.m_text)
				&& Objects.equal(m_language, other.m_language);
	}
}
