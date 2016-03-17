package sys;

import java.sql.*;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;



public class DBHelper {
	public static final String url = "jdbc:mysql://127.0.0.2/";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "root";
	public static final String password = "1234567890";

	private Connection conn ;
	private Statement state;
	private ResultSet ret ;
	public DBHelper(String database_name,String password)
	{
		try {
			conn = DriverManager.getConnection(url+database_name, user, password);
			System.out.println("连接成功");
			state =conn.createStatement();
		} catch (SQLException e) {
			System.out.println("连接失败");
		}
	}
	
	public Statement getStatement()
	{
		return state;
	}
	public ResultSet getResultSet(String sql)
	{
		ResultSet resultSet = null;
		try {
			resultSet= state.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("getResultSet exception");
		}
		return resultSet;
	}
	public boolean SetSql(String sql)
	{
		try {
			state.execute(sql);
		}catch(MySQLIntegrityConstraintViolationException e)
		{
			System.out.println("重复导入 ："+sql);
		}catch (SQLException e) {
			System.out.println("SetSql exception");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void ExecAndShow(String sql)
	{
		ResultSet ret=getResultSet(sql);
		Student stu;
		try {
			while(ret.next())
			{
				stu=new Student(Integer.parseInt(ret.getString(1)), 
						ret.getString(2), Integer.parseInt(ret.getString(3)));
				System.out.println(stu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		try {
			this.conn.close();
			this.state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
}
