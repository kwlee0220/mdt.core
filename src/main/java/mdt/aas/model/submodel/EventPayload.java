package mdt.aas.model.submodel;

import java.time.ZonedDateTime;

import javax.annotation.Nullable;

import org.eclipse.digitaltwin.aas4j.v3.model.ProtocolInformation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import mdt.aas.model.ModelReference.ReferableReference;
import mdt.aas.model.Reference;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventPayload {
	@JsonProperty("source") private ReferableReference m_source;
	@JsonProperty("sourceSemanticId") @Nullable private Reference m_sourceSemanticId;
	@JsonProperty("observableReference") private ReferableReference m_observableReference;
	@JsonProperty("observableSemanticId") @Nullable private Reference m_observableSemanticId;
	@JsonProperty("topic") @Nullable private String m_topic;
	@JsonProperty("subjectId") private Reference m_subjectId;
	@JsonProperty("timestamp") private ZonedDateTime m_timestamp;
	@JsonProperty("payload") @Nullable private String m_payload;
	
	@JsonProperty("source")
	public ReferableReference getSource() {
		return m_source;
	}
	@JsonProperty("source")
	public void setSource(ReferableReference source) {
		m_source = source;
	}

	@JsonProperty("sourceSemanticId")
	public Reference getSourceSemanticId() {
		return m_sourceSemanticId;
	}
	@JsonProperty("sourceSemanticId")
	public void setSourceSemanticId(@Nullable Reference sourceSemanticId) {
		m_sourceSemanticId = sourceSemanticId;
	}
	
	@JsonProperty("observableReference")
	public ReferableReference getObservableReference() {
		return m_observableReference;
	}
	@JsonProperty("observableReference")
	public void setObservableReference(ReferableReference observableReference) {
		m_observableReference = observableReference;
	}
	
	@JsonProperty("observableSemanticId")
	public Reference getObservableSemanticId() {
		return m_observableSemanticId;
	}
	@JsonProperty("observableSemanticId")
	public void setObservableSemanticId(@Nullable Reference observableSemanticId) {
		m_observableSemanticId = observableSemanticId;
	}
	
	@JsonProperty("topic")
	public @Nullable String getTopic() {
		return m_topic;
	}
	@JsonProperty("topic")
	public void setTopic(@Nullable String topic) {
		m_topic = topic;
	}
	
	@JsonProperty("subjectId")
	public @Nullable Reference getSubjectId() {
		return m_subjectId;
	}
	@JsonProperty("subjectId")
	public void setSubjectId(@Nullable Reference subjectId) {
		m_subjectId = subjectId;
	}
	
	@JsonProperty("timestamp")
	public ZonedDateTime getTimestamp() {
		return m_timestamp;
	}
	@JsonProperty("timestamp")
	public void setTimestamp(ZonedDateTime timestamp) {
		m_timestamp = timestamp;
	}
	
	@JsonProperty("payload")
	public @Nullable String getPayload() {
		return m_payload;
	}
	@JsonProperty("payload")
	public void setPayload(@Nullable String payload) {
		m_payload = payload;
	}
}
