package cn.e3mall.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ActivemqConsumer {
	@Test
	public void testMessageListener() throws Exception{
		//1.初始化一个spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//2.阻塞代码
		System.in.read();
	}
}
