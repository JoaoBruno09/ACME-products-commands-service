package com.isep.acme.rabbit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.ProductRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class RpcClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductRepository productRepository;
    public RpcClient(){

    }
    public void getProducts() throws Exception {
        String correlationId = UUID.randomUUID().toString();

        MessageProperties props = new MessageProperties();
        props.setReplyTo("rpc_callback_queue");
        props.setCorrelationId(correlationId);

        String message = "get_products";
        Message requestMessage = new Message(message.getBytes(), props);
        ArrayList<Product> productList = (ArrayList<Product>) rabbitTemplate.convertSendAndReceive("rpc_products_queue", requestMessage);


        System.out.println(productList);

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < productList.size(); i++) {
            System.out.println(productList.get(i));
            Product product = objectMapper.convertValue(productList.get(i), Product.class);
            System.out.println("ProductSKU " + product.getSku());
            productRepository.save(product);
            System.out.println("Product added to database");
        }
    }
}
