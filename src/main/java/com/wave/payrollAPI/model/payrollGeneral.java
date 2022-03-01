package com.wave.payrollAPI.model;

public class payrollGeneral {

    private payrollReport payrollReport;

    public payrollGeneral( payrollReport payrollReport) {
        this.payrollReport = payrollReport;
      }


    public payrollReport getPayrollReport() {
        return payrollReport;
    }

    public void setPayrollReport(payrollReport payrollReport) {
        this.payrollReport = payrollReport;
    }




}
