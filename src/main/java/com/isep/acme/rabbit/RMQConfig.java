package com.isep.acme.rabbit;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RMQConfig {

	@Value("${message-queue}")
	public static final String PCQUEUE = "";
	public static final String EXCHANGE = "fanout_exchange";

	@Bean
	public Queue queue() {
		return new Queue(PCQUEUE);
	}

	@Bean
	public FanoutExchange fanout() {
		return new FanoutExchange(EXCHANGE);
	}

	@Bean
	public Binding binding(FanoutExchange fanout, Queue queue) {
		return BindingBuilder.bind(queue).to(fanout);
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter(){
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory){
		RabbitTemplate template = new RabbitTemplate( connectionFactory);
		template.setMessageConverter(messageConverter());
		return template;
	}

}
