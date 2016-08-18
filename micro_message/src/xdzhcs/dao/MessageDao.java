package xdzhcs.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

import xdzhcs.db.DBAcess;
import xdzhcs.entity.Message;

/**
 * 和message表相关的数据库操作
 * @author sanders
 *
 */
public class MessageDao {
	/**
	 * 根据查询条件查询列表
	 */
	public List<Message> queryMessages(String command,String description){
		DBAcess dbAcess=new DBAcess();
		List<Message> messages=null;
		SqlSession sqlSession=null;
		try {
			sqlSession=dbAcess.getSqlSession();
			//通过SqlSession执行SQL语句
			Message param=new Message();
			param.setCommand(command);
			param.setDescription(description);
			messages=sqlSession.selectList("Message.queryMessages",param);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(sqlSession!=null){
				sqlSession.close();
			}
		}
		return messages;
	}
	/**
	 * 单条删除
	 * @param id
	 */
	public void deleteOne(int id){
		DBAcess dbAcess=new DBAcess();
		SqlSession sqlSession=null;
		try {
			sqlSession=dbAcess.getSqlSession();
			//通过SqlSession执行SQL语句
			//sqlSession.selectList("Message.deleteOne",id);
			sqlSession.delete("Message.deleteOne", id);
			sqlSession.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(sqlSession!=null){
				sqlSession.close();
			}
		}
	}
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteBatch(List<Integer> ids){
		DBAcess dbAcess=new DBAcess();
		SqlSession sqlSession=null;
		try {
			sqlSession=dbAcess.getSqlSession();
			//通过SqlSession执行SQL语句
			//sqlSession.selectList("Message.deleteOne",id);
			sqlSession.delete("Message.deleteBatch", ids);
			sqlSession.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(sqlSession!=null){
				sqlSession.close();
			}
		}
	}
//	public List<Message> queryMessages(String command,String description){
//		List<Message> messages = new ArrayList<Message>();
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/micro_message","root","123456");
//			StringBuilder sql=new StringBuilder("select ID,COMMAND,DESCRIPTION,CONTENT from MESSAGE where 1=1");
//			List<String>params = new ArrayList<String>();
//			if(command!=null&&!command.trim().equals("")){
//				sql.append(" and COMMAND=?");
//				params.add(command);
//			}
//			if(description!=null&&!description.trim().equals("")){
//				sql.append(" and DESCRIPTION like '%' ? '%'");
//				params.add(description);
//			}
//			
//			PreparedStatement statement=(PreparedStatement) conn.prepareStatement(sql.toString());
//			
//			for(int i=0;i<params.size();i++){
//				statement.setString(i+1, params.get(i));
//			}
//			System.out.println("sql="+sql.toString());
//			ResultSet resultSet=(ResultSet) statement.executeQuery();
//			
//			
//			while(resultSet.next()){
//				Message msg= new Message();
//				msg.setId(resultSet.getInt("ID"));
//				msg.setCommand(resultSet.getString("COMMAND"));
//				msg.setDescription(resultSet.getString("DESCRIPTION"));
//				msg.setContent(resultSet.getString("CONTENT"));
//				messages.add(msg);
//			}
//			//request.setAttribute("messages", messages);
//			System.out.println("messages.size():"+ messages.size());
//			//request.setAttribute("messages.size():", messages.size());
//			
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		return messages;
//	}
}
