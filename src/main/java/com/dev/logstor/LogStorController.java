package com.dev.logstor;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.logstor.models.LogStor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/logs")
public class LogStorController {
	
	@Autowired
	LogStorService service_obj;
	
	@Autowired
	StringProducer producer;
	
	@GetMapping("")
	List<LogStor> GetLogs() {
		return service_obj.repo_obj.findAll();
	}
	
	@PostMapping("/post")
	ResponseEntity<String> postLogs(@RequestBody LogStor log) {
		log.setCreatedTime(new java.util.Date().toString());
		log.setUpdatedTime(new java.util.Date().toString());
		
		ObjectMapper mapper = new ObjectMapper();
		String logString = null;
		try {
			logString = mapper.writeValueAsString(log);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(producer.sendJsonString(logString));
//		return service_obj.postLogs(log);
	}
	
	@GetMapping(value="/getLog/",params="type")
	List<LogStor> getLogsByType(@RequestParam("type") String type) {
		return service_obj.getLogBasedOnType(type);
	}
	
	@GetMapping(value="/getLog/",params="appName")
	List<LogStor> getLogsByApplicationName(@RequestParam("appName") String name) {
		return service_obj.getLogBasedOnAppName(name);
	}
}
