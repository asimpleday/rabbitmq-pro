package com.learn.demo5;

import com.learn.utils.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Demo5Provider {
    public static void main(String[] args) throws Exception {

        // 1. 建立和mq的连接
        Connection connection = ConnectionUtil.getConnection();
        // 2. 从连接中创建通道，channel   使用通道才能完成消息相关的操作
        Channel channel = connection.createChannel();
        //3.声明交换器和队列
        //3.声明交换器和队列
        String exchangeName = "exchange-topic";
        channel.exchangeDeclare(exchangeName,"topic",false); //交换机类型-direct
        //  创建2个队列
        //队列1
        String topicQueue1 = "topic_queue1";
        channel.queueDeclare(topicQueue1, false, false, false,null);
        //队列2
        String topicQueue2 = "topic_queue2";
        channel.queueDeclare(topicQueue2, false, false, false,null);

        //4.同一个交换机与2个队列绑定   参数3： 直连direct形式  定义路由规则   两个队列按照路由 接受对应的消息
        channel.queueBind(topicQueue1,exchangeName,"abc.#");//绑定第一个队列
        channel.queueBind(topicQueue2,exchangeName,"#.hello");//绑定第二个队列
        channel.queueBind(topicQueue2,exchangeName,"*.world");//绑定第二个队列
        //5.生产消息  向指定的队列投递消息
        for(int i=0;i<10;i++){
            channel.basicPublish(exchangeName,"abc.abc", true,null,"abc前缀的值!".getBytes());
            channel.basicPublish(exchangeName,"abc.demo.hello", true,null,"hello ".getBytes());
            channel.basicPublish(exchangeName,"update.world", true,null,"update_world!".getBytes());
        }

        //6.关闭channel和连接
        channel.close();
        //关闭连接
        connection.close();
    }

}
