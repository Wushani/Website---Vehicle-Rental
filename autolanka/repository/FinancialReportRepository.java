package org.web.autolanka.repository;

import org.web.autolanka.entity.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long> {
    List<FinancialReport> findByReportType(String reportType);
    List<FinancialReport> findByGeneratedBy(String generatedBy);
    List<FinancialReport> findByReportPeriodStartBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    FinancialReport findByReportName(String reportName);
}