public class JavaAndArithmetic
{
	public static void main(String[] args)
	{
		int[] params={1,2,3,4,5,6};
		int result=100;
		Arithmetic Arithmetic1=new Arithmetic(params,result);
		Arithmetic1.showResult();
	}
}
class Arithmetic
{
	int n,r,q;
	int[] p;
	boolean[] searched;
	String expression="";
	public Arithmetic(int[] params,int result)
	{
		this.p=params;
		this.r=result;
		this.n=params.length;
		this.searched=new boolean[n];
		for(int i=0;i<n;i++)this.searched[i]=false;
	}
	public void showResult()
	{
		this.q=0;
		for(int i=0;i<n;i++)
		{
			String exp=""+p[i];
			this.searched[i]=true;
			this.findResult(exp,p[i],n-1);
			this.searched[i]=false;
		}
		if(expression.equals(""))System.out.println("No Result");
		else System.out.println("There are "+q+" expressions in total...\n"+expression);
	}
	private void findResult(String exp,int t,int l)
	{
		if(l==0&&t==r){this.expression+="Expression="+exp+"\n";this.q++;}
		for(int i=0;i<n;i++)	
		{
			if(!searched[i])
			{
				this.searched[i]=true;
				String e=l==n-1?exp:"("+exp+")";
				this.findResult(e+"+"+p[i],t+p[i],l-1);
				this.findResult(e+"-"+p[i],t-p[i],l-1);
				this.findResult(e+"*"+p[i],t*p[i],l-1);
				this.findResult(e+"/"+p[i],t/p[i],l-1);
				this.searched[i]=false;
			}
		}
	}
}
