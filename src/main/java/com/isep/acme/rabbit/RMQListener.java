package com.isep.acme.rabbit;

import com.isep.acme.constants.Constants;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class RMQListener {

    private final MessageConverter messageConverter;
    @Autowired
    private ProductRepository repository;

    @RabbitListener(queues = RMQConfig.PCQUEUE)
    public void listener(Message message){
        String action= message.getMessageProperties().getHeader("action");
        Product product = (Product) messageConverter.fromMessage(message);
        final Optional<Product> productExists = repository.findBySku(product.getSku());
        if(product != null){
            switch(action) {
                case Constants.CREATED_PRODUCT_HEADER:
                    if(!productExists.isPresent()){
                        repository.save(product);
                    }
                    break;
                case Constants.UPDATED_PRODUCT_HEADER:
                    if(!productExists.isEmpty()){
                        productExists.get().updateProduct(product);
                        repository.save(productExists.get());
                    }
                    break;
                case Constants.DELETED_PRODUCT_HEADER:
                    if(!productExists.isEmpty()){
                        repository.deleteBySku(product.getSku());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}