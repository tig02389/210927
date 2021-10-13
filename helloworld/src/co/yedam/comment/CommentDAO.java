package co.yedam.comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.yadam.common.DAO;

public class CommentDAO extends DAO {
	//싱글톤 방식
	private static CommentDAO singleton = new CommentDAO();
	private CommentDAO() {
		
	}
	public static CommentDAO getInstance() {
		return singleton;
	}
	
	// 나중에 만들어주기 위해서 void로 만들어줌 
	// 글목록..
	public List<Comment> getCommnetList() {
		connect();
		List<Comment> list = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from comments order by 1");
			
			while(rs.next()) {
				Comment cmt = new Comment();
				cmt.setId(rs.getString("id"));
				cmt.setContent(rs.getString("content"));
				cmt.setName(rs.getString("name"));
				list.add(cmt);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			disconnect();
		}
		return list;
	}
		//글입력..1)현재id 2) id+1 값으로 글등록 3) 현재id 값 변경.
		public Comment insertComment(Comment comment) {		//값을 Comment형식으로 넘겨주겠습니다.
		connect();
		int currId = 0; //현재 id 값을 담고 있는 변수 
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select value from id_repository where name = 'COMMENT'");
			if(rs.next()) {
				currId = rs.getInt("value");
			}
			currId++;
			psmt = conn.prepareStatement("insert into comments values(?,?,?)");
			psmt.setInt(1, currId);
			psmt.setString(2, comment.getName());
			psmt.setString(3, comment.getContent());
			int r = psmt.executeUpdate(); // 몇건이 반환되는지 담는 변수 
			System.out.println(r+" 건 입력.");
			
			psmt = conn.prepareStatement("update id_repository set value=? where name ='COMMENT'");//벨류값을 계속 받아야지 최신화 된다 ?
			psmt.setInt(1, currId);
			r = psmt.executeUpdate();
			System.out.println(r + "건 변경.");
			
			conn.commit();
			comment.setId(String.valueOf(currId));
			return comment;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;	//예외가 발생한 부분은 아무것도 안넘기겠습니다. 
		}finally {
			disconnect();
		}
	}
	//글입력 ..
	public void insetComment() {
		
	}
	//글 수정...
	public Comment updateComment(Comment comment) {
	      connect();
	      String sql = "update comments set name=?, content=? where id=?";
	      try {
	         psmt = conn.prepareStatement(sql);
	         psmt.setString(1, comment.getName());
	         psmt.setString(2, comment.getContent());
	         psmt.setString(3, comment.getId());
	         int r = psmt.executeUpdate();
	         System.out.println(r + "건 변경.");
	         return comment;
	      } catch (SQLException e) {
	         e.printStackTrace();
	         return null;
	         
	      } finally {
	         disconnect();
	      }
	   }   
	//글 삭제...
	public String deleteComment(String id) {
		connect();
		try {
			psmt = conn.prepareStatement("delete from comments where id=?");
			psmt.setString(1, id);
			int r = psmt.executeUpdate();
			System.out.println(r+"건 삭제");
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
			disconnect();
		}
	}
}
