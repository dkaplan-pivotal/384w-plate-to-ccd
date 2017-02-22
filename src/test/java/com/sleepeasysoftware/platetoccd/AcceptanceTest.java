package com.sleepeasysoftware.platetoccd;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.List;
import java.util.Optional;

import static com.sleepeasysoftware.platetoccd.ApplicationUsageTest.EXISTING_INPUT_FILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class AcceptanceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        new SpringApplicationBuilder(Application.class).
                run(EXISTING_INPUT_FILE, "src/test/resources/test_output.xlsx");
    }

    @Test
    public void headerOutput() throws Exception {
        List<List<Optional<String>>> sheet = new ExcelParser().parseFirstSheet("src/test/resources/test_output.xlsx");

        List<Optional<String>> header = sheet.get(0);
        assertThat(header.get(0), equalTo("Plate"));
        assertThat(header.get(1), equalTo("Well"));
        assertThat(header.get(2), equalTo("Data"));
    }
}
