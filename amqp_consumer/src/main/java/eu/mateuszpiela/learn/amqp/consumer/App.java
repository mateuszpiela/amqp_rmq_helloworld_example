package eu.mateuszpiela.learn.amqp.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class App 
{
    private final static String QUEUE_NAME = "helloworld";

    public static void main( String[] args ) throws Exception
    {
        System.out.println("Starting the AMQP Consumer ..........");
        String uri = (System.getenv("AMQP_URL") == null) ? "amqp://guest:guest@localhost" : System.getenv("AMQP_URL");
        System.out.println(uri);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setConnectionTimeout(600);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println("Declaring the queue: " + QUEUE_NAME);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received message: " + message);
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
