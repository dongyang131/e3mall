package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId,Model model){
		//调用服务查询商品信息
		TbItem tbItem = itemService.getItemById(itemId);
		//把TbItem对象转成Item对象
		Item item = new Item(tbItem);
		//查询商品描述
		TbItemDesc tbItemDesc = itemService.getTbItemDescById(itemId);
		//把结果传递给jsp
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);
		//返回逻辑视图
		return "item";
	}
}
