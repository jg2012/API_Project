package com.promineo.jeep.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.promineo.jeep.entity.Color;
import com.promineo.jeep.entity.Customer;
import com.promineo.jeep.entity.Engine;
import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.JeepModel;
import com.promineo.jeep.entity.Option;
import com.promineo.jeep.entity.Order;
import com.promineo.jeep.entity.OrderRequest;
import com.promineo.jeep.entity.Tire;

public interface JeepOrderDao {


   Optional <Customer> fetchCustomer(@NotNull String customer);

    Optional <Engine> fetchEngine(String engineId);

    Optional <Tire> fetchTire(String tireId);

    Optional <Color> fetchColor(String colorId);

    Optional <Jeep> fetchModel(JeepModel model, String trim, int doors);


    List<Option> fetchOptions(List<String> optionIds);

 

    
    Order saveOrder(Customer customer, Jeep jeep, Color color, Engine engine, Tire tire, BigDecimal price,
	    List<Option> options);

    


}
