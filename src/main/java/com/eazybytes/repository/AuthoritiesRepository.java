package com.eazybytes.repository;

import com.eazybytes.model.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepository extends CrudRepository<Authority, Integer> {
}
