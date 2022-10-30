package com.promineo.jeep.dao;

import java.util.List;

import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.JeepModel;

public interface JeepSalesDao {

    
    List<Jeep> fetchJeeps(JeepModel model, String trim);
 
    
}
