package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class GenHtmlController {
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;
	@RequestMapping("/gen/html")
	@ResponseBody
	public String genHtml() throws Exception{
		Configuration configuration = freeMarkerConfig.getConfiguration();
		Template template = configuration.getTemplate("hello.ftl");
		Map map = new HashMap<>();
		map.put("hello","freemark整合springnvc测试!");
		Writer out = new FileWriter("C:/Users/Administrator/Desktop/新建文件夹/hello.html");
		template.process(map, out);
		out.close();
		return "ok";
	}
}
