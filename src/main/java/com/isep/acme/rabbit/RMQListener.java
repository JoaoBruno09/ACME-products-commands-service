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
        if(product != null && repository.findBySku(product.getSku()).isEmpty()){
            switch(action) {
                case Constants.CREATED_PRODUCT_HEADER:
                   repository.save(product);
                    break;
                case Constants.UPDATED_PRODUCT_HEADER:
                    repository.save(product);
                    break;
                case Constants.DELETED_PRODUCT_HEADER:
                    repository.deleteBySku(product.getSku());
                    break;
                default:
                    break;
            }
        }
    }
}