package com.isep.acme.services.impl;

import com.isep.acme.constants.Constants;
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

    @Autowired
    private ProductRepository repository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ProductDTO create(final Product product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());
        final Optional<Product> productToAdd = repository.findBySku(product.getSku());
        if(productToAdd.isEmpty()){
            rabbitTemplate.convertAndSend(Constants.EXCHANGE, "", p, createMessageProcessor(Constants.CREATED_PRODUCT_HEADER));
            return repository.save(p).toDto();
        }
        return null;
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {
        final Optional<Product> productToUpdate = repository.findBySku(sku);
        if(!productToUpdate.isEmpty()){
            productToUpdate.get().updateProduct(product);
            Product productUpdated = repository.save(productToUpdate.get());
            rabbitTemplate.convertAndSend(Constants.EXCHANGE, "", productUpdated, createMessageProcessor(Constants.UPDATED_PRODUCT_HEADER));
            return productUpdated.toDto();
        }
        return null;
    }

    @Override
    public void deleteBySku(String sku) {
        final Optional<Product> productToDelete = repository.findBySku(sku);
        if(!productToDelete.isEmpty()){
            rabbitTemplate.convertAndSend(Constants.EXCHANGE, "", productToDelete.get(), createMessageProcessor(Constants.DELETED_PRODUCT_HEADER));
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
