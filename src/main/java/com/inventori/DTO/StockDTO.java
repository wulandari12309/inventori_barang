package com.inventori.DTO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Data
public class StockDTO {

    private String namaBarang;
    private Integer jumlahStokBarang;
    private String nomorSeriBarang;
    private Map<String, Object> additionalInfo;
    private MultipartFile gambarBarang;
}
