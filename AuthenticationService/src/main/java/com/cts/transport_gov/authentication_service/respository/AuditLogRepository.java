package com.cts.transport_gov.authentication_service.respository;



import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.transport_gov.authentication_service.model.AuditLog;
import org.slf4j.Logger;
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    
    
}