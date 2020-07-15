package com.wjj.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import com.wjj.mapper.UsersFansMapper;
import com.wjj.mapper.UsersLikeVideosMapper;
import com.wjj.mapper.UsersMapper;
import com.wjj.mapper.UsersReportMapper;
import com.wjj.pojo.Users;
import com.wjj.pojo.UsersFans;
import com.wjj.pojo.UsersLikeVideos;
import com.wjj.pojo.UsersReport;
import com.wjj.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired //注入mapper并且自动为mapper赋值
	private UsersMapper userMapper;
	@Autowired
	private UsersFansMapper usersFansMapper;

	@Autowired
	private UsersReportMapper usersReportMapper;
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;
	@Autowired
	private Sid sid;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public boolean queryUsernameIsExist(String username) {
		
		Users user=new Users();
		user.setUsername(username);
		Users result=userMapper.selectOne(user);
		return result==null?false:true;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void saveuser(Users user) {
		
		String userId=sid.nextShort();
		user.setId(userId);
		userMapper.insert(user);

	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public Users queryUserForLogin(String username, String password) {
		// TODO Auto-generated method stub
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("username", username);
		criteria.andEqualTo("password", password);
		Users result = userMapper.selectOneByExample(userExample);
		
		return result;
	}
	@Override
	public void updateUserInfo(Users user) {
		// TODO Auto-generated method stub
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",user.getId());
		userMapper.updateByExampleSelective(user, userExample);
		
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Users queryUserInfo(String userId) {
		// TODO Auto-generated method stub
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",userId);
		Users user=userMapper.selectOneByExample(userExample);
		return user;
	}
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserLikeVideo(String userId, String videoId) {

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
			return false;
		}
		
		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("videoId", videoId);
		
		List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);
		
		if (list != null && list.size() >0) {
			return true;
		}
		
		return false;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void saveUserFanRelation(String userId, String fanId) {
		System.out.println("8888");
		String relId = sid.nextShort();
		
		UsersFans userFan = new UsersFans();
		userFan.setId(relId);
		userFan.setUserId(userId);
		userFan.setFanId(fanId);
		int n=	usersFansMapper.insert(userFan);
		System.out.println("n的值为："+n);
		
		userMapper.addFansCount(userId);
		userMapper.addFollersCount(fanId);
		System.out.println("666");
		
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void deleteUserFanRelation(String userId, String fanId) {
		// TODO Auto-generated method stub
		

		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);
		
		int m=usersFansMapper.deleteByExample(example);
		System.out.println("m的值为："+m);
		
		userMapper.reduceFansCount(userId);
		userMapper.reduceFollersCount(fanId);
		
	}
	@Override
	public boolean queryIfFollow(String userId, String fanId) {
		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);
		
		List<UsersFans> list = usersFansMapper.selectByExample(example);
		
		if (list != null && !list.isEmpty() && list.size() > 0) {
			return true;
		}
		
		return false;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void reportUser(UsersReport userReport) {
		String urId = sid.nextShort();
		userReport.setId(urId);
		userReport.setCreateDate(new Date());
		
		usersReportMapper.insert(userReport);
		
	}
	
	

}
