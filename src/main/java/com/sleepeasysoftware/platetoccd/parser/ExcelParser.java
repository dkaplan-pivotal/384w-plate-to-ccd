package com.sleepeasysoftware.platetoccd.parser;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
@Component
public class ExcelParser {

    public List<List<Optional<String>>> parseFirstSheet(String filePath) throws IOException, InvalidFormatException {
        File inputFile = new File(filePath);
        XSSFWorkbook wb = new XSSFWorkbook(inputFile);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;
        DataFormatter formatter = new DataFormatter();

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

        List<List<Optional<String>>> data = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            row = sheet.getRow(r);
            if (row != null) {
                List<Optional<String>> rowData = new ArrayList<>();
                for (int c = 0; c < cols; c++) {
                    cell = row.getCell(c);
                    if (cell != null) {
                        if (cell.getCellTypeEnum() == CellType.BLANK) {
                            rowData.add(Optional.empty());
                        } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
                            rowData.add(Optional.of(cell.getRichStringCellValue().getString()));
                        } else {
                            rowData.add(Optional.of(formatter.formatCellValue(cell)));
                        }
                    } else {
                        rowData.add(Optional.empty());
                    }
                }
                data.add(rowData);
            }
        }

        return data;
    }

}
