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
import static org.junit.Assert.fail;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class AcceptanceTest {

    private static final String OUTPUT_FILE = "src/test/resources/test_output.xlsx";
    private static final int PLATE_COLUMN = 0;
    private static final int WELL_INDEX = 1;
    private static final int DATA_INDEX = 2;

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

        assertThat(sheet, hasSize(1153));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void wellOutput() throws Exception {

        WellWriter wellWriter = new WellWriter();

        List<List<Optional<String>>> sheet = new ExcelParser().parseFirstSheet(OUTPUT_FILE);

        for (int outputRow = 1; outputRow < 1153; outputRow++) {
            char wellLetter = wellWriter.alphabeticRollover(outputRow - 1);
            String wellNumber = wellWriter.wellNumber(outputRow - 1);

            try {
                assertThat("row=" + outputRow, sheet.get(outputRow).get(WELL_INDEX).get(), equalTo(wellLetter + wellNumber));
            } catch (IndexOutOfBoundsException e) {
                fail("Out of bounds on row " + outputRow);
            }
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void dataOutput() throws Exception {
        List<List<Optional<String>>> outputSheet = new ExcelParser().parseFirstSheet(OUTPUT_FILE);

        assertThat(outputSheet.get(1).get(DATA_INDEX).get(), equalTo("4 2"));
        assertThat(outputSheet.get(2).get(DATA_INDEX).get(), equalTo("5 2"));
        assertThat(outputSheet.get(17).get(DATA_INDEX).get(), equalTo("4 3"));
        assertThat(outputSheet.get(18).get(DATA_INDEX).get(), equalTo("5 3"));
        assertThat(outputSheet.get(384).get(DATA_INDEX).get(), equalTo("19 25"));
        assertThat(outputSheet.get(385).get(DATA_INDEX).get(), equalTo("23 2"));
        assertThat(outputSheet.get(386).get(DATA_INDEX).get(), equalTo("24 2"));
        assertThat(outputSheet.get(768).get(DATA_INDEX).get(), equalTo("38 25"));
        assertThat(outputSheet.get(769).get(DATA_INDEX).get(), equalTo("42 2"));
        assertThat(outputSheet.get(1152).get(DATA_INDEX).get(), equalTo("57 25"));
    }
}
