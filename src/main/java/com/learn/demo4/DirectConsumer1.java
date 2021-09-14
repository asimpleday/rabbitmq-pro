package com.learn.demo4;
import com.learn.utils.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
public class DirectConsumer1 {
    public static void main(String[] args) throws Exception {

        // 1. 建立和mq的连接
        Connection connection = ConnectionUtil.getConnection();
        // 2. 从连接中创建通道，channel   使用通道才能完成消息相关的操作
        final Channel channel = connection.createChannel();

        // 3. 队列名称
        String queueName2 = "direct_queue2";
        // 绑定队列到交换机
        channel.queueBind(queueName2, "exchange-direct", "delete");
        channel.queueBind(queueName2, "exchange-direct", "update");

        //  4. 定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                try {
                    // body 即消息体
                    String msg = new String(body);
//                    System.out.println(1/0); // 模拟异常  表示消息未正常处理
                    System.out.println(" direct-consumer 2 : [x] received : " + msg + "!");
                    channel.basicAck(envelope.getDeliveryTag(),false);//  代码没有异常 手动通知队列 删除消息即可
                } catch (Exception e){
                    e.printStackTrace();  //  第三个参数  false  直接删除消息   true  表示 ：把消息重回队列
                    channel.basicNack(envelope.getDeliveryTag(),false,true);
                }
            }
        };
        // 5. 监听队列，第二个参数：是否自动进行消息确认。 false 告诉队列不要删除消息
        channel.basicConsume(queueName2, false, consumer);

    }
}
