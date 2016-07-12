package bluemei.java.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbArgs {
	public Connection con=null;
	public String tableName=null;
	public PreparedStatement pstmt=null;
	public ResultSet rs=null;
	
	public void close() throws SQLException
	{
		SQLException exception=null;
		try {
			if(rs!=null)
				rs.close();
		} catch (SQLException e) {			
			exception=e;
		}finally
		{
			if(pstmt!=null)
				pstmt.close();
		}
		if(exception!=null)
			throw exception;
	}
}
