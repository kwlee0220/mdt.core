package mdt.aas.model.submodel;

import java.time.Instant;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import mdt.aas.model.ModelReference.ReferableReference;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class BasicEventElement extends EventElement {
	@JsonProperty("observed") ReferableReference m_observed;
	@JsonProperty("direction") Direction m_direction;
	@JsonProperty("state") StateOfEvent m_state;
	@JsonProperty("messageTopic") @Nullable String m_messageTopic;
	@JsonProperty("messageBroker") @Nullable ReferableReference m_messageBroker;
	@JsonProperty("lastUpdate") @Nullable Instant m_lastUpdate;
	@JsonProperty("minInterval") @Nullable Instant m_minInterval;
	@JsonProperty("maxInterval") @Nullable Instant m_maxInterval;
	
	@JsonProperty("observed")
	public ReferableReference getObserved() {
		return m_observed;
	}
	@JsonProperty("observed")
	public void setObserved(ReferableReference observed) {
		m_observed = observed;
	}
	
	@JsonProperty("direction")
	public Direction getDirection() {
		return m_direction;
	}
	@JsonProperty("direction")
	public void setDirection(Direction direction) {
		m_direction = direction;
	}
	
	@JsonProperty("state")
	public StateOfEvent getStateOfEvent() {
		return m_state;
	}
	@JsonProperty("state")
	public void setStateOfEvent(StateOfEvent state) {
		m_state = state;
	}
	
	@JsonProperty("messageTopic")
	public @Nullable String getMessageTopic() {
		return m_messageTopic;
	}
	@JsonProperty("messageTopic")
	public void setMessageTopic(String messageTopic) {
		m_messageTopic = messageTopic;
	}
	
	@JsonProperty("messageBroker")
	public @Nullable ReferableReference getMessageBroker() {
		return m_messageBroker;
	}
	@JsonProperty("messageBroker")
	public void setMessageBroker(@Nullable ReferableReference messageBroker) {
		m_messageBroker = messageBroker;
	}
	
	@JsonProperty("lastUpdate")
	public @Nullable Instant getLastUpdate() {
		return m_lastUpdate;
	}
	@JsonProperty("lastUpdate")
	public void setLastUpdate(Instant lastUpdate) {
		m_lastUpdate = lastUpdate;
	}
	
	@JsonProperty("minInterval")
	public @Nullable Instant getMinInterval() {
		return m_minInterval;
	}
	@JsonProperty("minInterval")
	public void setMinInterval(Instant minInterval) {
		m_minInterval = minInterval;
	}
	
	@JsonProperty("maxInterval")
	public @Nullable Instant getMaxInterval() {
		return m_maxInterval;
	}
	@JsonProperty("maxInterval")
	public void setMaxInterval(Instant maxInterval) {
		m_maxInterval = maxInterval;
	}
}
