package eu.mateuszpiela.learn.amqp.producer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class App 
{
    private final static String QUEUE_NAME = "helloworld";
    private final static String message = "Hello World from Java to Java through AMQP";


    public static void main( String[] args ) throws Exception
    {
        System.out.println("Starting the AMQP Producer ..........");
        String uri = (System.getenv("AMQP_URL") == null) ? "amqp://guest:guest@localhost" : System.getenv("AMQP_URL");
        Integer sleep = (System.getenv("AMQP_PRODUCER_SLEEP") == null) ? 5 : Integer.parseInt(System.getenv("AMQP_PRODUCER_SLEEP"));


        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        factory.setConnectionTimeout(600);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println("Declaring the queue: " + QUEUE_NAME);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Closing channel and connection ...");
                    channel.close();
                    connection.close();
                } catch (IOException | AlreadyClosedException |TimeoutException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }));

        while(true){
            System.out.println("Sending message .....");
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            
            TimeUnit.SECONDS.sleep(sleep);
        }
    }
}
