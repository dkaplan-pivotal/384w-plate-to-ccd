package com.sleepeasysoftware.platetoccd;

import com.sleepeasysoftware.platetoccd.model.OutputDataRow;
import com.sleepeasysoftware.platetoccd.model.Plate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
@Component
public class ApplicationUsage implements ApplicationRunner {

    private final HeaderWriter headerWriter;
    private final PlateWriter plateWriter;
    private final ExcelParser excelParser;
    private final WellWriter wellWriter;
    private final DataWriter dataWriter;
    private final DataToPlates dataToPlates;
    private final PlatesToOutputData platesToOutputData;

    @Autowired
    public ApplicationUsage(HeaderWriter headerWriter, PlateWriter plateWriter, ExcelParser excelParser, WellWriter wellWriter, DataWriter dataWriter, DataToPlates dataToPlates, PlatesToOutputData platesToOutputData) {
        this.headerWriter = headerWriter;
        this.plateWriter = plateWriter;
        this.excelParser = excelParser;
        this.wellWriter = wellWriter;
        this.dataWriter = dataWriter;
        this.dataToPlates = dataToPlates;
        this.platesToOutputData = platesToOutputData;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> arguments = args.getNonOptionArgs();
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Incorrect usage:\n" +
                    "You need to pass in two arguments.  The first one is the\n" +
                    "path to the input file.  The second one is the path to the output file.  e.g.,\n" +
                    "java -jar 384w-plate-to-ccd.jar '/Users/pivotal/workspace/384w-plate-to-ccd/src/test/resources/happy_path_input.xlsx' '/Users/pivotal/workspace/384w-plate-to-ccd/src/test/resources/happy_path_output.xlsx'");
        }

        String inputPath = arguments.get(0);
        if (!new File(inputPath).exists()) {
            throw new IllegalArgumentException("Could not find the input file.  Looked for " + inputPath);
        }

        String outputPath = arguments.get(1);
        if (new File(outputPath).exists()) {
            throw new IllegalArgumentException("Output file already exists.  The output file must not already exist.  Found " + outputPath);
        }

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");

        headerWriter.execute(sheet);

        List<List<Optional<String>>> inputData = excelParser.parseFirstSheet(inputPath);

        List<Plate> plates = dataToPlates.execute(inputData);

        List<OutputDataRow> outputData = platesToOutputData.execute(plates);

        for (int i = 0; i < outputData.size(); i++) {
            OutputDataRow outputRow = outputData.get(i);

            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(outputRow.getPlateName());
            row.createCell(1).setCellValue(outputRow.getWell());
            row.createCell(2).setCellValue(outputRow.getData().orElse(""));
        }

//        plateWriter.execute(inputData, sheet);
//        wellWriter.execute(inputData, sheet);
//        dataWriter.execute(inputData, sheet);

        try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
            wb.write(fileOut);
        }
    }
}
