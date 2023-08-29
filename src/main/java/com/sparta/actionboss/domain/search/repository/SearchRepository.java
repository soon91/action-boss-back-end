package com.sparta.actionboss.domain.search.repository;

import com.sparta.actionboss.domain.search.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Address, Long> {
    Address findByAddress(String keyword);
    List<Address> findByAddressContaining(String keyword);
}
