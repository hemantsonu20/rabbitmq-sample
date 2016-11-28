package com.github.rabbitmq.queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.github.rabbitmq.CommonUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class QueueReceiver implements AutoCloseable {

    private Connection connection;
    private Channel channel;

    public void run() {

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        try {
            initConnection();

            receive();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void receive() throws IOException {

        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {

                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };

        while (true) {
            channel.basicConsume(CommonConstants.QUEUE_NAME, true, consumer);
        }
    }

    private void initConnection() throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(CommonConstants.HOST_NAME);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(CommonConstants.QUEUE_NAME, false, false, false, null);
    }

    @Override
    public void close() {

        CommonUtils.close(channel);
        CommonUtils.close(connection);
        System.out.println("receiver connection closed");
    }

    public static void main(String[] args) {

        try (QueueReceiver receiver = new QueueReceiver()) {
            receiver.run();
        }
    }
}
