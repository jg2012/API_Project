package com.promineo.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import com.promineo.jeep.JeepSales;
import com.promineo.jeep.controller.support.CreateOrderTestSupport;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = JeepSales.class)
@ActiveProfiles("test")
@Sql(scripts = {
	"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
	"classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
	config = @SqlConfig(encoding = "utf-8"))
@Service
class CreateOrderTest extends CreateOrderTestSupport {
    
    @Test
    void testCreateOrderReturnsSuccess201() {
	//Given: an order as JSON
	String body = createOrderBody();
	String uri = getBaseURIForOrders(); 
	
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers); 
	
	//When the order is sent
	ResponseEntity<?> response = getRestTemplate().exchange(uri, HttpMethod.POST, bodyEntity, Object.class);
	
	//Then a 201 status is returned 
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED); 
	//And: the returned order is correct
	
    }
}

