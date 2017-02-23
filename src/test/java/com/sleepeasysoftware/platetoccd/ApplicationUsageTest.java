package com.sleepeasysoftware.platetoccd;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class ApplicationUsageTest {

    public static final String EXISTING_INPUT_FILE = "src/test/resources/happy_path_input.xlsx";
    private static final String EXISTING_OUTPUT_FILE = "src/test/resources/happy_path_input.xlsx";
    public static final String DOES_NOT_EXIST_FILE = "src/test/resources/does_not_exist";

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
    public void requiresInput() throws Exception {

        thrown.expect(IllegalStateException.class);
        subject.run();
    }

    @Test
    public void happyPathHasNoErrors() throws Exception {

        subject.run(EXISTING_INPUT_FILE, DOES_NOT_EXIST_FILE);
    }

    @Test
    public void requireExistingInputFile() throws Exception {
        thrown.expect(IllegalStateException.class);

        subject.run(DOES_NOT_EXIST_FILE, DOES_NOT_EXIST_FILE);
    }

    @Test
    public void requireNonExistingOutputFile() throws Exception {
        thrown.expect(IllegalStateException.class);

        subject.run(EXISTING_INPUT_FILE, EXISTING_OUTPUT_FILE);
    }
}
