package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.exception.CompensationAlreadyExistsException;
import com.mindex.challenge.exception.EmployeeDoesNotExistException;
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
        try {
            return compensationService.createCompensation(id, compensation);
        } catch (EmployeeDoesNotExistException | CompensationAlreadyExistsException e){
            // Avoid using 500, so use a 400 when the employee does not exist.
            // Not a 404 because that is often intuited as path not found
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @GetMapping("/employee/{id}/compensation")
    public Compensation read(@PathVariable String id, HttpServletResponse response){
        LOG.debug("Received employee compensation read request for id [{}] ", id);

        try {
            return compensationService.readCompensation(id).orElseGet(
                    () ->  {
                        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                        return null;}
            );
        } catch (EmployeeDoesNotExistException e){
            // Avoid using 500, so use a 400 when the employee does not exist.
            // Not a 404 because that is often intuited as path not found
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }
}
