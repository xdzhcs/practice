package xdzhcs.service;

import java.util.List;

import xdzhcs.dao.MessageDao;
import xdzhcs.entity.Message;

/**
 * 列表相关的业务功能
 * @author sanders
 *
 */
public class ListService {
	public List<Message> queryMessages(String command,String description){
		MessageDao mDao=new MessageDao();
		return mDao.queryMessages(command, description);
	}
}
