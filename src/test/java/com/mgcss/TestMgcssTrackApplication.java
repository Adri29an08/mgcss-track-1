package com.mgcss;

import org.springframework.boot.SpringApplication;

public class TestMgcssTrackApplication {

	public static void main(String[] args) {
		SpringApplication.from(MgcssTrackApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
