package com.promineo.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import com.promineo.jeep.controller.support.FetchJeepTestSupport;
import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.JeepModel;

import io.swagger.v3.oas.models.PathItem.HttpMethod;

    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    @ActiveProfiles("test")
    @Sql(scripts = {
    	"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    	"classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
    	config = @SqlConfig(encoding = "utf-8"))
    class FetchJeepTest {
    
	  @Autowired
	  private TestRestTemplate restTemplate;
	  
	  @LocalServerPort
	  private int serverPort;
    @Test
    void testThatJeepsAreREturnedWhenAValidModelAndTrimAreSupplied() {
	//Given: a valid model, trim and URI
	JeepModel model = JeepModel.WRANGLER; 
	String trim = "Sport";
	String uri = String.format("%s?model=%s,&trim=%s", serverPort,model,trim);
	
	//When: a connection is made to the URI
	
	    ResponseEntity<Jeep> response = restTemplate.getForEntity(uri, Jeep.class);
	
	
	//Then: a success (OK - 200 ) is returned
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	


    } 
    }
    
 