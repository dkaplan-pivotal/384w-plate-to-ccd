package com.sleepeasysoftware.platetoccd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
@RunWith(SpringRunner.class)
@SpringBootTest({"input=src/test/resources/happy_path_input.xlsx", "output=src/test/resources/test_output.xlsx"})
public class AcceptanceTest {

    @Test
    public void headerOutput() throws Exception {
        List<List<Optional<String>>> sheet = new ExcelParser().parseFirstSheet("src/test/resources/test_output.xlsx");

        List<Optional<String>> header = sheet.get(0);
        assertThat(header.get(0), equalTo("Plate"));
        assertThat(header.get(1), equalTo("Well"));
        assertThat(header.get(2), equalTo("Data"));
    }
}
