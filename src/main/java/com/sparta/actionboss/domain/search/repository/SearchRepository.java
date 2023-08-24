package com.sparta.actionboss.domain.search.repository;

import com.sparta.actionboss.domain.search.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Address, Long> {
    Address findByAddressContaining(String search);
}
