package com.promineo.jeep.controller;

import org.springframework.web.bind.annotation.RestController;

import com.promineo.jeep.entity.Order;
import com.promineo.jeep.entity.OrderRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BasicJeepOrderController implements JeepOrderController {

    @Override
    public Order createOrder(OrderRequest orderRequest) {

	log.debug("Order={}", orderRequest);
	return null;
    }

}
