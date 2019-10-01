package com.mc6007.t2.repository;

import com.mc6007.t2.domain.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, String> {

}
