package com.wjj.service;


import java.util.List;

import com.wjj.pojo.Bgm;
import com.wjj.pojo.Users;

public interface BgmService {
	/**
	 * 查询背景音乐列表
	 * @return
	 */
	
	public List<Bgm> queryBgmList();
	/**
	 * 根据id查询背景音乐
	 * @param bgmId
	 * @return
	 */
	public Bgm queryBgmById(String bgmId);
	
}
