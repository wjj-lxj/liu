package com.wjj.mapper;

import java.util.List;

import com.wjj.pojo.Comments;
import com.wjj.pojo.vo.CommentsVO;
import com.wjj.utils.MyMapper;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}