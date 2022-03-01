package com.wave.payrollAPI.model;

public class employeeReports {

    private payPeriod payPeriod;

    private String employeeId;

    private String amountPaid;

    public employeeReports(String employeeId, payPeriod payPeriod, String amountPaid) {
        this.employeeId = employeeId;
        this.payPeriod = payPeriod;
        this.amountPaid = amountPaid;
      }
      public payPeriod getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(payPeriod payPeriod) {
        this.payPeriod = payPeriod;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }




}
