package com.project.openmarket.global.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TossHttpClient {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {
		HttpURLConnection connection = createConnection(secretKey, urlString);
		try (OutputStream os = connection.getOutputStream()) {
			os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
		}

		try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() :
			connection.getErrorStream();
			 Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
			return (JSONObject)new JSONParser().parse(reader);
		} catch (Exception e) {
			logger.error("Error reading response", e);
			JSONObject errorResponse = new JSONObject();
			errorResponse.put("error", "Error reading response");
			return errorResponse;
		}
	}

	private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestProperty("Authorization",
			"Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(
				StandardCharsets.UTF_8)));
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		return connection;
	}
}