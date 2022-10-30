package com.promineo.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.promineo.jeep.controller.support.FetchJeepTestSupport;
import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.JeepModel;

    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    @ActiveProfiles("test")
    @Sql(scripts = {
    	"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    	"classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
    	config = @SqlConfig(encoding = "utf-8"))
    @Service
    class FetchJeepTest extends FetchJeepTestSupport{
    
	@LocalServerPort
	private int serverPort;

	@Autowired
	private TestRestTemplate restTemplate;
	
	
	
	
		
	@Test
	void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {

		// Given : a valid model, trim, and URI
		JeepModel model = JeepModel.WRANGLER;
		String trim = "Sport";
		String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);
		

		// When : a connection is "made to the URI

		ResponseEntity<List<Jeep>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Jeep>>() {
				});

		// Then : a success (OK - 200) status code is returned
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// And the actual list returned is the same as the expected list
		List<Jeep> actual = response.getBody();
		List<Jeep> expected = buildExpected();

		assertThat(actual).isEqualTo(expected);
	}
    
    }
    
 