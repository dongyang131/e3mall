package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;

@Controller
public class SearchIndexController {
	@Autowired
	private SearchItemService  searchItemService;
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importIndex(){
		E3Result e3Result = searchItemService.importIndex();
		return e3Result;
	}
}
