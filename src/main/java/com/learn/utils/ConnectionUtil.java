package com.learn.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工具类封装
 */
public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
        //TODO:1.创建连接工厂对象
        ConnectionFactory factory = new ConnectionFactory();
        //TODO:2.设置参数
        factory.setHost("192.168.206.99");
        factory.setPort(5672);
        factory.setVirtualHost("/myhost");
        factory.setUsername("xulinjun");
        factory.setPassword("123456");
        //TODO:3.获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
