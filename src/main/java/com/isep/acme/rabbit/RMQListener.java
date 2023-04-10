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

    @RabbitListener(queues = "#{queue.name}")
    public void listener(Message message){
        final String action= message.getMessageProperties().getHeader("action");
        if (action.equals(Constants.CREATED_PRODUCT_HEADER) || action.equals(Constants.UPDATED_PRODUCT_HEADER) || action.equals(Constants.DELETED_PRODUCT_HEADER)){
            final Product product = (Product) messageConverter.fromMessage(message);
            System.out.println("Received Product Message " + product);
            final Optional<Product> productToAction = repository.findBySku(product.getSku());
            if(product != null){
                switch(action) {
                    case Constants.CREATED_PRODUCT_HEADER:
                        if(productToAction.isEmpty()){
                            repository.save(product);
                            System.out.println("Product Added " + product);
                        }
                        break;
                    case Constants.UPDATED_PRODUCT_HEADER:
                        if(!productToAction.isEmpty()){
                            productToAction.get().updateProduct(product);
                            repository.save(productToAction.get());
                            System.out.println("Product Updated " + productToAction.get());
                        }
                        break;
                    case Constants.DELETED_PRODUCT_HEADER:
                        if(!productToAction.isEmpty()){
                            repository.deleteBySku(product.getSku());
                            System.out.println("Product Deleted " + productToAction.get());
                        }
                        break;
                    default:
                        break;
                }
        }


        }
    }
}