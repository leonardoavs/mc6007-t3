package com.mc6007.t1.repository;

import com.mc6007.t1.domain.Authority;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends PagingAndSortingRepository<Authority, String> {
}
