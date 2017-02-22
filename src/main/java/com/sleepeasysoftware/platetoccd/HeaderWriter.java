package com.sleepeasysoftware.platetoccd;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

/**
 * Created by pivotal on 2/21/17.
 */
@Component
public class HeaderWriter {
    public void execute(Sheet sheet) {
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Plate");
        row.createCell(1).setCellValue("Well");
        row.createCell(2).setCellValue("Data");
    }

}
