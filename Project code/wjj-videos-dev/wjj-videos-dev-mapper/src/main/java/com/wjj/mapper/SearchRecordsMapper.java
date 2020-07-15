package com.wjj.mapper;

import java.util.List;

import com.wjj.pojo.SearchRecords;
import com.wjj.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	public List<String> getHotwords();
}