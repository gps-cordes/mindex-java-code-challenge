package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private CompensationService compensationService;

    @PostMapping("/employee/{id}/compensation")
    public Compensation createCompensation(@RequestBody Compensation compensation, @PathVariable String id){
        LOG.debug("Received employee compensation create request for id [{}] ", id);
        return compensationService.createCompensation(id, compensation);
    }

    @GetMapping("/employee/{id}/compensation")
    public Compensation getCompensation(@PathVariable String id){
        LOG.debug("Received employee compensation request for id [{}] ", id);
        return compensationService.readCompensation(id).orElse(null);
    }
}
