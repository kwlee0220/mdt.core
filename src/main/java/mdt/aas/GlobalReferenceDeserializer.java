package mdt.aas;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import mdt.aas.model.GlobalReference;
import mdt.aas.model.Key;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class GlobalReferenceDeserializer extends StdDeserializer<GlobalReference> {
	private static final long serialVersionUID = 1L;
	
	public GlobalReferenceDeserializer() {
		super(GlobalReference.class);
	}

	@Override
	public GlobalReference deserialize(JsonParser parser, DeserializationContext ctxt)
		throws IOException, JacksonException {
		JsonNode node = parser.getCodec().readTree(parser);
		if ( node instanceof TextNode textNode ) {
			return GlobalReference.fromKey(Key.Identifiable(textNode.asText()));
		}
		else {
			return null;
		}
	}
}
