package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	DataGridResult getContentList(Long categoryId,int page,int rows);
	E3Result addContent(TbContent content );
	List<TbContent> getContentListByCid(long categoryId);
}
