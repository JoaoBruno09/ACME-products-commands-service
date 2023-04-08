package com.isep.acme.services.impl;

import com.isep.acme.rabbit.RMQConfig;
import com.isep.acme.model.Product;
import com.isep.acme.model.dtos.ProductDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.services.ProductService;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static String createdProductHeader = "product-created";
    private static String updatedProductHeader = "product-updated";
    private static String deletedProductHeader = "product-deleted";

    @Autowired
    private ProductRepository repository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ProductDTO create(final Product product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());
        rabbitTemplate.convertAndSend(RMQConfig.EXCHANGE, "", p, createMessageProcessor(createdProductHeader));
        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {
        final Optional<Product> productToUpdate = repository.findBySku(sku);
        if(!productToUpdate.isEmpty()){
            productToUpdate.get().updateProduct(product);
            Product productUpdated = repository.save(productToUpdate.get());
            rabbitTemplate.convertAndSend(RMQConfig.EXCHANGE, "", productUpdated, createMessageProcessor(updatedProductHeader));
            return productUpdated.toDto();
        }
        return null;
    }

    @Override
    public void deleteBySku(String sku) {
        final Optional<Product> productToDelete = repository.findBySku(sku);
        if(!productToDelete.isEmpty()){
            rabbitTemplate.convertAndSend(RMQConfig.EXCHANGE, "", productToDelete.get(), createMessageProcessor(deletedProductHeader));
            repository.deleteBySku(sku);
        }
    }

    @Override
    public MessagePostProcessor createMessageProcessor(String header) {
        return message -> {
            message.getMessageProperties().setHeader("action", header);
                return message;
        };
    }
}
