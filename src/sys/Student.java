package sys;

public class Student implements Comparable<Student>
{
	private int id;
	private String name;
	private int mark;
	Student(){}
	public Student(int id,String name,int mark)
	{
		this.id=id;
		this.name=name;
		this.mark=mark;
	}
	@Override
	public int compareTo(Student o) {
		return this.id<o.id?-1:(id==o.id?0:1);
	}
	public String toSqlInsert()
	{
		return "('"+id+"',"+"'"+name+"',"+"'"+mark+"');";
	}
	public String toString()
	{
		return id+" "+name+"  "+mark;
	}
	public boolean equals(int search_id) {
		return  this.id==search_id;
	}
}