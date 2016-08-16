package xdzhcs.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

import xdzhcs.entity.Message;

/**
 * Servlet implementation class ListServlet
 */
@WebServlet("/ListServlet")
public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		try {
			String command=request.getParameter("command");
			String description=request.getParameter("description");
			request.setAttribute("command", command);
			request.setAttribute("description", description);
			System.out.println("description="+description);
			System.out.println("command="+command);
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/micro_message","root","123456");
			StringBuilder sql=new StringBuilder("select ID,COMMAND,DESCRIPTION,CONTENT from MESSAGE where 1=1");
			List<String>params = new ArrayList<String>();
			if(command!=null&&!command.trim().equals("")){
				sql.append(" and COMMAND=?");
				params.add(command);
			}
			if(description!=null&&!description.trim().equals("")){
				sql.append(" and DESCRIPTION like '%' ? '%'");
				params.add(description);
			}
			
			PreparedStatement statement=(PreparedStatement) conn.prepareStatement(sql.toString());
			
			for(int i=0;i<params.size();i++){
				statement.setString(i+1, params.get(i));
			}
			System.out.println("sql="+sql.toString());
			ResultSet resultSet=(ResultSet) statement.executeQuery();
			
			List<Message> messages = new ArrayList<Message>();
			while(resultSet.next()){
				Message msg= new Message();
				msg.setId(resultSet.getInt("ID"));
				msg.setCommand(resultSet.getString("COMMAND"));
				msg.setDescription(resultSet.getString("DESCRIPTION"));
				msg.setContent(resultSet.getString("CONTENT"));
				messages.add(msg);
			}
			request.setAttribute("messages", messages);
			System.out.println("messages.size():"+ messages.size());
			//request.setAttribute("messages.size():", messages.size());
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("/WEB-INF/jsp/back/list.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
