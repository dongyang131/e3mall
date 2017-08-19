package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.UserService;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private TbUserMapper UserMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${session.timeout}")
	private Integer timeout;

	@Override
	public E3Result checkData(String data, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1.判断type,根据不同的数据类型创建不同的查询条件
		//1.用户名    2.手机号   3.邮箱
		if(type == 1){
			criteria.andUsernameEqualTo(data);
		}else if(type == 2){
			criteria.andPhoneEqualTo(data);
		}else if(type == 3){
			criteria.andEmailEqualTo(data);
		}else{
			return E3Result.build(400, "非法的数据类型");
		}
		List<TbUser> list = UserMapper.selectByExample(example);
		if(list == null || list.size()==0){
			return E3Result.ok(true);
		}
		return E3Result.ok(false);
	}

	@Override
	public E3Result register(TbUser user) {
		if(StringUtils.isBlank(user.getUsername())){
			return E3Result.build(400, "用户名不能为空");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return E3Result.build(400, "密码不能为空");
		}
		//1.校验用户名是否可用
		E3Result e3Result = checkData(user.getUsername(), 1);
		if(!(boolean) e3Result.getData()){
			return E3Result.build(400, "用户名已经存在");
		}
		//2.校验密码是否可用
		if(StringUtils.isNotBlank(user.getPhone())){
			e3Result = checkData(user.getPhone(), 2);
			if(!(boolean) e3Result.getData()){
				return E3Result.build(400, "手机号已经存在");
			}
		}
		//3.校验邮箱是否可用
		if(StringUtils.isNotBlank(user.getEmail())){
			e3Result = checkData(user.getEmail(), 3);
			if(!(boolean) e3Result.getData()){
				return E3Result.build(400, "邮箱已经存在");
			}
		}
		//4.对密码进行MD5加密
		String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Password);
		//5.补全pojo类的属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//6.插入到数据库
		UserMapper.insert(user);
		//7.返回成功
		return e3Result.ok();
	}

	@Override
	public E3Result login(String username, String password) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = UserMapper.selectByExample(example);
		if(list == null || list.size() == 0){
			return E3Result.build(400, "用户名或者密码错误");
		}
		TbUser user = list.get(0);
		if(!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
			return E3Result.build(400, "用户名或者密码错误");
		}
		String token = UUID.randomUUID().toString();
		user.setPassword(null);
		jedisClient.hset("session:"+token, "user", JsonUtils.objectToJson(user));
		jedisClient.expire("session:"+token, timeout);
		return E3Result.ok(token);
	}

	@Override
	public E3Result getUserByToken(String token) {
		String json = jedisClient.hget("session:"+token, "user");
		if(StringUtils.isBlank(json)){
			return E3Result.build(400, "用户登录过期了");
		}
		TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
		jedisClient.expire("session:"+token, timeout);
		return E3Result.ok(tbUser);
	}

}
