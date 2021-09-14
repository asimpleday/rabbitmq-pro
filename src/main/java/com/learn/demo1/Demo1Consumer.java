package com.learn.demo1;

import com.learn.utils.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * TODO:简单模式-消费者
 */
public class Demo1Consumer {
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
        //TODO:5.找到队列(订阅队列)
        /**
         * 参数1: 队列名称
         * 参数2: 是否持久化
         * 参数3: 是否独有
         * 参数4: 是否自动删除
         * 参数5: 其他参数
         */
        channel.queueDeclare("hello_world", true, false, false, null);
        // 设置每个消费者同时只能处理一条消息
        channel.basicQos(1);
        // TODO:6.创建消费者对象
        // 当目标队列中有数据时,调用此消费者进行消费
        Consumer consumer = new DefaultConsumer(channel){
            /**
             * 回调交付
             * @param consumerTag 标记
             * @param envelope 获取一些信息,交换机,路由
             * @param properties 配置信息
             * @param body 从队列中获取的数据
             * @throws IOException
             */
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    //System.out.println(1/0);
                    // 手动确认: 交付成功,确认交付
                    // 删除当前的数据  false : 单个删除
                    System.out.println("数据: "+ new String(body));
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    Thread.sleep(1000);
                    System.out.println("============");
                } catch (Exception e) {
                    // 如果程序出现了异常,将取出的归还
                    // 参数1: 确认字符串  参数2: 是否批量处理 参数3: 当前消费失败时,将消息重新存放到队列中
                    channel.basicNack(envelope.getDeliveryTag(),false,true);
                    e.printStackTrace();

                }
            }
        };
        // TODO:7.消费(监听)
        // 参数1: 队列的名称
        // 参数2: 是否自动确认(一旦自动确认,该数据就会从队列中移除)
        channel.basicConsume("hello_world",false,consumer);
        System.out.println("=========消费者2=========");
        // 阻塞线程
        System.in.read();
    }
}
