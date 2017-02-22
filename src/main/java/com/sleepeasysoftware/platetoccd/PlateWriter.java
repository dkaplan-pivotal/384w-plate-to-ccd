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
public class PlateWriter {

    private static final int PLATE_NAME_INDEX = 0;

    public void execute(List<List<Optional<String>>> inputData, Sheet outputSheet) {
        int outputRow = 1;  //not 0 because of the header row

        for (int inputRowIndex = 1; inputRowIndex < inputData.size(); inputRowIndex++) {
            List<Optional<String>> previousRow = inputData.get(inputRowIndex - 1);
            List<Optional<String>> currentRow = inputData.get(inputRowIndex);

            if (currentRowContainsPlateName(previousRow)) {
                String plateName = currentRow.get(0).orElse("(Plate Name Missing)");
                for (int plateIndex = 0; plateIndex < 384; plateIndex++) {
                    Row row = outputSheet.createRow(outputRow);
                    row.createCell(0).setCellValue(plateName);
                    outputRow++;
                }
            }
        }
    }

    private boolean currentRowContainsPlateName(List<Optional<String>> previousRow) {
        return previousRow.get(0).isPresent() && "Measurement".equals(previousRow.get(PLATE_NAME_INDEX).get());
    }
}
