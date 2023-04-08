package com.isep.acme.rabbit;

import com.isep.acme.model.Product;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RMQListener {

    @RabbitListener(queuesToDeclare = @Queue(name = "${env.message-queue}"))
    public void listener(Product product){
        System.out.println("Product" + product);
        System.out.println("Product SKU" + product.getSku());
    }
}