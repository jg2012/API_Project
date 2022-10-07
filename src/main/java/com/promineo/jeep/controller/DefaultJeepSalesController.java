package com.promineo.jeep.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.promineo.jeep.entity.Jeep;
import com.promineo.jeep.entity.JeepModel;

@RestController
public class DefaultJeepSalesController implements JeepSalesController {

    @Override
    public List<Jeep> fetchJeeps(JeepModel model, String trim) {
	return null;
    }

}
