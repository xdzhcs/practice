package xdzhcs.service;

import java.util.ArrayList;
import java.util.List;

import xdzhcs.dao.MessageDao;

/**
 * 维护相关的业务功能
 *
 */
public class MaintainService {
	
	public void deleteOne(String id){
		System.out.println("MaintainService-->deleteOne()-->id:"+id);
		if(id!=null&& !id.equals("")){
			MessageDao mDao = new MessageDao();
			try{
				System.out.println("MaintainService-->deleteOne()-->try-->id:"+id);
				mDao.deleteOne(Integer.valueOf(id));
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("转换错误");
			}
		}
	}
	public void deleteBatch(String[] ids){
		System.out.println("MaintainService-->deleteBatch-->ids:"+ids);
		MessageDao mDao = new MessageDao();
		List<Integer> idList=new ArrayList<Integer>();
		for(String id:ids){
			idList.add(Integer.valueOf(id));
		}
		mDao.deleteBatch(idList);
	}
}
