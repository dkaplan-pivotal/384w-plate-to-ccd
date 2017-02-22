package com.sleepeasysoftware.platetoccd;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;

import static com.sleepeasysoftware.platetoccd.ApplicationUsageTest.DOES_NOT_EXIST_FILE;
import static com.sleepeasysoftware.platetoccd.ApplicationUsageTest.EXISTING_INPUT_FILE;

public class ApplicationTests {

	private SpringApplicationBuilder subject;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		//noinspection ResultOfMethodCallIgnored
		new File(DOES_NOT_EXIST_FILE).delete();

		subject = new SpringApplicationBuilder(Application.class);
	}

	@Test
	public void contextLoads() {
		subject.run(EXISTING_INPUT_FILE, DOES_NOT_EXIST_FILE);
	}

}
