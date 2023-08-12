package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);
    @Autowired
    private CompensationService compensationService;

    @PostMapping("/employee/{id}/compensation")
    public Compensation create(@RequestBody Compensation compensation, @PathVariable String id, HttpServletResponse response){
        LOG.debug("Received employee compensation create request for id [{}] ", id);
        return compensationService.createCompensation(id, compensation);

    }

    @GetMapping("/employee/{id}/compensation")
    public Compensation read(@PathVariable String id, HttpServletResponse response){
        LOG.debug("Received employee compensation read request for id [{}] ", id);


        return compensationService.readCompensation(id).orElseGet(
                () ->  { // no compensation is not an error state, so just return no data back and 204 (would document this and inform clients)
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return null;}
            );

    }
}
