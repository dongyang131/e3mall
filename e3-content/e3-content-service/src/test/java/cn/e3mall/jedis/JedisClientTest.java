package cn.e3mall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.ApplicationConfig;

import cn.e3mall.common.jedis.JedisClient;

public class JedisClientTest {
	@Test
	public void testJedisClient(){
		//1.初始化spring容器
		ApplicationContext applicationConfig = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//2.从容器中获得JedisClient对象
		JedisClient jedisClient = applicationConfig.getBean(JedisClient.class);
		//3.使用JedisClient查询对象
		String string = jedisClient.get("mytest");
		//4.打印结果
		System.out.println(string);
	}
	
	
}
