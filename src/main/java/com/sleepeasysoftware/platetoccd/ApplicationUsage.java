package com.sleepeasysoftware.platetoccd;

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

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
@Component
public class ApplicationUsage implements ApplicationRunner {

    private final HeaderWriter headerWriter;

    @Autowired
    public ApplicationUsage(HeaderWriter headerWriter) {
        this.headerWriter = headerWriter;
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

        if (!new File(arguments.get(0)).exists()) {
            throw new IllegalArgumentException("Could not find the input file.  Looked for " + arguments.get(0));
        }

        if (new File(arguments.get(1)).exists()) {
            throw new IllegalArgumentException("Output file already exists.  The output file must not already exist.  Found " + arguments.get(1));
        }

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");

        headerWriter.execute(sheet);

        try (FileOutputStream fileOut = new FileOutputStream(arguments.get(1))) {
            wb.write(fileOut);
        }
    }
}
