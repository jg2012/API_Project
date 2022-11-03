package com.promineo.jeep.entity;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private Customer customer; 
    private Jeep model; 
    private Color color; 
    private Engine engine; 
    private Tire tire;
    private List<Option> option; 
  

  
}
