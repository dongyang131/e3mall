package cn.e3mall.freemark;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;


public class FreeMarkTest {
	@Test
	public void testFreeMark() throws Exception{
		//1.把freemark的jar包添加到工程中
		//2.创建模板文件
		//3.创建一个Configurartion对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//4.设置模板所在的路径
		configuration.setDirectoryForTemplateLoading(new File("E:/workspace/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		//5.模板的编码格式,通常是utf-8
		configuration.setDefaultEncoding("utf-8");
		//6.加载模板文件,生成模板对象
		Template template = configuration.getTemplate("student.ftl");
		List stuList = new ArrayList<>();
		//7.创建数据集对象可以使用pojo类,也可以使用map,通常使用map
		stuList.add(new Student("1","小花",3,"北京昌平"));
		stuList.add(new Student("2","小花1",1,"北京昌平"));
		stuList.add(new Student("3","小花2",2,"北京昌平"));
		stuList.add(new Student("4","小花3",4,"北京昌平"));
		stuList.add(new Student("5","小花4",5,"北京昌平"));
		Map map = new HashMap<>();
		map.put("hello", "hello freemark!!");
		map.put("student", new Student("1","小花",3,"北京昌平"));
		map.put("stuList", stuList);
		map.put("date", new Date());
		//8.创建一个writter对象,需要设置文件保存路径及文件名
		Writer out = new FileWriter("C:/Users/Administrator/Desktop/新建文件夹/student.html");
		//9.生成文件
		template.process(map, out);
		//10.关闭流
		out.close();
	}
}
