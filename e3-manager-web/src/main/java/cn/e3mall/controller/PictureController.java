package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

@Controller
public class PictureController {
	@Value("${picture.service.url}")
	private String pictureServiceUrl;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		try {
			//获取文件的原始路径
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			String pictureUrl = pictureServiceUrl + url;
			Map map = new HashMap<>();
			map.put("error", 0);
			map.put("url", pictureUrl);
			return JsonUtils.objectToJson(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map map = new HashMap<>();
			map.put("error", 1);
			map.put("message", "出错啦啦~");
			return JsonUtils.objectToJson(map);
		}
		
	}
}
