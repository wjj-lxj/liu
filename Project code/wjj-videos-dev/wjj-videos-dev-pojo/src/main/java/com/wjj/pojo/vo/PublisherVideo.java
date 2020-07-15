package com.wjj.pojo.vo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


public class PublisherVideo {
public UsersVo publisher;
public boolean userLikeVideo;
public UsersVo getPublisher() {
	return publisher;
}
public void setPublisher(UsersVo publisher) {
	this.publisher = publisher;
}
public boolean isUserLikeVideo() {
	return userLikeVideo;
}
public void setUserLikeVideo(boolean userLikeVideo) {
	this.userLikeVideo = userLikeVideo;
}

}