package com.sleepeasysoftware.platetoccd.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class ExcelParserTest {

    private ExcelParser subject;
    private int countPlate1;
    private int countPlate2;
    private int countPlate3;
    private int count19_25;
    private int count23_2;
    private int count24;
    private int countP;
    private int countNulls;

    @Before
    public void setUp() throws Exception {
        subject = new ExcelParser();
    }

    @Test
    public void canReadInputFile() throws Exception {

        List<List<Optional<String>>> data = subject.parseFirstSheet("src/test/resources/happy_path_input.xlsx");

        data.forEach(row -> row.forEach(cell -> {
            if (!cell.isPresent()) {
                countNulls++;
            } else {
                Object cellData = cell.get();
                if ("Plate1".equals(cellData.toString())) {
                    countPlate1++;
                }
                if ("Plate2".equals(cellData.toString())) {
                    countPlate2++;
                }
                if ("Plate3".equals(cellData.toString())) {
                    countPlate3++;
                }
                if ("19 25".equals(cellData.toString())) {
                    count19_25++;
                }
                if ("23 2".equals(cellData.toString())) {
                    count23_2++;
                }
                if ("P".equals(cellData.toString())) {
                    countP++;
                }
                if ("24".equals(cellData.toString())) {
                    count24++;
                }
            }
        }));

        assertThat(data.toString(), countPlate1, equalTo(1));
        assertThat(data.toString(), countPlate2, equalTo(1));
        assertThat(data.toString(), countPlate3, equalTo(1));
        assertThat(data.toString(), count19_25, equalTo(1));
        assertThat(data.toString(), count23_2, equalTo(1));
        assertThat(data.toString(), countNulls, greaterThan(0));
        assertThat(data.toString(), count24, equalTo(3));
        assertThat(data.toString(), countP, equalTo(3));
    }

    @Test
    public void blankDataIsNotCollapsed() throws Exception {
        List<List<Optional<String>>> data = subject.parseFirstSheet("src/test/resources/input_with_blanks.xlsx");

        for (int i = 0; i < data.size(); i++) {
            List<Optional<String>> row = data.get(i);

            if (row.get(0).isPresent()) {
                String firstCell = row.get(0).get();
                if (firstCell.length() == 1 && StringUtils.isAlpha(firstCell)) {
                    assertTrue("wrong number of columns for row " + i + "\n" + row, row.get(24).isPresent());
                }
            }
        }
    }

}
