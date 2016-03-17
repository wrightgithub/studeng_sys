package sys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ImportData extends ArrayList<Student> 
{
	private File file;
	public DBHelper db;
	public ImportData(String filepath,DBHelper db)
	{
		file=new File(filepath);
		this.db=db;
		getContent(filepath);
		if(ImportToDatabase())
			System.out.println("导入数据库成功");
		
	}
	public boolean ImportToDatabase()
	{
		String sql_first="INSERT INTO student VALUES ";
		for(Student stu:this)
		{
			String sql=sql_first+stu.toSqlInsert();
			if(!db.SetSql(sql))
			{
				System.err.println("ImportToDatabase Error!==> "+stu);
				System.out.println(sql);
				System.exit(0);
			}
		}
		
		return true;
	}
	public void fileclear()
	{
		PrintWriter out = null;
		try {
			out = new PrintWriter(file);
			out.write("");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			out.close();
		}
	}
	public boolean ImportToFile() 
	{
		
		 PrintWriter out = null;
		try {
			fileclear();
			out = new PrintWriter(file);
			out.print(toString());
		} catch (IOException e) {
			System.out.println("ImportToFile：写入失败");
			e.printStackTrace();
			return false;
		}finally {
			out.close();
		}
		 return true;
	}
	public boolean Refresh() 
	{
		boolean flag =ImportToDatabase()&&ImportToFile();
		assert flag:"Refresh 失败";
		return  flag;
	}
	private void getContent(String filepath)
	{
		try {
			BufferedReader in=new BufferedReader(new FileReader(filepath));
			try {
				String line;
				String[] str;
				Student student;
				while((line=in.readLine())!=null)
				{
					str=line.split(" +");
					student=new Student(Integer.parseInt(str[0]),
										str[1],Integer.parseInt(str[2]));
					add(student);
				}
			} finally {
				in.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("getContent：文件不存在");
		}catch (Exception e) {
			System.out.println("getContent：其他异常");
			e.printStackTrace();
		}
	}
	public Student getStudent(int id)
	{
		for(Student stu:this)
		{
			if(stu.equals(id))
				return stu;
		}
		return null;
	}
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
		for(Student stu:this)
		{
			sb.append(stu+"\n");
		}
		return sb.toString();
	}
	public static void main(String[] args) 
	{
		DBHelper mysql=new DBHelper("student_sys","1234567890");
		ImportData data=new ImportData("./resource/students_data.txt",mysql);
		//data.ImportToFile();
		//mysql.ExecAndShow("select * from student");
		//System.out.println(data);
	}
}
