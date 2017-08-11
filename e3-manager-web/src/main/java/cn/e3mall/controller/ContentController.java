package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	@Autowired
	private ContentService contentService; 

	@RequestMapping("/content/query/list")
	@ResponseBody
	public DataGridResult getContentList(Long categoryId,Integer page,Integer rows){
		DataGridResult dataGridResult = contentService.getContentList(categoryId, page, rows);
		return dataGridResult;
	}
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent content){
		E3Result e3Result = contentService.addContent(content);
		return e3Result;
	}
}
