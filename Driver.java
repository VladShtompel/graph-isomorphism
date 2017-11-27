import jdk.nashorn.internal.objects.Global;
import static java.lang.Math.toIntExact;
import java.math.BigInteger;
import java.util.List;

public class Driver {

	public static Graph g1, g2;

	public static void main(String[] args) 
	{
		try 
		{ 
			g1 = new Graph("/home/vladi/Desktop/Isomorphic/Graphs/grid1.txt");
			g2 = new Graph("/home/vladi/Desktop/Isomorphic/Graphs/grid2.txt");
		}
		catch(Exception e)
		{ System.out.println(e.getMessage()); return; }

		//System.out.println("G1 tree? " + g1.tree);
		//System.out.println("G2 tree? " + g2.tree);

		if(g1.tree && g2.tree)
		{
			Tree t1 = new Tree(g1);
			Tree t2 = new Tree(g2);

			t1.print();
			t2.print();

			printTreeIso(t1.Isomorphic(t2));

		}
		else if(!g1.tree && g2.tree)
			g1.graphInfo();
		else if(!g2.tree && g1.tree)
			g2.graphInfo();
		else
		{
			printGraphIso(g1.Isomorphic(g2));
			g1.print();
			g2.print();
		}

	}


	public static void printTreeIso(List<List<Vertex>> M)
	{
		if(M.isEmpty())
		{
			System.out.println("The Trees are not Isomorphic!");
		}
		else
		{
			System.out.println("The Trees are Isomorphic!");
			System.out.println("The Isomorphism is:");
			for(List<Vertex> l: M)
			{
				System.out.println("V" + (l.get(0).name+1) + " --> V" + (l.get(1).name+1));
			}
		}
	}

	public static void printGraphIso(List<List<Vertex>> M)
	{
		if(M.isEmpty())
		{
			System.out.println("The Graphs are not Isomorphic!");
		}
		else
		{
			long ch1=1;
			BigInteger ch2 = factorial1(g1.v_num);
			for(Vertex v: g1.vertices)
				ch1= Math.max(v.possibleCandid.size(), ch1);
			ch1 = factorial(ch1);
			System.out.println("The Graphs are Isomorphic!");
			System.out.println("Our program only checked " + ch1 + " combinations instead of "+ ch2);
			//System.out.println("That's roughly "+ Math.max(Math.floor(ch2/ch1), 1) + " times faster!" );
			int bar = toIntExact(ch1);
			System.out.println("That's roughly "+ch2.divide(BigInteger.valueOf(bar))+" times faster");
			System.out.print("The Isomorphism");
			if(M.size() == 1) System.out.println(" is:");
			else System.out.println("s are:");
			for(int j=0; j<M.size(); j++)
			{
				if(M.size() > 1) System.out.println("#### "+ (j+1) + " ####");
				for(int i=0; i<M.get(j).size(); i++)
					System.out.println("V" + (i+1) + " --> V" + (M.get(j).get(i).name+1));
				System.out.println();
				System.out.println();
			}
		}
	}

	public static BigInteger factorial1(int n) {
		BigInteger result = BigInteger.ONE;
		for (; n > 1; n--) {
			result = result.multiply(BigInteger.valueOf(n));
		}
		return result;
	}

	private static long factorial(long n) {
		if (n <= 1)
			return 1;
		else
			return n * factorial(n - 1);
	}



}
