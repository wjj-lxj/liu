package com.wjj.service.impl;

import java.util.Date;
import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import com.wjj.mapper.BgmMapper;
import com.wjj.mapper.CommentsMapper;
import com.wjj.mapper.CommentsMapperCustom;
import com.wjj.mapper.SearchRecordsMapper;
import com.wjj.mapper.UsersLikeVideosMapper;
import com.wjj.mapper.UsersMapper;
import com.wjj.mapper.VideosMapper;
import com.wjj.mapper.VideosMapperCustom;
import com.wjj.pojo.Bgm;
import com.wjj.pojo.Comments;
import com.wjj.pojo.SearchRecords;
import com.wjj.pojo.Users;
import com.wjj.pojo.UsersLikeVideos;
import com.wjj.pojo.Videos;
import com.wjj.pojo.vo.CommentsVO;
import com.wjj.pojo.vo.VideosVo;
import com.wjj.service.BgmService;
import com.wjj.service.UserService;
import com.wjj.service.VideoService;
import com.wjj.utils.PagedResult;
import com.wjj.utils.TimeAgoUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class VideoServiceImpl implements VideoService {
	
	@Autowired //注入mapper并且自动为mapper赋值
	private VideosMapper videoMapper;
	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private CommentsMapper commentMapper;
	@Autowired
	private CommentsMapperCustom commentMapperCustom;
	@Autowired
	private VideosMapperCustom videoMapperCustom;
	@Autowired
	private SearchRecordsMapper searchRecordsMapper;
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;
	@Autowired
	private Sid sid;
	
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public String saveVideo(Videos video) {
		String id=sid.nextShort();
		video.setId(id);
		videoMapper.insertSelective(video);
		return id;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void updateVideo(String videoId, String coverPath) {
		
		Videos video=new Videos();
		video.setId(videoId);
		video.setCoverPath(coverPath);
		videoMapper.updateByPrimaryKeySelective(video);
		
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public PagedResult getAllVideos(Videos video,Integer isSaveRecord,
			Integer page, Integer pageSize) {
		//保存热搜词
		String desc=video.getVideoDesc();
		String userId=video.getUserId();
		if(isSaveRecord !=null && isSaveRecord==1) {
			SearchRecords record=new SearchRecords();
			String recordId=sid.nextShort();
			record.setId(recordId);
			record.setContent(desc);
			searchRecordsMapper.insert(record);
		}
		
		PageHelper.startPage(page,pageSize);
		List<VideosVo> list=videoMapperCustom.queryAllVideos(desc,userId);
		
		PageInfo<VideosVo> pageList=new PageInfo<>(list);
		
		PagedResult pagedResult=new PagedResult();
		pagedResult.setPage(page);
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public List<String> getHotwords() {
		// TODO Auto-generated method stub
		return searchRecordsMapper.getHotwords();
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
		//1保存用户和视频的喜欢点赞关联关系表
		String likeId=sid.nextShort();
		UsersLikeVideos ulv=new UsersLikeVideos();
		ulv.setId(likeId);
		ulv.setUserId(userId);
		ulv.setVideoId(videoId);
		usersLikeVideosMapper.insert(ulv);
		
		//2视频喜欢数量增加
		videoMapperCustom.addVideoLikeCount(videoId);
		
		//3 用户受喜欢数量增加
		usersMapper.addReceiveLikeCount(userId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	
	public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {	
		//1保存用户和视频的不喜欢取消点赞关联关系表
		System.out.println("88888");
		Example example=new Example(UsersLikeVideos.class);
		
		Criteria criteria=example.createCriteria();
		criteria.andEqualTo("userId",userId);
		criteria.andNotEqualTo("videoId", videoId);
		int n=	usersLikeVideosMapper.deleteByExample(example);
				
				//2视频喜欢数量累减
				videoMapperCustom.reduceVideoLikeCount(videoId);
				
				//3 用户受喜欢数量累减
				usersMapper.reduceReceiveLikeCount(userId);
				System.out.println("n的值为："+n);
				System.out.println("66666");
	}
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVo> list = videoMapperCustom.queryMyLikeVideos(userId);
				
		PageInfo<VideosVo> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVo> list = videoMapperCustom.queryMyFollowVideos(userId);
				
		PageInfo<VideosVo> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveComment(Comments comment) {
		String id = sid.nextShort();
		comment.setId(id);
		comment.setCreateTime(new Date());
		commentMapper.insert(comment);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
		
		PageHelper.startPage(page, pageSize);
		
		List<CommentsVO> list = commentMapperCustom.queryComments(videoId);
		
			for (CommentsVO c : list) {
				String timeAgo = TimeAgoUtils.format(c.getCreateTime());
				c.setTimeAgoStr(timeAgo);
			}
		
		PageInfo<CommentsVO> pageList = new PageInfo<>(list);
		
		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());
		
		return grid;
	}

	
}
