package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;
	@Value("${search.each.count}")
	private Integer searchRows;
	
	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue="1")int page,Model model) throws Exception{
		//int i = 1/0;
		//1.接收页面提交的参数
		//2.调用Servie的查询结果
		if(keyword != null && !"".equals(keyword)){
			keyword = new String (keyword.getBytes("iso8859-1"),"utf-8");
		}
		SearchResult searchResult = searchService.search(keyword, page, searchRows);
		//3.把查询的结果传递给jsp
		model.addAttribute("query", keyword);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("recourdCount", searchResult.getRecourdCount());
		model.addAttribute("itemList", searchResult.getItemList());
		//4.返回逻辑视图
		return "search";
	}
}
