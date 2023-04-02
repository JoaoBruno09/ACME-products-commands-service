package com.isep.acme.services.impl;

import com.isep.acme.rabbit.RMQConfig;
import com.isep.acme.model.Product;
import com.isep.acme.model.dtos.ProductDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.services.ProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ProductDTO create(final Product product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());
        rabbitTemplate.convertAndSend(RMQConfig.EXCHANGE, "", p);
        System.out.println("Product" + p);
        System.out.println("Product SKU" + product.getSku());
        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {
        
        final Optional<Product> productToUpdate = repository.findBySku(sku);

        if( productToUpdate.isEmpty() ) return null;

        productToUpdate.get().updateProduct(product);

        Product productUpdated = repository.save(productToUpdate.get());
        
        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {
        repository.deleteBySku(sku);
    }
}
