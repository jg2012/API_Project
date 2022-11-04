package com.promineo.jeep.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import com.promineo.jeep.dao.JeepOrderDao;
import com.promineo.jeep.entity.Color;
import com.promineo.jeep.entity.Customer;
import com.promineo.jeep.entity.Engine;
import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.Option;
import com.promineo.jeep.entity.Order;
import com.promineo.jeep.entity.OrderRequest;
import com.promineo.jeep.entity.Tire;

@Service
public class DefaultJeepOrderService implements JeepOrderService {

    @Autowired
    private JeepOrderDao jeepOrderDao; 
    
    @Transactional
    @Override
    public Order createOrder(OrderRequest orderRequest) {
	
	Customer customer = getCustomer(orderRequest); 
	Jeep jeep = getModel(orderRequest); 
	Color color = getColor(orderRequest);
	Engine engine = getEngine(orderRequest); 
	Tire tire = getTire(orderRequest);  
	List<Option> options = getOption(orderRequest);
	BigDecimal price = jeep.getBasePrice().add(color.getPrice().add(engine.getPrice().add(tire.getPrice()))); 
	
	for(Option option : options ) {
	   price = price.add(option.getPrice()); 
	}
	
	return jeepOrderDao.saveOrder(customer, jeep, color, engine, tire, price, options); 
    }

    private List<Option> getOption(OrderRequest orderRequest) {
	return jeepOrderDao.fetchOptions(orderRequest.getOptions());
	    
	
    }

    protected Tire getTire(OrderRequest orderRequest) {
	return jeepOrderDao.fetchTire(orderRequest.getTire())
		.orElseThrow(() -> new NoSuchElementException("Tire with ID =" 
		+ orderRequest.getTire()+ "was not found"));
    }

    protected Engine getEngine(OrderRequest orderRequest) {
	return jeepOrderDao.fetchEngine(orderRequest.getEngine())
	.orElseThrow(() -> new NoSuchElementException("Engine with ID =" 
		+ orderRequest.getEngine()+ "was not found"));
    }

    protected Color getColor(OrderRequest orderRequest) {
	return jeepOrderDao.fetchColor(orderRequest.getColor())
		.orElseThrow(() -> new NoSuchElementException("Color with ID =" 
		+ orderRequest.getColor()+ "was not found"));
    }

    protected Jeep getModel(OrderRequest orderRequest) {
	return jeepOrderDao
		.fetchModel(orderRequest.getModel(), orderRequest.getTrim(),
			orderRequest.getDoors())
	.orElseThrow(() -> new NoSuchElementException("Model with ID=" 
		+ orderRequest.getModel() + ", trim=" 
	        + orderRequest.getTrim()+orderRequest.getDoors() + " was not found."));
    }

    protected Customer getCustomer(OrderRequest orderRequest) {
	return jeepOrderDao.fetchCustomer(orderRequest.getCustomer())
		.orElseThrow(() -> new NoSuchElementException("Customer with ID =" 
			+ orderRequest.getCustomer()+ "was not found"));
    }

}
