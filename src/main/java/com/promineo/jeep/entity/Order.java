package com.promineo.jeep.entity;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private Long orderPK; 
    private Customer customer; 
    private Jeep model; 
    private Color color; 
    private Engine engine; 
    private Tire tire;
    private List<Option> option; 
    private BigDecimal price; 
  

  
}
