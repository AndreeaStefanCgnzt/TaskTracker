package com.example.learning_springboot.firstapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "application.security.jwt.secret-key=8a3c8e5f2b9d4e7a1c6f3b8d4e2a9f7c5b1d0e4f8a2c7b5e9d3f1a6c4b8e2d0f")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
