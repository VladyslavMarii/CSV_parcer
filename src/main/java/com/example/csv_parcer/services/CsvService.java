package com.example.csv_parcer.services;

import com.example.csv_parcer.Entities.CsvEntity;
import com.example.csv_parcer.exceptions.FailToUploadException;
import com.example.csv_parcer.repo.CsvStorageRepo;
import com.example.csv_parcer.specifications.CsvEntitySpecifications;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvService {

    @Autowired
    private CsvStorageRepo repository;

    @Transactional
    public void save(MultipartFile file) throws FailToUploadException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            char delimiter = detectDelimiter(file);
            CSVFormat format = CSVFormat.DEFAULT.withDelimiter(delimiter).withFirstRecordAsHeader();
            CSVParser csvParser = new CSVParser(reader, format);

            List<CsvEntity> dataList = new ArrayList<>();

            // Retrieve headers dynamically
            String[] headers = csvParser.getHeaderMap().keySet().toArray(new String[0]);

            for (CSVRecord csvRecord : csvParser) {
                CsvEntity data = new CsvEntity();

                // Set properties dynamically based on headers
                for (String header : headers) {
                    switch (header) {
                        case "Numeric":
                            data.setColumn1(csvRecord.get(header));
                            break;
                        case "Numeric-2":
                            data.setColumn2(csvRecord.get(header));
                            break;
                        case "Numeric-Suffix":
                            data.setColumn3(csvRecord.get(header));
                            break;
                        // Add more cases for additional headers as needed
                        default:
                            // Handle unexpected headers or ignore them
                            break;
                    }
                }

                // Add CsvEntity to dataList if it contains valid data

                dataList.add(data);

            }

            // Save all entities to repository
            repository.saveAll(dataList);

        } catch (IOException e) {
            e.printStackTrace();
            throw new FailToUploadException("Failed to upload");
        }
    }

    public char detectDelimiter(MultipartFile file) throws IOException {
        Map<Character, Integer> delimiterCounts = new HashMap<>();
        delimiterCounts.put(',', 0);
        delimiterCounts.put(';', 0);
        delimiterCounts.put('\t', 0);

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            int c;
            while ((c = reader.read()) != -1 && c != '\n') {
                char ch = (char) c;
                if (delimiterCounts.containsKey(ch)) {
                    delimiterCounts.put(ch, delimiterCounts.get(ch) + 1);
                }
            }
        }

        return delimiterCounts.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    public List<CsvEntity> search(String keyword) {
        Specification<CsvEntity> spec = CsvEntitySpecifications.containsKeyword(keyword);
        return repository.findAll(spec);
    }
}
