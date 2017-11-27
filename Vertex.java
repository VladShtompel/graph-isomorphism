import java.util.ArrayList;

public class Vertex implements Cloneable {

	//tree algo
	public int depth;
	public int label;
	public Vertex parent;
	public ArrayList<Vertex> children = new ArrayList<Vertex>();
	public ArrayList<Integer> orderedlabels = new ArrayList<Integer>();
	public ArrayList<Vertex> orderedchildren = new ArrayList<Vertex>();

	//graph algo
	public int triangles;
	public int squares;
	public ArrayList<Vertex> possibleCandid = new ArrayList<Vertex>();

	//general
	public int name;
	private int rank;
	public ArrayList<Vertex> neighbours = new ArrayList<Vertex>();

	public Vertex(int num)
	{
		name = num;
		rank = 0;
		triangles = 0;
		squares = 0;
	}

	public Vertex(Vertex other)
	{
		deepCopy(other);
	}

	private Vertex deepCopy(Vertex other)
	{
		if(other != null)
		{
			name = other.name;
			rank = other.rank;
			triangles = other.triangles;
			squares = other.squares;
			label = other.label;
			depth = other.depth;
			parent = deepCopy(parent);

			orderedlabels = new ArrayList<Integer>();
			for(Integer i: other.orderedlabels) { orderedlabels.add(new Integer(i)); }

			possibleCandid = new ArrayList<Vertex>();
			for(Vertex v: other.possibleCandid) { possibleCandid.add(new Vertex(v)); }

			orderedchildren = new ArrayList<Vertex>();
			for(Vertex v: other.orderedchildren) { orderedchildren.add(new Vertex(v)); }

			neighbours = new ArrayList<Vertex>();
			children = new ArrayList<Vertex>();
			for(Vertex v: other.children) { children.add(new Vertex(v));
											neighbours.add(new Vertex(v)); }
			neighbours.add(parent);
		}
		return this;
	}


	public void print()
	{
		System.out.println();
		System.out.println();
		System.out.println("Vertex: V" + (name+1) + "\nRank: " + rank);

		//if a tree:
		if(parent != null || !children.isEmpty())
		{
			System.out.println("Vertex Label: " + label);
			System.out.print("Ordered Labels: ");
			for (Integer n : orderedlabels)
			{
				System.out.print(n + " ");
			}
			System.out.println();

			System.out.println("Vertex Depth: " + depth);
			if(parent != null)
				System.out.println("Parent: V" + (parent.name+1));

			else System.out.println("Parent: null");

			System.out.print("Children: { ");
			for (Vertex c : children)
				System.out.print("V" + (c.name+1) + " ");

			System.out.print("}");
			System.out.println();
		}

		else
		{
			System.out.println("In " + triangles + " triangles");
			System.out.println("In " + squares + " squares");
			System.out.print("Possible Candidates: { ");
			for (Vertex c : possibleCandid)
				System.out.print("V" + (c.name + 1) + " ");

			System.out.print("}");
			System.out.println();
		}
	}

	public void addNeighb(Vertex v)
	{
		neighbours.add(v);
		rank++;
	}

	public int getRank()
	{
		return rank;
	}

	public int getLabel() { return label; }

}
