package com.promineo.jeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.promineo.ComponentScanMarker;

@SpringBootApplication(scanBasePackageClasses = { ComponentScanMarker.class})
public class JeepSales {

    public static void main(String[] args) {
	SpringApplication.run(JeepSales.class, args); 
    }
}
