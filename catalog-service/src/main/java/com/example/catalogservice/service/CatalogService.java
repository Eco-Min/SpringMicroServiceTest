package com.example.catalogservice.service;

import com.example.catalogservice.entity.CatalogEntity;
import org.springframework.stereotype.Service;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
