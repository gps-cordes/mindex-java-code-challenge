package com.mindex.challenge;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;

import static org.junit.Assert.assertEquals;

public class TestUtil {
    public static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    public static void assertCompensationEquivalence(Compensation expected, Compensation actual){
        // check employee
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        // this could go into a utility method shared between the tests
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        // check compensation
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEquals(expected.getSalary(), actual.getSalary());

    }
}
