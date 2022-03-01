package com.wave.payrollAPI.repository;

import com.wave.payrollAPI.model.workTimeEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface payrollRepository extends JpaRepository<workTimeEmployee, Long> {}
