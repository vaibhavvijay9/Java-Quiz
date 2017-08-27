import java.sql.*;

class DBInfo
{
	static Connection con;
	static
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db","root","rat");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}