package com.promineo.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import com.promineo.jeep.Constants;
import com.promineo.jeep.controller.support.FetchJeepTestSupport;
import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.JeepModel;
import com.promineo.jeep.service.JeepSalesService;

   
    class FetchJeepTest {
	
	@Nested
	 @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	    @ActiveProfiles("test")
	    @Sql(scripts = {
	    	"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
	    	"classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
	    	config = @SqlConfig(encoding = "utf-8"))
	    @Service
	class TestsThatDoNotPolluteTheApplicationContext extends FetchJeepTestSupport{
	    
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
	    
	    
		
		
	    
	    @Test
		void testThatAnErrorMessageisReturnedWhenAnUnkownTrimIsSupplied() {

			// Given : a valid model, trim, and URI
			JeepModel model = JeepModel.WRANGLER;
			String trim = "Unknown Value";
			String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);
			

			// When : a connection is "made to the URI

			ResponseEntity<Map<String,Object>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<>() {});

			// Then : a not found (4040 status cod is returned 
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

			// And: an error message is returned
			Map<String, Object> error = response.getBody(); 
			
			assertErrorMessageValid(error, HttpStatus.NOT_FOUND);
		}

	    
	    @ParameterizedTest
	    @MethodSource("com.promineo.jeep.controller.FetchJeepTest#parametersForInvalidInput")
	   	void testThatAnErrorMessageisReturnedWhenAnInvalidValueIsSupplied(
	   		String model, String trim, String reason) {

	   		// Given : a valid model, trim, and URI
	   		String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);
	   		

	   		// When : a connection is "made to the URI
	   		ResponseEntity<Map<String,Object>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
	   				new ParameterizedTypeReference<>() {});

	   		// Then : a not found (4040 status cod is returned 
	   		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	   		// And: an error message is returned
	   		Map<String, Object> error = response.getBody(); 
	   		
	   		assertErrorMessageValid(error, HttpStatus.BAD_REQUEST);
	   	}
	    
	}
	
	    static Stream<Arguments> parametersForInvalidInput(){
		 //@formatter:off
		 return Stream.of(
			arguments("WRANGLER", "@#$$^&&%", "Trim continas non-alpha-numeric characters"),
			arguments("WRANGLER","C".repeat(Constants.TRIM_MAX_LENGTH + 1), "Trim length too long"), 
			arguments("INVALID", "Sport", "Model is not enum value")
			 ); 
		 //@formatter:on
	     }

	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	    @ActiveProfiles("test")
	    @Sql(scripts = {
	    	"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
	    	"classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
	    	config = @SqlConfig(encoding = "utf-8"))
	 
	class TestThatPolluteTheApplicationContext extends FetchJeepTestSupport{   
	    @MockBean
	    private JeepSalesService jeepSalesService; 
	    
	    @Test
		void testThatAnUnplannedErrorResultsInA500status() {

			// Given : a valid model, trim, and URI
			JeepModel model = JeepModel.WRANGLER;
			String trim = "Invalid";
			String uri = String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);
			
			doThrow(new RuntimeException("Outch!")).when(jeepSalesService).fetchJeeps(model,trim); 
			
			// When : a connection is "made to the URI

			ResponseEntity<Map<String,Object>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<>() {});

			// Then : an Internal server error (500) status is returned
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

			// And: an error message is returned
			Map<String, Object> error = response.getBody(); 
			
			assertErrorMessageValid(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    
	}
    }
    

    
 