package fr.gdufrene.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.gdufrene.appender.WebConsoleAppender;

@RestController
public class ConsoleController {
	
	@GetMapping("/console")
	public String data() {
		return WebConsoleAppender.bos.toString();
	}
	

}
