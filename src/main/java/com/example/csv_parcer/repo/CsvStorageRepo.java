package com.example.csv_parcer.repo;

import com.example.csv_parcer.Entities.CsvEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author $ {Vladyslav Marii}
 **/
public interface CsvStorageRepo extends JpaRepository<CsvEntity, Long> {
}
