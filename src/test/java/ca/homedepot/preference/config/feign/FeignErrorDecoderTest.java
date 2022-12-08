package ca.homedepot.preference.config.feign;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Response;
import feign.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static feign.Util.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

class FeignErrorDecoderTest
{


	FeignErrorDecoder feignErrorDecoder;

	@BeforeEach
	void setUp()
	{
		feignErrorDecoder = new FeignErrorDecoder();
	}

	@AfterEach
	void tearDown()
	{
		feignErrorDecoder = null;
	}

	@Test
	void decode()
	{
		// Given
		Map<String, Collection<String>> headers = new LinkedHashMap<>();
		Response response = Response.builder().status(500).reason("Internal server error")
				.request(Request.create(HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8)).headers(headers)
				.body("hello world", UTF_8).build();
		// When
		try
		{
			throw feignErrorDecoder.decode("Service#foo()", response);
		}
		catch (FeignException e)
		{
			assertThat(e.getMessage()).isEqualTo("status 500 reading Service#foo()");
			assertThat(e.contentUTF8()).isEqualTo("hello world");
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}

	}

	@Test
	void decode404Case()
	{
		// Given
		Map<String, Collection<String>> headers = new LinkedHashMap<>();
		Response response = Response.builder().status(404).reason("Internal server error")
				.request(Request.create(HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8)).headers(headers)
				.body("{\"status\": \"NA\", \"details\":\"GCP  not connected\"}", UTF_8).build();
		// When
		try
		{
			throw feignErrorDecoder.decode("Service#foo()", response);
		}
		catch (FeignException e)
		{
			assertThat(e.getMessage()).isEqualTo("status 404 reading Service#foo()");
			assertThat(e.contentUTF8()).isEqualTo("{\"status\": \"NA\", \"details\":\"GCP  not connected\"}");
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}

	}
}