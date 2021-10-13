package co.yedam.comment;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class CommentServlet
 */
@WebServlet("/CommentServlet")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CommentServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// response= 응답정보를 내보낸다.  
		// request = 사용자가 요청한 것을 처리 
		request.setCharacterEncoding("UTF-8");					//요청한 데이터? 
		response.setCharacterEncoding("UTF-8");					//데이터로 넘겨질 때 
		response.setContentType("text/html; charset=UTF-8");	//보여지는 내용이 html 파일이면 
		
			
		PrintWriter out = response.getWriter();
		Gson gson = new GsonBuilder().create();
		
		
		String cmd = request.getParameter("cmd");	//파라미터가 cmd
		CommentDAO dao = CommentDAO.getInstance();
		
		if(cmd == null) {
			out.println("<h1>빈페이지입니다.</h1>");
			
		}else if(cmd.equals("list")) {
			System.out.println("<h1>리스트 페이지입니다.</h1>");
			
			List<Comment> list = dao.getCommnetList();
	
			out.println(gson.toJson(list));
			
		}else if(cmd.equals("add")) {
			System.out.println("<h1>추가페이지입니다..</h1>");
			String name = request.getParameter("name");			//디비에 입력하기 위해서 
			String content = request.getParameter("content");	//디비에 입력하기 위해서 
			
			Comment comment = new Comment();
			comment.setName(name);
			comment.setContent(content);
			
			dao.insertComment(comment);	//인서트 처리 //에러가 나면 NULL이 넘어옴 
			
			out.println(gson.toJson(comment));	//제이슨으로 바꿔서 출력 	가지고온 데이터를 제이슨으로 바꿔줌 
			
		}else if(cmd.equals("mod")) {
			System.out.println("<h1>수정페이지입니다.</h1>");
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String content = request.getParameter("content");
			
			  Comment comment = new Comment();
		         comment.setId(id);
		         comment.setName(name);
		         comment.setContent(content);
		         
		         dao.updateComment(comment);
		         out.println(gson.toJson(comment));
			
			
		}else if(cmd.equals("del")) {
			System.out.println("<h1>삭제페이지입니다.</h1>");
			String id = request.getParameter("id");
			
			if(dao.deleteComment(id) == null) {
				//{"retCode":"fail"}
				out.println("{\"retCode\":\"fail\"}");
				return;
			}
			out.println("{\"retCode\":\"success\"}");
		}else {
			out.println("<h1>" + cmd +"</h1>");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
