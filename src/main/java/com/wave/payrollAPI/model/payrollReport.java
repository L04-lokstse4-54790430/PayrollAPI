package com.wave.payrollAPI.model;

import java.util.List;

public class payrollReport {

    private List <employeeReports> employeeReports;

    public payrollReport( List<employeeReports> employeeReports) {
        this.employeeReports = employeeReports;
      }


    public List <employeeReports> getEmployeeReports() {
        return employeeReports;
    }

    public void setEmployeeReports(List <employeeReports> employeeReports) {
        this.employeeReports = employeeReports;
    }




}
