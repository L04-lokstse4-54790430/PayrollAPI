package com.wave.payrollAPI.model;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payroll")
@EntityListeners(AuditingEntityListener.class)
public class workTimeEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "hours_worked", nullable = false)
    private BigDecimal hoursWorked;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "job_group", nullable = false)
    private String jobGroup;

    @Column(name = "file_id", nullable = false)
    private String fileId;
    public workTimeEmployee() {
      }
 
    public workTimeEmployee( LocalDate date, BigDecimal hoursWorked, String employeeId, String jobGroup, String fileId) {
        this.date = date;
        this.hoursWorked = hoursWorked;
        this.employeeId = employeeId;
        this.jobGroup = jobGroup;
        this.fileId = fileId;
      }
    



public long getId() {
        return id;
    }

 
public void setId(long id) {
        this.id = id;
    }

public LocalDate getDate() {
        return date;
    }


public void setDate(LocalDate date) {
        this.date = date;
    }


public BigDecimal getHoursWorked() {
        return hoursWorked;
    }


public void setHoursWorked(BigDecimal hoursWorked) {
        this.hoursWorked = hoursWorked;
    }


public String getEmployeeId() {
        return employeeId;
    }


public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }


public String getJobGroup() {
        return jobGroup;
    }


public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
    
    public String getFileId() {
        return fileId;
    }


public void setFileId(String fileId) {
        this.fileId = fileId;
    }



}
