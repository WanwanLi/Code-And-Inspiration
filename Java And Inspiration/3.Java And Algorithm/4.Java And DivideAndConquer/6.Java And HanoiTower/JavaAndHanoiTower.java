public class JavaAndHanoiTower
{
	public static void main(String[] args)
	{
		HanoiTower HanoiTower1=new HanoiTower(3);
		HanoiTower1.showResult();
	}
}
class HanoiTower
{
	int number;
	public HanoiTower(int number)
	{
		this.number=number;
	}	
	private void movePlate(int n,char A,char B,char C)
	{
		if(n==1)System.out.println("Move Plate"+n+" From "+A+" To "+C);
		else
		{
			movePlate(n-1,A,C,B);
			System.out.println("Move Plate"+n+" From "+A+" To "+C);
			movePlate(n-1,B,A,C);
		}
	}
	public void showResult()
	{
		this.movePlate(number,'A','B','C');	
	}
}






