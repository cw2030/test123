package com.wzp.test;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ActualConsumer extends DefaultConsumer {

	public ActualConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		// TODO Auto-generated method stub
		super.handleDelivery(consumerTag, envelope, properties, body);
	}

}
