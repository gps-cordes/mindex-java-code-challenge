package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.LocalDate;

// Compensation data could be stored on the Employee object as a list, which would be more idiomatic to mongodb design.
// However, in order to maintain existing endpoint behavior (and avoid breaking any existing external test frameworks)
// that would ideally mean breaking Employee into two objects:
//      one to return to clients without compensation for the existing read / create endpoints
//      one that would be stored in the database with the compensation field(s) attached

// I opted for treating it as a separate mongodb document with a foreign key because it was easier given the existing code.

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
