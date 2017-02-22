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
public class DataWriter {
    private static final int DATA_INDEX = 2;
    private static final int DATA_ROWS_PER_PLATE = 16;
    private static final int DATA_CELLS_PER_PLATE = 384;
    private static final int DATA_COLUMNS_PER_PLATE = 24;

    public void execute(List<List<Optional<String>>> inputSheet, Sheet outputSheet) {
        int outputRow = 1;  //not 0 because of the header row


        for (int iterations = 1; iterations < outputSheet.getPhysicalNumberOfRows(); iterations++) {
            int outputRowMinusHeader = outputRow - 1;
            int inputRowHeaderOffset = 3;
            int inputRowIndex = getInputRowIndex(outputRowMinusHeader, inputRowHeaderOffset);
            int inputOffsetToData = 1;
            int inputColumnIndex = getInputColumnIndex(outputRowMinusHeader) + inputOffsetToData;
            String inputData = inputSheet.get(inputRowIndex).get(inputColumnIndex).orElseThrow(()-> new IndexOutOfBoundsException("inputRowIndex=" + inputRowIndex + " inputColumnIndex=" + inputColumnIndex + " outputRowMinusHeader=" + outputRowMinusHeader));

            Row row = outputSheet.getRow(outputRow);
            row.createCell(DATA_INDEX).setCellValue(inputData);
            outputRow++;
        }
    }

    public int getInputRowIndex(int outputRowMinusHeader, int inputRowHeaderOffset) {
        int plateNumber = (outputRowMinusHeader / DATA_CELLS_PER_PLATE) + 1;
        return (outputRowMinusHeader % DATA_ROWS_PER_PLATE) + (inputRowHeaderOffset * plateNumber) + ((plateNumber - 1) * DATA_ROWS_PER_PLATE);
    }

    public int getInputColumnIndex(int outputRowMinusHeader) {
        return (outputRowMinusHeader / DATA_ROWS_PER_PLATE) % DATA_COLUMNS_PER_PLATE;
    }
}
