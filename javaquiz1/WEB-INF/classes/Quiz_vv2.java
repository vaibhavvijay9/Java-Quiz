import javax.servlet.http.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.util.*;
import java.sql.*;

@WebServlet("/Quiz_vv2")
public class Quiz_vv2 extends HttpServlet
{
    HttpSession session;
    String ans;
    PrintWriter pw;
    public void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException
    {
        pw=res.getWriter();
        session = req.getSession();
        if(session.isNew())
        {
            int qno[]=initial();
            int i=0;
            session.setAttribute("arr",qno);
            session.setAttribute("num",i);
            session.setAttribute("question",qno[i]);             
            session.setAttribute("options",new String[15]); //options contains the marked answer array
            session.setAttribute("ans",new String[15]);
            session.setAttribute("flag",0);
        }
        pw.println("<html>");    
        pw.println("<head>");
        pw.println("<link rel='stylesheet' type='text/css' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'/>");
        pw.println("<link rel='icon' href='favicon.ico' type='image/x-icon' sizes='16x16'>");
		pw.println("<style>");
        pw.println("div {border:1px solid black;margin:10 0 0 0;padding:30 0 0 0;max-width:800px;font-family:Verdana;font-size:12px;color:#58585A;box-sizing:border-box;-moz-user-select:none;-webkit-user-select:none;-ms-user-select:none;user-select:none;-o-user-select:none;}");
        pw.println("div.alert {padding: 20px;background-color: #f44336;color: white;}");
        pw.println("span.closebtn {margin-left: 15px;color: white;font-weight: bold;float: right;font-size: 22px;line-height: 20px;cursor: pointer;transition: 0.3s;}");
        pw.println("span.closebtn:hover {color: black;}");
        pw.println("button.close {padding-right: 20px;}");
        pw.println("</style>");
		
        pw.println("<title>Java Quiz</title>");
        
        pw.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>");
        pw.println("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>");
        pw.println("<script>"); 
        pw.println("window.location.hash='no-back-button';");
        pw.println("window.location.hash='Again-No-back-button';");//again because google chrome don't insert first hash into history
        pw.println("window.onhashchange=function(){window.location.hash='no-back-button';}");
        pw.println("</script>"); 
		
		pw.println("</head>");
        pw.println("<body>");
		
        if(Integer.valueOf(session.getAttribute("flag").toString())==1)
        {
            pw.println("<div class='alert alert-warning alert-dismissible container' role='alert'>");
            pw.println("<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button>");
            pw.println("Please select an option.");
            pw.println("</div>");
            session.setAttribute("flag",0);
        }
		pw.println("<div class='container div'>");
        pw.println("<form name='myForm' method='post'>");
        test(req,res);
        pw.println("</form></body></html>");
    }
    public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException
    {
        pw=res.getWriter();
        session = req.getSession();
        if(session.isNew())
        {
            int qno[]=initial();
            int i=0;
            session.setAttribute("num",i);
            session.setAttribute("arr",qno);
            session.setAttribute("question",qno[i]);
            session.setAttribute("options",new String[15]);
            session.setAttribute("ans",new String[15]);
            session.setAttribute("flag",0);
        }
        else if(req.getParameter("option")!=null)
        {
            int j=Integer.parseInt(session.getAttribute("num").toString());
            session.setAttribute("num",j+1);
            int qnu[]=(int[])session.getAttribute("arr"); 
            try
            {
                session.setAttribute("question",qnu[j+1]);
            }
            catch(Exception e)
            {
            }
            String r[]=(String[])session.getAttribute("options");   //everytime we fill r from 0 ,means we overwrite it
            r[j]=req.getParameter("option").toString();
            session.setAttribute("options",r);
        }
        else
        {
           session.setAttribute("flag",1);
        }
        doGet(req,res);  
    }
    public void test(HttpServletRequest req,HttpServletResponse res) throws IOException
	{
	  PrintWriter pw=res.getWriter();	
	  try
		{
            int count=0;
            session=req.getSession();
            int i=Integer.valueOf(session.getAttribute("num").toString());
			if(i==15)
			{
				res.sendRedirect("Result");
			}
			else
			{
                int k= Integer.valueOf(session.getAttribute("question").toString());
				String query="select * from quiz where qno=?";
				PreparedStatement ps=DBInfo.con.prepareStatement(query);
				ps.setInt(1,k);
                ResultSet rs=ps.executeQuery();
				rs.next();
                pw.println("<html><body>");

				pw.println("<center><b>Question "+(i+1)+". of 15</b></center><br>");
				pw.println(rs.getString(2));
				pw.println("<br><br><label><input type='radio' name='option' value='a'>"+rs.getString(3)+"</label>");
				pw.println("<br><br><label><input type='radio' name='option' value='b'>"+rs.getString(4)+"</label>");
				pw.println("<br><br><label><input type='radio' name='option' value='c'>"+rs.getString(5)+"</label>");
				pw.println("<br><br><label><input type='radio' name='option' value='d'>"+rs.getString(6)+"</label>");
				pw.println("<br><br><input type='submit' value='Submit' onclick='check()'");
                String r[]=(String[])session.getAttribute("ans");   //everytime we fill r from 0 ,means we overwrite it
                r[i]=rs.getString(7);
                session.setAttribute("ans",r);

				pw.println("</body></html>");		
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
    public int[] initial()
    {
        //getting 15 unique random questions
        TreeSet<Integer> ts=new TreeSet<>();
		int n;
		int j=0;
		int qno[];
        while(ts.size()!=15)
		{
			n=(int)(Math.random()*156)+1;
			ts.add(n);
		}
		qno=new int[ts.size()];
		
		for(int num:ts)
		{
			qno[j++]=num;
		}
        return qno;
    }
}