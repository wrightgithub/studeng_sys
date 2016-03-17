package UI;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jws.Oneway;

import sys.DBHelper;
import sys.ImportData;
import sys.Student;

import static net.mindview.util.Print.print;
interface ModifyHandle
{
	abstract void operation(Student stu);
}
class Thread_Input implements Runnable
{
	public static ImportData list;
	public Thread_Input(ImportData list)
	{
		this.list=list;
	}
	public void modify( Scanner in,ModifyHandle op)
	{
		print("请输入学生学号");
		int id=in.nextInt();
		Student stu=list.getStudent(id);
		if(stu!=null)
		{
			op.operation(stu);
		}
		else {
			print("没有这个人！");
		}
	}
	public void run() {
			final Scanner in=new Scanner(System.in);
			while(true)
			{
				showMenu();
				switch (in.nextInt()) {
				case 1:
					print("请按以下格式输入=> 学号 ,姓名,成绩 ");
					list.add(new Student(in.nextInt(), in.next(), in.nextInt()));
					list.Refresh();
					break;
				case 2: 
					modify(in, new ModifyHandle() {
						public void operation(Student stu) {
							list.remove(stu);
							list.Refresh();
						}
					});

					break;
				case 3:
					print(list.toString());
					break;
				case 4:
					modify(in, new ModifyHandle() {
						public void operation(Student stu) {
							print(stu);
						}
					});				
					break;
				case 5:
					modify(in, new ModifyHandle() {
						public void operation(Student stu) {
							list.remove(stu);
							print("请输入要修改的学生内容=> 学号 ,姓名,成绩");
							list.add(new Student(in.nextInt(), in.next(), in.nextInt()));
							list.Refresh();
						}
					});
					break;
				case 6:
					list.db.ExecAndShow("select * from student");
				default:
					break;
				}
			}
			
	}
	public void showMenu()
	{
		print("请输入操作选项:");
		print(" 1:add\n 2:delete\n 3:show\n 4:search\n 5:edit\n 6:show database");
	}
	
}

public class menu {
	
	public static void main(String[] args)
	{
		DBHelper mysql=new DBHelper("student_sys","1234567890");
		ImportData data=new ImportData("./resource/students_data.txt",mysql);
		ExecutorService exec=Executors.newCachedThreadPool();
		exec.execute(new Thread_Input(data));
	}
}
