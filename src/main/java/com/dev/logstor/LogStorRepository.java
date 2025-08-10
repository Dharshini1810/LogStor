package com.dev.logstor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.logstor.models.LogStor;


public interface LogStorRepository extends JpaRepository<LogStor, Long>{
	List<LogStor> findLogByLogType(String logType);
	List<LogStor> findLogByApplicationName(String applicationName);
}
