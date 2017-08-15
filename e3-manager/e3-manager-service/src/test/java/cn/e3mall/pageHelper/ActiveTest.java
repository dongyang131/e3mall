package cn.e3mall.pageHelper;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class ActiveTest {

	@Test
	public void testQueue() throws Exception{
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.166:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("test-dd");
		MessageProducer producer = session.createProducer(queue);
		TextMessage message = new ActiveMQTextMessage();
		message.setText("helloaaaaaaaaaaaaaaaa");
		producer.send(message);
		producer.close();
		session.close();
		connection.close();
	}
	@Test
	public void testQueueConsumer() throws Exception{
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.166:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("test-dd");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}
}
