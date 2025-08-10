package com.dev.logstor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.logstor.models.LogStor;

@RestController
@RequestMapping("/api/logs")
public class LogStorController {
	
	@Autowired
	LogStorService service_obj;
	
	@GetMapping("")
	String GetLogs() {
		return "Welcome to LogStor";
	}
	
	@PostMapping("/post")
	LogStor postLogs(@RequestBody LogStor log) {
		log.setCreatedTime(new java.util.Date().toString());
		log.setUpdatedTime(new java.util.Date().toString());
		return service_obj.postLogs(log);
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
