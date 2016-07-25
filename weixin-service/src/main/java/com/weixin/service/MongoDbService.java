package com.weixin.service;

import java.io.InputStream;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

/**
 * mongodb相关操作工具类
 * @author Alan Fu
 * @date 2015年5月25日
 * @version 0.0.1
 */
//@Service
//@Transactional
public class MongoDbService {

	@Autowired
	private GridFsTemplate gridFsTemplate; //mongodb操作

	/**
	 * 文件保存
	 * 
	 * @param input
	 * @param fileName
	 * @param imageType
	 * @return
	 */
	public String save(InputStream input, String fileName, String imageType) {
		GridFSFile gridFile = gridFsTemplate.store(input, fileName, imageType);
		return gridFile.getId().toString();
	}

	/**
	 * 根据objectId查找文件
	 * 
	 * @param ids
	 * @return
	 */
	public List<GridFSDBFile> find(String... ids) {
		if (null != ids) {
			ObjectId[] objectIds = new ObjectId[ids.length];
			for (int i = 0; i < ids.length; i++) {
				objectIds[i] = new ObjectId(ids[i]);
			}
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").in(objectIds));
			return gridFsTemplate.find(query);
		}
		return null;
	}

	/**
	 * 根据openId查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<GridFSDBFile> findByOpenIds(List<String> ids) {
		if (null != ids) {
			ObjectId[] objectIds = new ObjectId[ids.size()];
			int i = 0;
			for (String id : ids) {
				objectIds[i] = new ObjectId(id);
				i++;
			}
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").in(objectIds));
			return gridFsTemplate.find(query);
		}
		return null;
	}

	/**
	 * 根据id查找
	 * 
	 * @param id
	 * @return
	 */
	public GridFSDBFile findByMongoId(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		return gridFsTemplate.findOne(query);
	}
}
