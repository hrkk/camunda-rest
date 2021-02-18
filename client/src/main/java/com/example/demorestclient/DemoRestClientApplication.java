package com.example.demorestclient;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class DemoRestClientApplication {
	private static String state = "NORMAL";

	public static void main(String[] args) {
		SpringApplication.run(DemoRestClientApplication.class, args);
	}

	@GetMapping("/hello")
	public ResponseEntity<String> hello() throws InterruptedException {
		log.debug("hello service invoked");
		if ("ERROR".equals(state)) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error 500");
		}
		if("LONG_RUN".equals(state)) {
			Thread.sleep(25000);
		} else {
			Thread.sleep(2000);
		}
		return ResponseEntity.ok("hello, state=" + state);
	}

	@GetMapping("/long-run-15sec")
	public ResponseEntity<String> longRun() {
		state = "LONG_RUN";
		return ResponseEntity.ok("state=" + state);
	}


	@GetMapping("/normal")
	public ResponseEntity<String> normal() {
		state = "NORMAL";
		return ResponseEntity.ok("state=" + state);
	}

	@GetMapping("/error-500")
	public ResponseEntity<String> error() {
		state = "ERROR";
		return ResponseEntity.ok("state=" + state);
	}

}
