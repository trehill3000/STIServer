package com.stiserver.webAutomation;

import com.stiserver.webAutomation.bLogic.Main2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebAutomationApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebAutomationApplication.class, args);

		Main2.main2();
	}

}
