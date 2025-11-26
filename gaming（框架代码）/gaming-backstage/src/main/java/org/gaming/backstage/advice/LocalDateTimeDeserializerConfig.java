/**
 * 
 */
package org.gaming.backstage.advice;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author YY
 *
 */
@JsonComponent
public class LocalDateTimeDeserializerConfig extends JsonDeserializer<LocalDateTime> {
	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
		long timestamp = p.getValueAsLong();
		if (timestamp > 0) {
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
		} else {
			return null;
		}
	}

}
