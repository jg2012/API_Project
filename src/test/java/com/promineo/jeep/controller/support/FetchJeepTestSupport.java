 package com.promineo.jeep.controller.support;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.JeepModel;

public class FetchJeepTestSupport extends BaseTest {
    protected List<Jeep> buildExpected() {
  	List<Jeep> list = new LinkedList<>();
  
  	list.add(Jeep.builder()
  		.modelId(JeepModel.WRANGLER)
  		.trimLevel("Sport")
  		.numDoors(2)
  		.wheelSize(17)
  		.basePrice(new BigDecimal("28475.00"))
  		.build());
  	list.add(Jeep.builder()
  		.modelId(JeepModel.WRANGLER)
  		.trimLevel("Sport")
  		.numDoors(4)
  		.wheelSize(17)
  		.basePrice(new BigDecimal("31975.00"))
  		.build());
  	
  	Collections.sort(list);
	return list; 
    }
    
}
