package com.inventori.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventori.DTO.StockDTO;
import com.inventori.entity.Stock;
import com.inventori.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<Stock> createStock(
            @RequestParam("namaBarang") String namaBarang,
            @RequestParam("jumlahStokBarang") Integer jumlahStokBarang,
            @RequestParam("nomorSeriBarang") String nomorSeriBarang,
            @RequestParam("additionalInfo") String additionalInfoStr,
            @RequestParam("gambarBarang") MultipartFile gambarBarang,
            @RequestParam("createdBy") String createdBy) {

        Map<String, Object> additionalInfo;
        try {
            additionalInfo = objectMapper.readValue(additionalInfoStr, Map.class);
        } catch (IOException e) {
            log.error("Failed to parse additionalInfo: {}", additionalInfoStr, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        StockDTO stockDTO = new StockDTO();
        stockDTO.setNamaBarang(namaBarang);
        stockDTO.setJumlahStokBarang(jumlahStokBarang);
        stockDTO.setNomorSeriBarang(nomorSeriBarang);
        stockDTO.setAdditionalInfo(additionalInfo);
        stockDTO.setGambarBarang(gambarBarang);

        Stock stock = stockService.createStock(stockDTO, createdBy);

        return new ResponseEntity<>(stock, HttpStatus.CREATED);
    }



    // List All Stocks
    @GetMapping
    public ResponseEntity<List<Stock>> listStocks() {
        log.info("Request to list all stocks");  // Log ketika ada request untuk list semua stock
        List<Stock> stocks = stockService.listAllStock();
        log.info("Stocks retrieved: {}", stocks);  // Log hasil list stock
        return ResponseEntity.ok(stocks);
    }

    // Get Stock Detail
    @GetMapping("/{idBarang}")
    public ResponseEntity<Stock> getStockDetail(@PathVariable Long idBarang) {
        log.info("Request to get stock detail for ID: {}", idBarang);  // Log ketika ada request untuk detail stock
        Stock stock = stockService.getStockDetail(idBarang);
        log.info("Stock detail retrieved: {}", stock);  // Log hasil detail stock
        return ResponseEntity.ok(stock);
    }

    // Update Stock
    @PutMapping("/{idBarang}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long idBarang, @ModelAttribute StockDTO stockDTO, @RequestHeader("updatedBy") String updatedBy) {
        log.info("Request to update stock with ID: {}", idBarang);  // Log ketika ada request untuk update stock
        try {
            Stock updatedStock = stockService.updateStock(idBarang, stockDTO, updatedBy);
            log.info("Stock successfully updated: {}", updatedStock);  // Log setelah berhasil update stock
            return ResponseEntity.ok(updatedStock);
        } catch (IOException e) {
            log.error("Error updating stock: {}", e.getMessage());  // Log ketika terjadi error saat update stock
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete Stock
    @DeleteMapping("/{idBarang}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long idBarang) {
        log.info("Request to delete stock with ID: {}", idBarang);  // Log ketika ada request untuk delete stock
        stockService.deleteStock(idBarang);
        log.info("Stock successfully deleted with ID: {}", idBarang);  // Log setelah berhasil delete stock
        return ResponseEntity.ok().build();
    }
}