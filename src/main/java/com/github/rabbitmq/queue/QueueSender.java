package com.github.rabbitmq.queue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.github.rabbitmq.CommonUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class QueueSender implements Runnable, AutoCloseable {

    private Connection connection;
    private Channel channel;

    @Override
    public void run() {

        System.out.println(" [*] Enter messages to send. To exit press CTRL+C");
        try {
            initConnection();
            send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void send() throws UnsupportedEncodingException, IOException {

        try (Scanner sc = new Scanner(System.in)) {

            while (sc.hasNextLine()) {
                String message = sc.nextLine();
                channel.basicPublish("", CommonConstants.QUEUE_NAME, null, message.getBytes("UTF-8"));
                System.out.println("Message Sent: " + message);
            }
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
        System.out.println("sender connection closed");
    }

    public static void main(String[] args) {

        try (QueueSender sender = new QueueSender()) {
            sender.run();
        }
    }
}
