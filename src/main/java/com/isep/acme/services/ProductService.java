package com.isep.acme.services;

import com.isep.acme.model.Product;
import com.isep.acme.model.dtos.ProductDTO;
import org.springframework.amqp.core.MessagePostProcessor;

public interface ProductService {

    ProductDTO create(final Product manager);

    ProductDTO updateBySku(final String sku, final Product product);

    void deleteBySku(final String sku);

    MessagePostProcessor createMessageProcessor(String header);
}
