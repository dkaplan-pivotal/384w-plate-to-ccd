package com.sleepeasysoftware.platetoccd;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class ApplicationUsageTest {

    public static final String EXISTING_INPUT_FILE = "src/test/resources/happy_path_input.xlsx";
    private static final String EXISTING_OUTPUT_FILE = "src/test/resources/happy_path_output.xlsx";

    private SpringApplicationBuilder subject;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        subject = new SpringApplicationBuilder(Application.class);
    }

    @Test
    public void requiresInputAndOutput() throws Exception {

        thrown.expect(IllegalStateException.class);
        subject.run();
    }

    @Test
    public void happyPathHasNoErrors() throws Exception {

        subject.run(EXISTING_INPUT_FILE, "does_not_exist");
    }

    @Test
    public void requireExistingInputFile() throws Exception {
        thrown.expect(IllegalStateException.class);

        subject.run("does_not_exist", EXISTING_OUTPUT_FILE);
    }

    @Test
    public void requireNonExistingOutputFile() throws Exception {
        thrown.expect(IllegalStateException.class);

        subject.run(EXISTING_INPUT_FILE, EXISTING_OUTPUT_FILE);
    }
}
