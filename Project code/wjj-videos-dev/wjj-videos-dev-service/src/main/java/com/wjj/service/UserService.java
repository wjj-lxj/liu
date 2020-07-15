package com.wjj.service;

import com.wjj.pojo.Users;
import com.wjj.pojo.UsersReport;

public interface UserService {
	/**
	 * 判断用户名是否存在
	 * @param username
	 * @return
	 */
	public boolean queryUsernameIsExist(String username);
	/**
	 * 保存用户（注册用户）
	 * @param user
	 */
	public void saveuser(Users user);
	/**
	 * 用户登录，根据用户名和密码查询用户
	 * @param username
	 * @param password
	 * @return
	 */
	public Users queryUserForLogin(String username,String password);
	/**
	 * 修改用户信息
	 * @param user
	 */
	public void updateUserInfo(Users user);
	/**
	 * 查询用户信息
	 * @param userId
	 * @return
	 */
	public Users queryUserInfo(String userId);
	
	/**
	 * @Description: 查询用户是否喜欢点赞视频
	 */
	public boolean isUserLikeVideo(String userId, String videoId);
	
	/**
	 * @Description: 增加用户和粉丝的关系
	 */
	public void saveUserFanRelation(String userId, String fanId);
	
	/**
	 * @Description: 删除用户和粉丝的关系
	 */
	public void deleteUserFanRelation(String userId, String fanId);
	
	/**
	 * @Description: 查询用户是否关注
	 */
	public boolean queryIfFollow(String userId, String fanId);
	
	/**
	 * @Description: 举报用户
	 */
	public void reportUser(UsersReport userReport);
}