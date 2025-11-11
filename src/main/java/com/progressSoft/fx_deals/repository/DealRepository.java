package com.progressSoft.fx_deals.repository;

import com.progressSoft.fx_deals.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<Deal, String> {
    boolean existsById(String id);
}
