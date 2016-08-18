package xdzhcs.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.javassist.compiler.ast.NewExpr;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 访问数据库类
 * @author sanders
 *
 */
public class DBAcess {
	public SqlSession getSqlSession() throws IOException{
		//通过配置文件获取数据库连接信息
		Reader reader=Resources.getResourceAsReader("xdzhcs/config/Configuration.xml");
		//通过配置信息构建一个SqlSessionFactory
		SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(reader);
		//通过SqlSessionFactory打开一个数据库会话
		SqlSession sqlSession=factory.openSession();
		return sqlSession;
		
	}
}
