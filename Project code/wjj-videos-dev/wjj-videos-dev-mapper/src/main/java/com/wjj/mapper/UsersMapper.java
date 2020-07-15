package com.wjj.mapper;

import com.wjj.pojo.Users;
import com.wjj.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {
	
	/**
	 * 用户受喜欢数累加
	 * @param userId
	 */
	public void addReceiveLikeCount(String userId);
	/**
	 * 用户受喜欢数减少
	 * @param userId
	 */
	public void reduceReceiveLikeCount(String userId);
	
	/**
	 * @Description: 增加粉丝数
	 */
	public void addFansCount(String userId);
	
	/**
	 * @Description: 增加关注数
	 */
	public void addFollersCount(String userId);
	
	/**
	 * @Description: 减少粉丝数
	 */
	public void reduceFansCount(String userId);
	
	/**
	 * @Description: 减少关注数
	 */
	public void reduceFollersCount(String userId);
}