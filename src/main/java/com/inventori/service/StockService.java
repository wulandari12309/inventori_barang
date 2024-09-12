package com.inventori.service;

import com.inventori.DTO.StockDTO;
import com.inventori.entity.Stock;
import com.inventori.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Stock createStock(StockDTO stockDTO, String createdBy) {
        // Initialize a Stock entity
        Stock stock = new Stock();
        stock.setNamaBarang(stockDTO.getNamaBarang());
        stock.setJumlahStokBarang(stockDTO.getJumlahStokBarang());
        stock.setNomorSeriBarang(stockDTO.getNomorSeriBarang());
        stock.setAdditionalInfo(stockDTO.getAdditionalInfo());

        // Handle the image file (if present)
        if (stockDTO.getGambarBarang() != null && !stockDTO.getGambarBarang().isEmpty()) {
            try {
                byte[] imageBytes = stockDTO.getGambarBarang().getBytes();
                stock.setGambarBarang(imageBytes);  // Save the image as a byte array in the Stock entity
            } catch (IOException e) {
                throw new RuntimeException("Error processing image", e);
            }
        }

        // Set other fields
        stock.setCreatedAt(LocalDateTime.now());
        stock.setCreatedBy(createdBy);
        stock.setUpdatedAt(LocalDateTime.now());
        stock.setUpdatedBy(createdBy);

        // Save the Stock entity to the database
        return stockRepository.save(stock);
    }

    public List<Stock> listAllStock() {
        return stockRepository.findAll();
    }

    public Stock getStockDetail(Long idBarang) {
        return stockRepository.findById(idBarang)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with ID: " + idBarang));
    }

    public Stock updateStock(Long idBarang, StockDTO stockDTO, String updatedBy) throws IOException {
        Stock stock = getStockDetail(idBarang);
        stock.setNamaBarang(stockDTO.getNamaBarang());
        stock.setJumlahStokBarang(stockDTO.getJumlahStokBarang());
        stock.setNomorSeriBarang(stockDTO.getNomorSeriBarang());
        stock.setAdditionalInfo(stockDTO.getAdditionalInfo());

        // Handle image update (if present)
        MultipartFile file = stockDTO.getGambarBarang();
        if (file != null && !file.isEmpty()) {
            stock.setGambarBarang(file.getBytes());
        }

        stock.setUpdatedAt(LocalDateTime.now());
        stock.setUpdatedBy(updatedBy);

        return stockRepository.save(stock);
    }

    public void deleteStock(Long idBarang) {
        stockRepository.deleteById(idBarang);
    }
}