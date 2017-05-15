package com.sleepeasysoftware.platetoccd;

import au.com.bytecode.opencsv.CSVWriter;
import com.sleepeasysoftware.platetoccd.model.OutputDataRow;
import com.sleepeasysoftware.platetoccd.model.Plate;
import com.sleepeasysoftware.platetoccd.parser.ExcelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Optional;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
@Component
public class ApplicationUsage implements ApplicationRunner {


    private final ExcelParser excelParser;
    private final DataToPlates dataToPlates;
    private final PlatesToOutputData platesToOutputData;

    @Autowired
    public ApplicationUsage(ExcelParser excelParser, DataToPlates dataToPlates, PlatesToOutputData platesToOutputData) {
        this.excelParser = excelParser;
        this.dataToPlates = dataToPlates;
        this.platesToOutputData = platesToOutputData;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> files = args.getNonOptionArgs();
        if (files.size() != 2) {
            throw new IllegalArgumentException("Incorrect usage:\n" +
                    "You need to pass in two arguments.  The first one is the\n" +
                    "path to the input file.  The second one is the path to the output file.  e.g.,\n" +
                    "java -jar 384w-plate-to-ccd.jar '/Users/pivotal/workspace/384w-plate-to-ccd/src/test/resources/happy_path_input.xlsx' '/Users/pivotal/workspace/384w-plate-to-ccd/src/test/resources/happy_path_output.xlsx'");
        }

        String inputPath = files.get(0);
        if (!new File(inputPath).exists()) {
            throw new IllegalArgumentException("Could not find the input file.  Looked for " + inputPath);
        }

        String outputPath = files.get(1);
        if (new File(outputPath).exists()) {
            throw new IllegalArgumentException("Output file already exists.  The output file must not already exist.  Found " + outputPath);
        }

        List<List<Optional<String>>> inputData = excelParser.parseFirstSheet(inputPath);

        List<Plate> plates = dataToPlates.execute(inputData);

        List<OutputDataRow> outputData = platesToOutputData.execute(plates);

        try (CSVWriter writer = new CSVWriter(new FileWriter(outputPath))) {
            writer.writeNext(new String[]{"Plate", "Well", "Data"});

            for (OutputDataRow outputRow : outputData) {
                String[] rawRow = {outputRow.getPlateName(), outputRow.getWell(), outputRow.getData().orElse("")};
                writer.writeNext(rawRow);
            }
        }
    }
}
