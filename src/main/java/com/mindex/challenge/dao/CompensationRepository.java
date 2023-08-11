package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    List<Compensation> findByEmployeeEmployeeIdAndEffectiveDateLessThanOrderByEffectiveDateDesc(String employeeId, LocalDateTime currentDate);

    Optional<Compensation> findByEmployeeEmployeeIdAndEffectiveDate(String employeeId, LocalDate currentDate);

}
