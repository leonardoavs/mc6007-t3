package com.mc6007.t1.repository;

import com.mc6007.t1.domain.PersistentAuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Spring Data MongoDB repository for the {@link PersistentAuditEvent} entity.
 */
public interface PersistenceAuditEventRepository extends PagingAndSortingRepository<PersistentAuditEvent, String> {

    List<PersistentAuditEvent> findByPrincipal(String principal);

    List<PersistentAuditEvent> findByAuditEventDateAfter(Date after);

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, Date after);

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfterAndAuditEventType(String principal, Date after, String type);

    Page<PersistentAuditEvent> findAllByAuditEventDateBetween(Date fromDate, Date toDate, Pageable pageable);
}
