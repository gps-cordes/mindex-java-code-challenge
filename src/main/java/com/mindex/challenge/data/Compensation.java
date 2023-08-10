package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Compensation {


    private Employee employee;

    // in dollars
    private int salary;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

}
