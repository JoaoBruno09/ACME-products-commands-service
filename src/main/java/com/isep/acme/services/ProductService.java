package com.isep.acme.services;

import java.util.Optional;

import com.isep.acme.model.Product;
import com.isep.acme.model.ProductDTO;
import com.isep.acme.model.ProductDetailDTO;

public interface ProductService {

    ProductDTO create(final Product manager);

    ProductDTO updateBySku(final String sku, final Product product);

    void deleteBySku(final String sku);
}
