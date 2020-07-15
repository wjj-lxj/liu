package com.wjj.service;


import java.util.List;

import com.wjj.pojo.Bgm;
import com.wjj.pojo.Comments;
import com.wjj.pojo.Users;
import com.wjj.pojo.Videos;
import com.wjj.utils.PagedResult;

public interface VideoService {
	
	/**
	 * 保存视频
	 * @param bgmId
	 * @return
	 */
	public String saveVideo(Videos video);
	
	/**
	 * 修改视频封面
	 * @param videoId
	 * @param coverPath
	 * @return
	 */
	public void updateVideo(String videoId,String coverPath);
	
	/**
	 * 分页查询视频列表
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public PagedResult getAllVideos(Videos video,Integer isSaveRecord,
			Integer page,Integer pageSize);
	/**
	 * 获取热搜词列表
	 * @return
	 */
	public List<String> getHotwords();
	
	/**
	 * 用户喜欢视频/点赞
	 * @param userId
	 * @param videoId
	 * @param videoCreaterId
	 */
	public void userLikeVideo(String userId,String videoId,String videoCreaterId);
	/**
	 * 用户不喜欢视频/取消点赞
	 * @param userId
	 * @param videoId
	 * @param videoCreaterId
	 */
	public void userUnLikeVideo(String userId,String videoId,String videoCreaterId);
	

	/**
	 * @Description: 查询我喜欢的视频列表
	 */
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);
	
	/**
	 * @Description: 查询我关注的人的视频列表
	 */
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);
	
	/**
	 * @Description: 用户留言
	 */
	public void saveComment(Comments comment);
	
	/**
	 * @Description: 留言分页
	 */
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
	
}
