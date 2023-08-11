package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {

    @PostMapping("/employee/{id}/compensation")
    public Compensation createCompensation(@RequestBody Compensation compensation, @PathVariable String id){
        return new Compensation();
    }

    @GetMapping("/employee/{id}/compensation")
    public Compensation getCompensation(@PathVariable String id){
        return new Compensation();
    }
}
