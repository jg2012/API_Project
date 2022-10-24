package com.promineo.jeep.service;

import java.util.List;

import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.JeepModel;

public interface JeepSalesService {

    
    List<Jeep> fetchJeeps(JeepModel model, String trim);

}
