package com.promineo.jeep.service;

import com.promineo.jeep.entity.Order;
import com.promineo.jeep.entity.OrderRequest;

public interface JeepOrderService {

    Order createOrder(OrderRequest orderRequest);

}
