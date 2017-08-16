package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${item.expire}")
	private Integer itemExpirt;
	@Override
	public TbItem getItemById(long itemId) {
		//添加缓存
		try {
			String string = jedisClient.get("item-info:"+itemId+":base");
			if(StringUtils.isNotBlank(string)){
				return JsonUtils.jsonToPojo(string, TbItem.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		try {
			//把結果添加到緩存
			jedisClient.set("item-info:"+itemId+":base", JsonUtils.objectToJson(item));
			//设置缓存的过期时间
			jedisClient.expire("item-info:"+itemId+":base", itemExpirt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	@Override
	public DataGridResult getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		DataGridResult dataGridResult = new DataGridResult();
		dataGridResult.setTotal(total);
		dataGridResult.setRows(list);
		return dataGridResult;
	}
	@Override
	public E3Result addItem(TbItem item,String desc) {
		final long id = IDUtils.genItemId();
		item.setId(id);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(id);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		tbItemDescMapper.insert(tbItemDesc);
		//发送消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				//消息内容是商品id
				TextMessage textMessage = session.createTextMessage(id+"");
				return textMessage;
			}
		});
		
		return E3Result.ok();
	}
	@Override
	public TbItemDesc getTbItemDescById(long itemId) {
		//添加缓存
		try {
			String string = jedisClient.get("item-info:"+itemId+":desc");
			if(StringUtils.isNotBlank(string)){
				return JsonUtils.jsonToPojo(string, TbItemDesc.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		try {
			//把結果添加到緩存
			jedisClient.set("item-info:"+itemId+":desc", JsonUtils.objectToJson(tbItemDesc));
			//设置缓存的过期时间
			jedisClient.expire("item-info:"+itemId+":desc", itemExpirt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
