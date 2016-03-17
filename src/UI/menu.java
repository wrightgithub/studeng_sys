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
		print("������ѧ��ѧ��");
		int id=in.nextInt();
		Student stu=list.getStudent(id);
		if(stu!=null)
		{
			op.operation(stu);
		}
		else {
			print("û������ˣ�");
		}
	}
	public void run() {
			final Scanner in=new Scanner(System.in);
			while(true)
			{
				showMenu();
				switch (in.nextInt()) {
				case 1:
					print("�밴���¸�ʽ����=> ѧ�� ,����,�ɼ� ");
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
							print("������Ҫ�޸ĵ�ѧ������=> ѧ�� ,����,�ɼ�");
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
		print("���������ѡ��:");
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
