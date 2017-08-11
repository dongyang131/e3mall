package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;
@Service
public class ContentCatServiceImpl implements ContentCatService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	@Override
	public List<TreeNode> getContentCatList(long parentId) {
		TbContentCategoryExample e = new TbContentCategoryExample();
		Criteria criteria = e.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(e);
		List<TreeNode> treeNodeList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(tbContentCategory.getId());
			treeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
			treeNode.setText(tbContentCategory.getName());
			//添加到结果列表
			treeNodeList.add(treeNode);
		}
		return treeNodeList;
	}
	@Override
	public E3Result addContentCategory(long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		tbContentCategoryMapper.insert(contentCategory);
		TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		return E3Result.ok(contentCategory);
	}

}
