package com.sleepeasysoftware.platetoccd;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static com.sleepeasysoftware.platetoccd.ApplicationUsageTest.EXISTING_INPUT_FILE;

public class ApplicationTests {

	private SpringApplicationBuilder subject;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		subject = new SpringApplicationBuilder(Application.class);
	}

	@Test
	public void contextLoads() {
		subject.run(EXISTING_INPUT_FILE, "foo");
	}

}
