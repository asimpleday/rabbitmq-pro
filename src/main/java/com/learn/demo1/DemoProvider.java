package com.learn.demo1;



import com.learn.utils.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * TODO:简单模式-生产者
 */
public class DemoProvider {
    public static void main(String[] args) throws Exception {
        /*// TODO:1.创建连接工厂 用于生产连接的
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // TODO:2.设置相关参数
        connectionFactory.setUsername("xulinjun");
        connectionFactory.setPassword("123456");
        connectionFactory.setHost("192.168.206.99");
        connectionFactory.setPort(5672);
        // 设置连接的虚拟机名称(虚拟机需要在浏览器客户端上创建)
        connectionFactory.setVirtualHost("/myhost");
        // TODO:3.获取连接对象
        Connection conn = connectionFactory.newConnection();*/



        Connection conn = ConnectionUtil.getConnection();
        // TODO:4.获取通道
        Channel channel = conn.createChannel();
        // TODO:5.使用通道创建队列
        /**
         queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         参数:
         参数1.queue 队列名称
         参数2.durable 是否持久化,当MQ重启后是否存在
         参数3.exclusive
         是否独占. 只能有一个消费者监听此队列
         参数4.autoDelete 是否自动删除, 没有consumer时自动删除
         参数5.arguments 其他参数
         */
        // 如果没有名称叫做hello_world的对象则创建,如果有则不创建
        channel.queueDeclare("hello_world", true, false, false, null);
        // TODO:6. 定义发送到mq的消息内容

        /**
         * basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
         * 参数1: exchange 交换机名称,简单模式下使用默认的 写""即可
         * 参数2: routingKey 路由名称
         *      当前没有交换机,我们直接指定队列
         * 参数3: props 配置信息
         * 参数4: body 发送的消息数据
         */
        for (int i = 0; i < 10000; i++) {
            String message = "Hello RabbitMQ! " + i;
            channel.basicPublish("", "hello_world", null, message.getBytes());
        }
//        channel.basicPublish("", "hello_world", null, message.getBytes());
        System.out.println("发布消息成功...");
        //TODO:7.关闭通道和连接
        channel.close();
        conn.close();
    }
}
