package com.cts.transport_gov.report_analytics_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.transport_gov.report_analytics_service.model.Report;


@Repository
public interface IReportRepository extends JpaRepository<Report, Long> {

}	