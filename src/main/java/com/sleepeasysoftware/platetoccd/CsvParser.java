package com.sleepeasysoftware.platetoccd;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Daniel Kaplan on behalf of Sleep Easy Software.
 */
public class CsvParser {
    public List<List<Optional<String>>> parse(String filePath) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<List<Optional<String>>> parsed = new ArrayList<>();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            List<Optional<String>> row = new ArrayList<>();

            for (String cell : nextLine) {
                if (StringUtils.isBlank(cell)) {
                    row.add(Optional.empty());
                } else {
                    row.add(Optional.of(cell));
                }
            }

            parsed.add(row);
        }
        return parsed;
    }
}
