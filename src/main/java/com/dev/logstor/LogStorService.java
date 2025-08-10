package com.dev.logstor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.logstor.models.LogStor;

@Service
public class LogStorService {
	@Autowired
	LogStorRepository repo_obj;
	
	LogStor postLogs(LogStor log) {
		return repo_obj.save(log);
	}
	
	List<LogStor> getLogBasedOnType(String type) {
		return repo_obj.findLogByLogType(type);
	}
	
	List<LogStor> getLogBasedOnAppName(String name) {
		return repo_obj.findLogByApplicationName(name);
	}
}
