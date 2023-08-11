package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.LocalDate;

@CompoundIndex(def = "{'employee':1, 'effectiveDate':1}", unique = true)
public class Compensation {


    // when writing, only write the employeeID, similar to how the direct reports are written. Retrieval will hydrate it.

    private Employee employee;

    // in dollars
    private int salary;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
