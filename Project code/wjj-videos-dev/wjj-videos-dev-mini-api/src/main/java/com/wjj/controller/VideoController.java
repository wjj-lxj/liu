package com.wjj.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wjj.enums.VideoStatusEnum;
import com.wjj.pojo.Bgm;
import com.wjj.pojo.Comments;
import com.wjj.pojo.Users;
import com.wjj.pojo.Videos;
import com.wjj.service.BgmService;
import com.wjj.service.VideoService;
import com.wjj.utils.FetchVideoCover;
import com.wjj.utils.IMoocJSONResult;
import com.wjj.utils.MergeVideoMp3;
import com.wjj.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value="视频相关业务的接口",tags= {"视频相关业务的controller"})

@RequestMapping("/video")
public class VideoController extends BasicController{
	
	@Autowired
	private BgmService bgmService;
	@Autowired
	private VideoService videoService;
	@ApiOperation(value="上传视频", notes="上传视频的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户id",required=true,
				dataType="String",paramType="form"),
		@ApiImplicitParam(name="bgmId",value="背景音乐id",required=false,
				dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoSeconds",value="背景音乐播放长度",required=true,
				dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoWidth",value="视频宽度",required=true,
				dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoHeight",value="视频高度",required=true,
				dataType="String",paramType="form"),
		@ApiImplicitParam(name="desc",value="视频描述",required=false,
				dataType="String",paramType="form"),
	})
	
	@PostMapping(value="/upload",headers="content-type=multipart/form-data")
	public IMoocJSONResult upload(String userId,
			String bgmId,double videoSeconds,
			int videoWidth,int videoHeight,
			String desc,
			@ApiParam(value="短视频",required=true)
			MultipartFile file) throws Exception {
		
		if(StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("用户id不能为空");
		}
		
		//文件保存的命名空间
	//	String fileSpace="F:/wjj-video";
		//保存到数据库中的相对路径
		String uploadPathDB="/" + userId +"/video";
		String coverPathDB="/" + userId +"/video";
		
		FileOutputStream fileOutputStream=null;
		InputStream inputStream=null;
		String finalVideoPath="";
		try {
			if(file !=null) {
				String fileName=file.getOriginalFilename();
				
				String arrayFilenameItem[] =  fileName.split("\\.");
				String fileNamePrefix = "";
				for (int i = 0 ; i < arrayFilenameItem.length-1 ; i ++) {
					fileNamePrefix += arrayFilenameItem[i];
				}
				//String fileNamePrefix=fileName.split("\\.")[0];
				if(StringUtils.isNotBlank(fileName)) {
					//文件上传的最终保存路径
					 finalVideoPath=FILE_SPACE + uploadPathDB +"/" +fileName;
					//设置数据库保存路径、
					uploadPathDB+=("/" +fileName);
					coverPathDB=coverPathDB+ "/" +fileNamePrefix+".jpg";
					File outFile=new File(finalVideoPath);
					if(outFile.getParentFile() !=null || 
							!outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream =new FileOutputStream(outFile);
					inputStream=file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
					
				}
			}else {
				return IMoocJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return IMoocJSONResult.errorMsg("上传出错...");
		}finally {
			if(fileOutputStream !=null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		//判断bgmId是否为空，如果不为空
		//那就查询bgm的信息，并且合成视频，生成新的视频
		if(StringUtils.isNotBlank(bgmId)) {
			
			Bgm bgm=bgmService.queryBgmById(bgmId);
			String mp3InputPath=FILE_SPACE + bgm.getPath();
			
			MergeVideoMp3 tool=new MergeVideoMp3(FFMPEG_EXE);
			String videoInPutPath=finalVideoPath;
			
			String videoOutputName=UUID.randomUUID().toString()+".mp4";
			uploadPathDB="/" + userId +"/video"+"/"+videoOutputName;
			finalVideoPath=FILE_SPACE+uploadPathDB;
			tool.convertor(videoInPutPath, mp3InputPath, videoSeconds, finalVideoPath);
			
		}
		System.out.println("uploadPathDB="+uploadPathDB);
		System.out.println("finalVideoPath="+finalVideoPath);
		
		//对视频进行截图获取封面
		FetchVideoCover videoInfo=new FetchVideoCover(FFMPEG_EXE);
		videoInfo.getCover(finalVideoPath,FILE_SPACE+ coverPathDB);
		
		
		
		
		//保存视频信息到数据库
		Videos video=new Videos();
		video.setAudioId(bgmId);
		video.setUserId(userId);
		video.setVideoSeconds((float)videoSeconds);
		video.setVideoHeight(videoHeight);
		video.setVideoWidth(videoWidth);
		video.setVideoDesc(desc);
		video.setVideoPath(uploadPathDB);
		video.setCoverPath(coverPathDB);
		video.setStatus(VideoStatusEnum.SUCCESS.value);
		video.setCreateTime(new Date());
		
		String videoId=videoService.saveVideo(video);
		
		return IMoocJSONResult.ok(videoId);
		}
	
	
	@ApiOperation(value="上传封面", notes="上传封面的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户id",required=true,
				dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoId",value="视频主键id",required=true,
				dataType="String",paramType="form")
	})
	
	@PostMapping(value="/uploadCover",headers="content-type=multipart/form-data")
	public IMoocJSONResult uploadCover(String userId,
			String videoId,
			@ApiParam(value="视频封面",required=true)
			MultipartFile file) throws Exception {
		
		if(StringUtils.isBlank(videoId)||StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("视频主键id和用户id不能为空");
		}
		
		//文件保存的命名空间
	//	String fileSpace="F:/wjj-video";
		//保存到数据库中的相对路径
		String uploadPathDB="/" + userId +"/video";
		FileOutputStream fileOutputStream=null;
		InputStream inputStream=null;
		String finalCoverPath="";
		try {
			if(file !=null) {
				String fileName=file.getOriginalFilename();
				if(StringUtils.isNotBlank(fileName)) {
					//文件上传的最终保存路径
					finalCoverPath=FILE_SPACE + uploadPathDB +"/" +fileName;
					//设置数据库保存路径、
					uploadPathDB+=("/" +fileName);
					
					File outFile=new File(finalCoverPath);
					if(outFile.getParentFile() !=null || 
							!outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream =new FileOutputStream(outFile);
					inputStream=file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
					
				}
			}else {
				return IMoocJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return IMoocJSONResult.errorMsg("上传出错...");
		}finally {
			if(fileOutputStream !=null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		
		videoService.updateVideo(videoId, uploadPathDB);
		
		return IMoocJSONResult.ok();
		}
	
	/**
	 * 
	 * @Description: 分页和搜索查询视频列表
	 * isSaveRecord：1 - 需要保存
	 * 				 0 - 不需要保存 ，或者为空的时候
	 */
	@PostMapping(value="/showAll")
	public IMoocJSONResult showAll(@RequestBody Videos video,Integer isSaveRecord, 
			Integer page) throws Exception {
		
		if(page==null) {
			page=1;
		}
		PagedResult result=videoService.getAllVideos(video,isSaveRecord,page, PAGE_SIZE);
		return IMoocJSONResult.ok(result);
	}
	

	/**
	 * @Description: 我关注的人发的视频
	 */
	@PostMapping("/showMyFollow")
	public IMoocJSONResult showMyFollow(String userId, Integer page) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.ok();
		}
		
		if (page == null) {
			page = 1;
		}

		int pageSize = 6;
		
		PagedResult videosList = videoService.queryMyFollowVideos(userId, page, pageSize);
		
		return IMoocJSONResult.ok(videosList);
	}
	
	/**
	 * @Description: 我收藏(点赞)过的视频列表
	 */
	@PostMapping("/showMyLike")
	public IMoocJSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.ok();
		}
		
		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = 6;
		}
		
		PagedResult videosList = videoService.queryMyLikeVideos(userId, page, pageSize);
		
		return IMoocJSONResult.ok(videosList);
	}
	
	@PostMapping(value="/hot")
	public IMoocJSONResult hot() throws Exception {	
		return IMoocJSONResult.ok(videoService.getHotwords());
	}
	
	@PostMapping(value="/userLike")
	public IMoocJSONResult userLike(String userId,String videoId,String videoCreaterId) throws Exception {
		videoService.userLikeVideo(userId, videoId, videoCreaterId);
		return IMoocJSONResult.ok();
	}
	
	@PostMapping(value="/userUnLike")
	public IMoocJSONResult userUnLike(String userId,String videoId,String videoCreaterId) throws Exception {	
		videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
		return IMoocJSONResult.ok();
	}
	

	@PostMapping("/saveComment")
	public IMoocJSONResult saveComment(@RequestBody Comments comment, 
			String fatherCommentId, String toUserId) throws Exception {
		
		comment.setFatherCommentId(fatherCommentId);
		comment.setToUserId(toUserId);
		
		videoService.saveComment(comment);
		return IMoocJSONResult.ok();
	}
	
	@PostMapping("/getVideoComments")
	public IMoocJSONResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {
		
		if (StringUtils.isBlank(videoId)) {
			return IMoocJSONResult.ok();
		}
		
		// 分页查询视频列表，时间顺序倒序排序
		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = 10;
		}
		
		PagedResult list = videoService.getAllComments(videoId, page, pageSize);
		
		return IMoocJSONResult.ok(list);
	}
	
}
