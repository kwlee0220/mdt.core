package mdt.aas.model;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import utils.stream.FStream;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class Reference {
	@JsonProperty("type") ReferenceTypes m_referenceType;
	@JsonProperty("referredSemanticId") @Nullable Reference m_referredSemanticId;
	@JsonProperty("keys") List<Key> m_keys;
	
	public Reference() { }
	public Reference(ReferenceTypes refType, Key key) {
		m_referenceType = refType;
		m_keys = Arrays.asList(key);
	}
	public Reference(ReferenceTypes refType, List<Key> keys) {
		m_referenceType = refType;
		m_keys = keys;
	}
	
	@JsonProperty("type")
	public ReferenceTypes getType() {
		return m_referenceType;
	}
	@JsonProperty("type")
	public void setType(ReferenceTypes type) {
		m_referenceType = type;
	}
	
	@JsonProperty("referredSemanticId")
	public @Nullable Reference getReferredSemanticId() {
		return m_referredSemanticId;
	}
	@JsonProperty("referredSemanticId")
	public void setReferredSemanticId(@Nullable Reference referredSemanticId) {
		m_referredSemanticId = referredSemanticId;
	}
	
	@JsonProperty("keys")
	public List<Key> getKeys() {
		return m_keys;
	}
	@JsonProperty("keys")
	public void setKeys(List<Key> keys) {
		Preconditions.checkArgument(keys.size() > 0);
		m_keys = keys;
	}
	
	@Override
	public String toString() {
		return String.format("[%s] %s", m_referenceType, FStream.from(m_keys).map(Key::toString).join(", "));
	}
}
