package com.sleepeasysoftware.platetoccd;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class InputTest {

    private int countPlate1;
    private int countPlate2;
    private int countPlate3;
    private int count19_25;
    private int count23_2;
    private int count24;
    private int countP;
    private int countNulls;

    @Test
    public void canReadInputFile() throws Exception {

        countPlate1 = 0;
        countPlate2 = 0;
        countPlate3 = 0;
        count19_25 = 0;
        count23_2 = 0;
        count24 = 0;
        countP = 0;
        countNulls = 0;


        List<List<Optional<Object>>> data = parseFirstSheet();

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
                if (Objects.equals(cellData, 24.0)) {
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

    private List<List<Optional<Object>>> parseFirstSheet() throws IOException, InvalidFormatException {
        File inputFile = new File("src/test/resources/happy_path_input.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(inputFile);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();

        int cols = 0; // No of columns
        int tmp = 0;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for (int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if (tmp > cols) {
                    cols = tmp;
                }
            }
        }

        List<List<Optional<Object>>> data = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            row = sheet.getRow(r);
            if (row != null) {
                List<Optional<Object>> rowData = new ArrayList<>();
                for (int c = 0; c < cols; c++) {
                    cell = row.getCell(c);
                    if (cell != null) {
                        if (cell.getCellTypeEnum() == CellType.BLANK) {
                            rowData.add(Optional.empty());
                        } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            rowData.add(Optional.of(cell.getNumericCellValue()));
                        } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
                            rowData.add(Optional.of(cell.getRichStringCellValue().getString()));
                        } else {
                            rowData.add(Optional.of(cell.toString()));
                        }
                    }
                }
                data.add(rowData);
            }
        }

        return data;
    }
}
