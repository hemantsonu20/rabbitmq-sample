package com.github.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class CommonUtils {

    public static void close(Connection connection) {

        try {
            if (null != connection) {
                connection.close();
            }
        }
        catch (IOException e) {
        }
    }

    public static void close(Channel channel) {

        try {
            if (null != channel) {
                channel.close();
            }
        }
        catch (Exception e) {
        }
    }
}
