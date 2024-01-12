package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findAllByPid(int pid);
    Optional<Stock> findStockByPidAndSize(int pid, String size);
    Optional<Stock> findStockByPid(int pid);

    @Query("SELECT DISTINCT s.size FROM Stock s")
    List<String> findAllSizes();
}
