package com.wjj.service.impl;

import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import com.wjj.mapper.BgmMapper;
import com.wjj.mapper.UsersMapper;
import com.wjj.pojo.Bgm;
import com.wjj.pojo.Users;
import com.wjj.service.BgmService;
import com.wjj.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class BgmServiceImpl implements BgmService {
	
	@Autowired //注入mapper并且自动为mapper赋值
	private BgmMapper bgmMapper;
	@Autowired
	private Sid sid;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public List<Bgm> queryBgmList() {
		
		return  bgmMapper.selectAll();
	}
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Bgm queryBgmById(String bgmId) {
		// TODO Auto-generated method stub
		return bgmMapper.selectByPrimaryKey(bgmId);
	}
	
}
