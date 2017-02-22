package com.sleepeasysoftware.platetoccd;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sleepeasysoftware.platetoccd.ApplicationUsageTest.EXISTING_INPUT_FILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class AcceptanceTest {

    private static final String OUTPUT_FILE = "src/test/resources/test_output.xlsx";
    private static final int PLATE_COLUMN = 0;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        new File(OUTPUT_FILE).delete();
        new SpringApplicationBuilder(Application.class).
                run(EXISTING_INPUT_FILE, OUTPUT_FILE);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void headerOutput() throws Exception {

        List<List<Optional<String>>> sheet = new ExcelParser().parseFirstSheet(OUTPUT_FILE);

        List<Optional<String>> header = sheet.get(0);
        assertThat(header.get(0).get(), equalTo("Plate"));
        assertThat(header.get(1).get(), equalTo("Well"));
        assertThat(header.get(2).get(), equalTo("Data"));
        assertThat(header, hasSize(3));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void plateOutput() throws Exception {

        List<List<Optional<String>>> sheet = new ExcelParser().parseFirstSheet(OUTPUT_FILE);

        for (int row = 1; row < 385; row++) {
            assertThat("row=" + row, sheet.get(row).get(PLATE_COLUMN).get(), equalTo("Plate1"));
        }

        for (int row = 386; row < 769; row++) {
            assertThat("row=" + row, sheet.get(row).get(PLATE_COLUMN).get(), equalTo("Plate2"));
        }

        for (int row = 770; row < 1153; row++) {
            assertThat("row=" + row, sheet.get(row).get(PLATE_COLUMN).get(), equalTo("Plate3"));
        }
    }
}
