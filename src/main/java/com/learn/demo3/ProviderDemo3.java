package com.learn.demo3;

import com.learn.utils.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * TODO: 订阅模式-生产者
 */
public class ProviderDemo3 {
    public static void main(String[] args) throws Exception {
        //1.获取连接对象
        Connection conn = ConnectionUtil.getConnection();
        //2.创建通道
        Channel channel = conn.createChannel();
        //3.绑定交换机
        String exchangeName = "exchange-fanout";
        channel.exchangeDeclare(exchangeName,"fanout");
        //4.创建队列
        String fanout_queue1 = "fanout_queue1";
        channel.queueDeclare(fanout_queue1,true,false,false,null);

        String fanout_queue2 = "fanout_queue2";
        channel.queueDeclare(fanout_queue2,true,false,false,null);
        //5.绑定到交换机上
        channel.queueBind(fanout_queue1,exchangeName,"");
        channel.queueBind(fanout_queue2,exchangeName,"");

        //6.生产消息
        for (int i = 1; i <= 1000; i++) {
            String msg = "fanout-hello-rabbitMQ! "+i;
            channel.basicPublish(exchangeName,"", true,null,msg.getBytes());
        }
        System.out.println("生产消息成功...");
        //5.关闭通道和连接
        channel.close();
        conn.close();
    }
}
