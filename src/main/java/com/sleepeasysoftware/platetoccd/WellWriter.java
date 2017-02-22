package com.sleepeasysoftware.platetoccd;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
@Component
public class WellWriter {

    private static final int WELL_INDEX = 1;

    public void execute(List<List<Optional<String>>> inputData, Sheet outputSheet) {
        int outputRow = 1;  //not 0 because of the header row

        for (int iterations = 1; iterations < outputSheet.getPhysicalNumberOfRows(); iterations++) {
            Row row = outputSheet.getRow(outputRow);
            char outputRowChar = alphabeticRollover(outputRow - 1);
            row.createCell(WELL_INDEX).setCellValue(outputRowChar + wellNumber(outputRow - 1));
            outputRow++;
        }
    }

    public char alphabeticRollover(int spreadsheetRow) {
        return (char) ((int) 'A' + spreadsheetRow % 16);
    }

    public String wellNumber(int spreadsheetRow) {
        return String.format("%02d", ((spreadsheetRow / 16) % 24) + 1);
    }
}
