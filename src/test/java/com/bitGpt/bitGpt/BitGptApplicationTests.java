package com.bitGpt.bitGpt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class BitGptApplicationTests {

	@Test
	void contextLoads() {
		String accessKey = "gUdxpoV4JieeqNyNnWJvc6gKK4xjzigd8ScCP5x1";
		String secretKey = "xGxjqCGMTtP2NqnxOvJIwJUMrQz5yIpBkOTLcFrX";
		String serverUrl = "https://api.upbit.com";

		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		String jwtToken = JWT.create()
				.withClaim("access_key", accessKey)
				.withClaim("nonce", UUID.randomUUID().toString())
				.sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
			request.setHeader("Content-Type", "application/json");
			request.addHeader("Authorization", authenticationToken);

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			System.out.println(EntityUtils.toString(entity, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
