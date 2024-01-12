package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.Stock;
import com.thesis.ecommerceweb.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    StockRepository stockRepository;

    public List<Stock> getStockByPid(int pid) {
        return stockRepository.findAllByPid(pid);
    }

    public Optional<Stock> getStock(int pid, String size) {
        return stockRepository.findStockByPidAndSize(pid, size);
    }

    public Optional<Stock> getStockOneProduct(int pid) {
        return stockRepository.findStockByPid(pid);
    }

    public List<String> getAllSizes() {
        return stockRepository.findAllSizes();
    }
}
