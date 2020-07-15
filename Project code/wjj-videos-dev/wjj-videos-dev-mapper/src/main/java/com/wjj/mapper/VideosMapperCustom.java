package com.wjj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wjj.pojo.Videos;
import com.wjj.pojo.vo.VideosVo;
import com.wjj.utils.MyMapper;

public interface VideosMapperCustom extends MyMapper<Videos> {
	
	public List<VideosVo> queryAllVideos(@Param("videoDesc") String videoDesc,
										@Param("userId") String userId);
	
	/**
	 * 对视频喜欢的数量进行累加
	 * @param videoId
	 */
	public void  addVideoLikeCount(String videoId);
	/**
	 * 对视频喜欢的数量进行累减
	 * @param videoId
	 */
	public void  reduceVideoLikeCount(String videoId);
	/**
	 * @Description: 查询关注的视频
	 */
	public List<VideosVo> queryMyFollowVideos(String userId);
	
	/**
	 * @Description: 查询点赞视频
	 */
	public List<VideosVo> queryMyLikeVideos(@Param("userId") String userId);
	
	
}