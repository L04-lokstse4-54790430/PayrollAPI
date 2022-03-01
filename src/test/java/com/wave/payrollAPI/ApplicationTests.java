package com.wave.payrollAPI;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.wave.payrollAPI.model.employeeReports;
import com.wave.payrollAPI.model.payrollGeneral;

import com.google.gson.Gson;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {



	@LocalServerPort
	private int port;

	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testDataSequenceDecs() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
        = new LinkedMultiValueMap<>();
        body.add("file", getTestFile());
		HttpEntity<MultiValueMap<String, Object>> requestEntity
         = new HttpEntity<>(body, headers);
		 RestTemplate restTemplate = new RestTemplate();
         MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
         mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
         restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);

		ResponseEntity<JSONObject> postResponse = restTemplate.postForEntity(getRootUrl() + "api/v1/payroll_upload", requestEntity, JSONObject.class);
		System.out.println("postResponse!!!!!!!!!!!!! " + postResponse.getBody());
		String payrollReport =  postResponse.getBody().toJSONString();
		Gson gson = new Gson();
		payrollGeneral payrollGeneral = gson.fromJson(payrollReport,payrollGeneral.class); ;
		//check the sequence of Data
		employeeReports curr = null;
		boolean checkEmployeeSeq = false;
		boolean checkTimeOrder = false;

		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-M-d");
		for (employeeReports employeeReport : payrollGeneral.getPayrollReport().getEmployeeReports()) {
			if (curr != null) {
				// compare Employee ID
				if(employeeReport.getEmployeeId().equalsIgnoreCase(curr.getEmployeeId()) || Integer.parseInt(employeeReport.getEmployeeId()) > Integer.parseInt(curr.getEmployeeId())){
					 checkEmployeeSeq = true;
				}else{
					 checkEmployeeSeq = false;
					 break;
				}
				// Check Time Order
				if(employeeReport.getPayPeriod().getStartDate().equalsIgnoreCase(curr.getPayPeriod().getStartDate()) || LocalDate.parse(employeeReport.getPayPeriod().getStartDate(), format).isAfter(LocalDate.parse(curr.getPayPeriod().getStartDate(), format))){
					checkTimeOrder = true;
			   }else{
				    checkTimeOrder = false;
				    break;
			   }

			}
			curr = employeeReport;
			
		}
		System.out.println("checkEmployeeSeq?????????? " + checkEmployeeSeq);
		System.out.println("checkTimeOrder?????????? " + checkTimeOrder);
		Assert.assertTrue("Employee ID Order incorrect", checkEmployeeSeq);
		Assert.assertTrue("Date Order incorrect", checkTimeOrder);
	}

	

	public static Resource getTestFile() throws IOException {
        Path testFile = Paths.get("src/main/resources/time-report-42.csv");
        return new FileSystemResource(testFile.toFile());
    }

}
