package com.example.demo;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import jakarta.jms.MessageConsumer;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;

import java.util.Date;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.text.SimpleDateFormat;

public class JmsClass {

    private static String utcNow(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime local = now.withZoneSameLocal(ZoneId.systemDefault());
        return formatter.format(Date.from(local.toInstant()));
    }

    public static void main(String[] args) throws Exception {
        SqsClient sqsClient = SqsClient.builder().region(Region.US_EAST_1).build();
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                sqsClient
        );

        new JmsClass().receive(connectionFactory);
        Logger.println("finished: " + utcNow());
    }
    public void receive(SQSConnectionFactory connectionFactory) throws Exception {
        Logger.println("start: " + utcNow());

        try (SQSConnection connection = connectionFactory.createConnection()) {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("Queue");
            MessageConsumer consumer = session.createConsumer(queue);
            connection.start();

            Message receivedMessage = consumer.receive(1000);

            // Cast the received message as TextMessage and display the text
            if (receivedMessage != null) {
                Logger.println("Received: " + utcNow() + " " + ((TextMessage) receivedMessage).getText());
            }
        }
    }
}
