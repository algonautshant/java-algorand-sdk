package com.algorand.algosdk.unit.utils;

import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TestingUtils {
	static ObjectMapper mapper = new ObjectMapper();

	@Test
	public void serializeTxType() {
		//Encoder.encodeToMsgPack(Type)
	}
	public static void verifyResponse(Response r, File body) throws IOException {
		assertThat(r).isNotNull();
		assertThat(r.isSuccessful()).isTrue();

		String expectedString = new String(Files.readAllBytes(body.toPath()));
		String actualString = r.toString();

		JsonNode expectedNode;
		try {
			expectedNode = mapper.readTree(expectedString);
			JsonNode actualNode = mapper.readTree(actualString);
			assertThat(expectedNode).isEqualTo(actualNode);

			// DONE!
			return;
		} catch (JsonProcessingException e) {
		    // Ignore....
		}

		/*
		// Get both as maps. This doesn't work because messagepack converts them to byte arrays and java uses strings.
		// Is the source wrong?
		Map<String,Object> exp = Encoder.decodeFromMsgPack(expectedString, Map.class);
		Map<String,Object> act = Encoder.decodeFromMsgPack(
				Encoder.encodeToBase64(Encoder.encodeToMsgPack(r.body())),
				Map.class);
		 */

		// This isn't totally valid because it is comparing the two objects after being serialized by the same
		// serializer...

		// Manually decode the thing with reflection to get the value type.
		Field f = FieldUtils.getField(r.getClass(), "valueType", true);
		Class value = null;
		try {
			value = (Class) f.get(r);
		} catch (IllegalAccessException e) {
		    Assertions.fail("No good.");
		}

		Object expectedObject = Encoder.decodeFromMsgPack(expectedString, value);

		assertThat(r.body()).isEqualTo(expectedObject);
		//byte[] bytes = Encoder.encodeToMsgPack(r.body());
		//String actualEncodedString = Encoder.encodeToBase64(bytes);
		//assertThat(actualEncodedString).isEqualTo(expectedString);
	}

	public static boolean comparePathUrls(String url1, String url2, String skip) {
		url1 = url1.replace(skip, "");
		url2 = url2.replace(skip, "");
		
		String[] segments1 = url1.split("[&?]");
		String[] segments2 = url2.split("[&?]");
		
		Arrays.sort(segments1, Comparator.naturalOrder());
		Arrays.sort(segments2, Comparator.naturalOrder());
		
		if (segments1.length != segments2.length) {
			return false;
		}
		
		int s2 = 0;
		for (String seg1 : segments1) {
			if (!seg1.equals(segments2[s2])) {
				return false;
			}
			s2++;
		}
		return true;
	}
	
	public static boolean notEmpty(String str) {
		return !str.isEmpty() && !str.equals("none");		
	}
	
	public static boolean notEmpty(Long val) {
		return val != 0;
	}
}
