package com.wave.payrollAPI.services;
import com.wave.payrollAPI.model.payrollReport;
import com.wave.payrollAPI.model.workTimeEmployee;
import com.wave.payrollAPI.repository.payrollRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wave.payrollAPI.model.employeeReports;
import com.wave.payrollAPI.model.payPeriod;
import com.wave.payrollAPI.model.payrollGeneral;
import com.wave.payrollAPI.services.ReportService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private payrollRepository payrollRepository;


    public ResponseEntity<Object> generateReport(MultipartFile file) throws IOException {

        String refId = String.valueOf(System.currentTimeMillis());
        String fileName = file.getOriginalFilename();
        Map<String, String> result = new HashMap<>();
        result.put("refId", refId);
        if (file.isEmpty()) {
            result.put("msg", "Empty File: " + fileName);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        List<workTimeEmployee> workTimeEmployeeList = new ArrayList<workTimeEmployee>();
        payrollGeneral payrollGeneral = new payrollGeneral(null);

        // prepare datetime formatter
        DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");
        DateTimeFormatter finalFormat = DateTimeFormatter.ofPattern("yyyy-M-d");

        try (InputStream inStream = file.getInputStream()) {
          BufferedReader fileReader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        try (CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {
          
          Iterable<CSVRecord> csvRecords = csvParser.getRecords();

          for (CSVRecord csvRecord : csvRecords) {
            workTimeEmployee workTimeEmployee = new workTimeEmployee(
            LocalDate.parse(csvRecord.get("date"), format),
            BigDecimal.valueOf(Double.valueOf(csvRecord.get("hours worked"))),
            csvRecord.get("employee id"),
            csvRecord.get("job group"),
            fileName

           );
           workTimeEmployeeList.add(workTimeEmployee);
          }
          
          //To group the biweek and add the total working hour for each biweek

          List<workTimeEmployee> res = workTimeEmployeeList.stream()
          .collect(Collectors.toMap(e -> getIntervalDate(e.getDate()) + "|"+ e.getEmployeeId() + "-" + e.getJobGroup() , 
                              e -> e.getHoursWorked(), (a, b) -> a.add(b)))
          .entrySet()
          .stream()
          .map(e -> new workTimeEmployee(LocalDate.parse(e.getKey().substring(0, e.getKey().indexOf("|"))), e.getValue(), StringUtils.substringBetween(e.getKey(), "|", "-"),e.getKey().substring(e.getKey().lastIndexOf("-") + 1), fileName))
          .collect(Collectors.toList());

          List<employeeReports> employeeReportsList = new ArrayList<employeeReports>();


          for (workTimeEmployee workTimeEmployee : res) {
            System.out.println(workTimeEmployee.getDate());
            System.out.println(workTimeEmployee.getEmployeeId());
            System.out.println(workTimeEmployee.getHoursWorked());
            System.out.println(workTimeEmployee.getJobGroup());
            float amountPaid = 0;
            if(workTimeEmployee.getJobGroup().equalsIgnoreCase("A")){
              amountPaid = workTimeEmployee.getHoursWorked().floatValue() * 20;
            }else if (workTimeEmployee.getJobGroup().equalsIgnoreCase("B")){
              amountPaid = workTimeEmployee.getHoursWorked().floatValue() * 30;
            }
            employeeReportsList.add(new employeeReports(
              workTimeEmployee.getEmployeeId(),  
              new payPeriod(
               (workTimeEmployee.getDate().minusDays(15)).format(finalFormat),
               workTimeEmployee.getDate().format(finalFormat)
              ),
              "$" + String.valueOf(amountPaid)
             ));

          }
          Comparator<employeeReports> compareByDate = (employeeReports o1, employeeReports o2) -> LocalDate.parse(o1.getPayPeriod().getStartDate(),finalFormat).compareTo( LocalDate.parse(o2.getPayPeriod().getStartDate(),finalFormat) );
          Comparator<employeeReports> compareById = (employeeReports o1, employeeReports o2) -> Integer.valueOf(o1.getEmployeeId()).compareTo( Integer.valueOf(o2.getEmployeeId()) );
          Collections.sort(employeeReportsList, compareByDate);
          Collections.sort(employeeReportsList, compareById);
          payrollReport payrollReport = new payrollReport(employeeReportsList);
          payrollGeneral = new payrollGeneral(payrollReport);

        }
      }
        
        return new ResponseEntity<Object>(payrollGeneral, HttpStatus.OK);

    }

    public List<workTimeEmployee> storeReport(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        if (file.isEmpty()) {
            return null;
        }

        List<workTimeEmployee> workTimeEmployeeList = new ArrayList<workTimeEmployee>();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");

        try (InputStream inStream = file.getInputStream()) {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
          try (CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {
            
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
  
            for (CSVRecord csvRecord : csvRecords) {
              workTimeEmployee workTimeEmployee = new workTimeEmployee(
              LocalDate.parse(csvRecord.get("date"), format),
              BigDecimal.valueOf(Double.valueOf(csvRecord.get("hours worked"))),
              csvRecord.get("employee id"),
              csvRecord.get("job group"),
              fileName
             );
             workTimeEmployeeList.add(workTimeEmployee);
            }
        }
    }

        
        return payrollRepository.saveAll(workTimeEmployeeList);
    }

    public Boolean fileValidate(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        List<workTimeEmployee> workTimeEmployeeList = new ArrayList<>();
        workTimeEmployeeList = payrollRepository.findAll();
        for (workTimeEmployee workTimeEmployee : workTimeEmployeeList) {
            if(workTimeEmployee.getFileId().equalsIgnoreCase(fileName)){
                return false;
            }
        }
        return true;
    }


    private LocalDate getIntervalDate(LocalDate d) {
        return (d.getDayOfMonth() > 15 ? d.withDayOfMonth(d.lengthOfMonth()) 
                                       : d.withDayOfMonth(15));
      }

}
