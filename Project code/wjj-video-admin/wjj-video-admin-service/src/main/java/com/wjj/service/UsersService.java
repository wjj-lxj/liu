package com.wjj.service;

import com.wjj.pojo.Users;
import com.wjj.utils.PagedResult;

public interface UsersService {

	public PagedResult queryUsers(Users user, Integer page, Integer pageSize);
	
}
