package com.inventori.entity;


import com.inventori.converter.JsonAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idBarang;

    @Column(nullable = false)
    private String namaBarang;

    @Column(nullable = false)
    private Integer jumlahStokBarang;

    @Column(nullable = false, unique = true)
    private String nomorSeriBarang;

    @Convert(converter = JsonAttributeConverter.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> additionalInfo;

    @Lob
    @Column(name = "gambar_barang")
    private byte[] gambarBarang; // Ensure this is byte[]

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}