import java.io.File;
import java.util.*;

public class Graph
{
	
	public int [][] mat;	//the 0-1 matrix
	public int v_num;		//number of vertices
	public int e_num;		//number of edges
	public int tri_num;		//number of triangles in graph
	public int squ_num;		//number of squares in graph
	
	public boolean tree;		//true\false
	public boolean anti_reflex;	//should always be true
	public boolean symmetric;	//should always be true
	public String path;         //path where file is in

	public ArrayList<Vertex> vertices;	//a list of the graph vertices as Vertex objects


	private Scanner s, f;
	public Graph(String path) throws Exception
	{
		Open_file(path);
		try{ Read_file(); }
		catch(Exception e){ System.out.println(e.getMessage()); return;}
		Close_file();
		
		countRanks();	
		e_num = countEdges();
		tri_num = countTriangles();
		squ_num = countSquares();
		symmetric = isSymmetric();
		anti_reflex = isAntiReflexive();
		this.path=path;

		if(symmetric)
		{
			if(anti_reflex) {}
			else throw new Exception("Not Anti-Reflexive graph: " + path);
		}

		else throw new Exception("Not symmetric graph: " + path);

		tree = isTree();
	}

	public Graph(Graph g)
	{
		mat = g.mat;
		v_num = g.v_num;
		e_num = g.e_num;
		tri_num = g.tri_num;
		squ_num = g.squ_num;
		symmetric = g.symmetric;
		anti_reflex = g.anti_reflex;
		tree = g.tree;
		this.path=g.path;
		vertices = new ArrayList<Vertex>();
		for(Vertex v: g.vertices)
		{
			vertices.add(v);
		}
	}


	private void Open_file(String path)
	{
		try 
		{
			f=new Scanner(new File(path));
			s=new Scanner(new File(path));
		}
		catch(Exception e){System.out.println("Could not open file: " + path);}
	}
	
	private void Read_file() throws Exception
	{
		vertices = new ArrayList<Vertex>();
		v_num = s.nextInt();



		if(Check_size())
		{
			mat = new int[v_num][v_num];
			for(int i=0; i<v_num; i++)
				for (int j = 0; j < v_num; j++)
				{
					if(s.hasNextInt())
						mat[i][j] = s.nextInt();
					//else s.next();
				}
		} 
		else throw new Exception("Bad input file");
	}
	
	private void Close_file()
	{
		s.close();
		f.close();	
	}
	void graphInfo()
	{
		System.out.print("Graph in File text : ");
		System.out.print(get_path());
		System.out.println(" Is not a Tree");
	}
	public String get_path(){return this.path;}
	private boolean Check_size()
	{
		int count=-1;
		int count1=0;
		while(f.hasNext())
		{
				f.nextInt();
				count += 1;

		}
		if(v_num==Math.sqrt(count))
			return true;

		else return false;
	}

	private void countRanks()
	{
		for(int i=0; i<v_num; i++)
			vertices.add(new Vertex(i));

		for(int i=0;i<mat.length;i++)
			for (int j = 0; j < mat.length; j++)
				if (mat[i][j] == 1)
					vertices.get(i).addNeighb(vertices.get(j));
	}

	private boolean isSymmetric()
	{
	    for(int a = 0; a < mat.length; a++)
	        for(int b = 0; b < mat.length; b++)
	            if(mat[a][b]!=mat[b][a])
	                return false;

	    return true;
	}
		
	private boolean isAntiReflexive()
	{
		for(int i=0;i<mat.length;i++)
			if (mat [i][i]!=0) return false;
		
		return true;	
	}
	
	private int countTriangles()
	{
		int count =0;
		for (int i = 0;i < mat.length;i++) 
		   for (int j = i+1;j < mat.length;j++)
			   if (mat[i][j] == 1)
				  for (int k = j+1;k < mat.length;k++)
					 if (mat[i][k] == 1 && mat[j][k] == 1)
					 {
						 count++;
						 for(Vertex v: vertices)
							if(v.name == i || v.name == j || v.name == k)
								v.triangles++;
					 }
		return count;
	}
	
	public void print()
	{
		System.out.println("-------------------------------------------------");
		System.out.println("Graph info:\n");
		System.out.println(this.path+"\n");
		for(int i=0;i<v_num;i++)
		{
			for(int j=0;j<v_num;j++)
				System.out.print(mat[i][j] + " ");

			System.out.println();
		}
		System.out.println();
		System.out.println("Number of verticies: " + v_num);		
		System.out.println("Number of edges: " + e_num);
		System.out.print("Vertices and ranks: ");

		for(Vertex v : vertices)
			v.print();

		System.out.println();
		System.out.println("Triangles in graph: " + tri_num);
		System.out.println("Squares in graph: " + squ_num);
		System.out.println("Symmetric: " + symmetric);
		System.out.println("Anti-reflexive: " + anti_reflex);
		System.out.println("Tree: " + tree);
		System.out.println("-------------------------------------------------");
	}
	
	private int countEdges()
	{
		int sum = 0;
		for(int i=0; i<v_num; i++)
			sum += vertices.get(i).getRank();
		
		return sum/2;		
	}
	
	private boolean checkCycle()
	{//0 - white , 1 - grey, 2 - black | true for cycle, false otherwise
		int [] pi = new int[v_num];
		int [] col = new int[v_num];
		int [] dis = new int[v_num];

		Queue<Vertex> q = new LinkedList<Vertex>();
		
		for(int i=1; i<v_num; i++)
		{
			col[i] = 0;
			dis[i] = 2147000000;
			pi[i] = 0;
		}
		
		col[1] = 1;
		dis[1] = 0;
		pi[1] = 0;
		
		q.add(vertices.get(0));
		
		while(!q.isEmpty())
		{
			Vertex tmp = q.poll();
			for(Vertex v : tmp.neighbours)
			{
				if(col[(v.name)] == 0)
				{
					col[(v.name)] = 1;
					dis[(v.name)] = dis[(tmp.name)] + 1;
					pi[(v.name)] = (tmp.name);
					q.add(v);
				}
				else if(col[(v.name)] == 1)
					return true;
			}
			col[(tmp.name)] = 2;
			
		}
		return false;
	}

	public List<List<Vertex>> Isomorphic(Graph other)
	{
		List<List<Vertex>> Mlst = new ArrayList<>();
		ArrayList<Vertex> M;

		if(this.equals(other))
		{
			for(Vertex v: vertices)
				for (Vertex w : other.vertices)
					if (areSimilar(v, w) && !v.possibleCandid.contains(w))
						v.possibleCandid.add(w);

			FlowNetwork fn = new FlowNetwork(vertices);
			M = fn.fordFulkerson();

			if(M.size() == vertices.size())
			{
				ArrayList<BipartiteGraph> Pb = new ArrayList<>();
				List<ArrayList<Vertex>> Pm = new ArrayList<>();

				Pb.add(new BipartiteGraph(vertices));
				Pm.add(M);
				Mlst.add(M);

				while(Pb.size() == Pm.size() && !Pb.isEmpty())
				{
					Edge e;
					BipartiteGraph B = Pb.get(Pb.size()-1);
					ArrayList<Vertex> next, match = Pm.get(Pm.size()-1);

					Pb.remove(Pb.size()-1);
					Pm.remove(Pm.size()-1);

					next = subProblem(B, match);

					if(next.size() == vertices.size())
					{
						BipartiteGraph B1 = new BipartiteGraph(B), B2 = new BipartiteGraph(B);

						Mlst.add(next);
						e = subtractEdges(match, next).get(0);

						B1.fixate(e);
						B2.edges.remove(e);

						Pb.add(B1);
						Pb.add(B2);

						Pm.add(match);
						Pm.add(next);
					}
				}
			}
		}

		return checkMatches(Mlst, other);
	}

	private ArrayList<Edge> subtractEdges(ArrayList<Vertex> m1, ArrayList<Vertex> m2) //returns edges in m1-m2
	{
		ArrayList<Edge> ed = new ArrayList<>();
		for(int i=0; i<m1.size(); i++)
			if(m1.get(i).name != m2.get(i).name)
				ed.add(new Edge(i, m1.get(i).name));

		return ed;
	}

	private ArrayList<Vertex> subProblem(BipartiteGraph b, ArrayList<Vertex> m)
	{
		ArrayList<Integer> cyc;
		ArrayList<Vertex> m2 = new ArrayList<>();

		BipartiteGraph b2 = new BipartiteGraph(b, m); //dirceted with m opposite

		cyc = b2.getCyc();
		if(cyc.size() > 0)
		{
			ArrayList<Edge> sd, ec = new ArrayList<>(), em = new ArrayList<>();
			ec.add(new Edge(cyc.get(cyc.size()-1), cyc.get(0)));

			for(int i=0; i<cyc.size()-1; i++)
				ec.add(new Edge(cyc.get(i), cyc.get(i+1)));

			for(int i=0; i<m.size(); i++)
				em.add(new Edge(i, m.get(i).name));

			sd = symmetricalDif(ec, em);
			Collections.sort(sd);

			for(Edge e: sd)
				m2.add(new Vertex(e.dest));
		}

		return m2;
	}

	public ArrayList<Edge> symmetricalDif(ArrayList<Edge> ec, ArrayList<Edge> em)
	{
		ArrayList<Edge> sd = new ArrayList<>();

		for(Edge e: em)
		{
			if(ec.contains(e) || ec.contains(new Edge(e.dest, e.source))) { }
			else sd.add(new Edge(e.source, e.dest));
		}

		for(Edge e: ec)
		{
			if(em.contains(e) || em.contains(new Edge(e.dest, e.source))) { }
			else sd.add(new Edge(e.source, e.dest));
		}

		return sd;
	}

	private boolean areSimilar(Vertex v, Vertex w)
	{
		ArrayList<Integer> lst1 = new ArrayList<>();//ordered neighbours degrees V
		ArrayList<Integer> lst2 = new ArrayList<>();//ordered neighbours degrees W
		ArrayList<Integer> lst3 = new ArrayList<>();//ordered neighbours triangles V
		ArrayList<Integer> lst4 = new ArrayList<>();//ordered neighbours squares  V
		ArrayList<Integer> lst5 = new ArrayList<>();//ordered neighbours triangles  W
		ArrayList<Integer> lst6 = new ArrayList<>();//ordered neighbours squares  W

		for(Vertex v1: v.neighbours)
		{
			lst1.add(v1.getRank());
			lst3.add(v1.triangles);
			lst4.add(v1.squares);
		}

		for(Vertex v2: w.neighbours)
		{
			lst2.add(v2.getRank());
			lst5.add(v2.triangles);
			lst6.add(v2.squares);
		}

		Collections.sort(lst1); Collections.sort(lst2);
		Collections.sort(lst3); Collections.sort(lst4);
		Collections.sort(lst5); Collections.sort(lst6);

		return (v.squares == w.squares &&
				v.triangles == w.triangles &&
				v.getRank() == w.getRank() &&
				lst1.equals(lst2) &&
				lst3.equals(lst5) &&
				lst4.equals(lst6));
	}

	private List<List<Vertex>> checkMatches(List<List<Vertex>> mlst, Graph other)
	{
		int viso, wiso;
		List<List<Vertex>> finlst = new ArrayList<>();

		for(List<Vertex> l: mlst)
			for(Vertex v: l)
				v.name -= l.size();

		for(List<Vertex> l: mlst)
		{
			boolean flag = true;
			for (Vertex v : vertices)
				for (Vertex w : vertices)
				{
					viso = l.get(v.name).name;
					wiso = l.get(w.name).name;

					if (v.neighbours.contains(w))
					{
						if (other.mat[viso][wiso] != 1)
							flag = false;
					}

					else if (other.mat[viso][wiso] == 1)
						flag = false;
				}

			if(flag)
				finlst.add(l);
		}
		return finlst;
	}

	private boolean isTree()
	{
		return (e_num == v_num - 1) && checkCycle();
	}

	 public boolean equals(Object other)
	{
		boolean result = false;
		if (other instanceof Graph)
		{
			Graph that = (Graph) other;
			result = (this.v_num == that.v_num &&
					  this.e_num == that.e_num &&
					  this.tri_num == that.tri_num &&
					  this.squ_num == that.squ_num);
		}
		return result;
	}

	private int countSquares()
	{
		int c=0, f;
		List<List<Integer>> sq = new ArrayList<>();
		for(Vertex v1: vertices)
			for(Vertex v2: v1.neighbours)
				for(Vertex v3: v2.neighbours)
					if(v3.name != v1.name)
						for(Vertex v4: v3.neighbours)
							if(v4.name != v2.name && v4.name != v1.name)
								if(v4.neighbours.contains(v1))
								{
									f = 0;
									ArrayList<Integer> l = new ArrayList<Integer>();
									ArrayList<Integer> lr = new ArrayList<Integer>();
									l.add(v1.name); l.add(v2.name); l.add(v3.name); l.add(v4.name);
									lr.add(v4.name); lr.add(v3.name); lr.add(v2.name); lr.add(v1.name);
									for(int i=0; i<l.size(); i++)
									{
										if (sq.contains(l) || sq.contains(lr)) { f = 1; }
										l = rotate(l);
										lr = rotate(lr);
									}
									if(f == 0)
									{
										sq.add(l);
										c++;
										for(Vertex v: vertices)
											if(v.name == v1.name || v.name == v2.name || v.name == v3.name || v.name == v4.name)
												v.squares++;
									}
								}
		return c;
	}

	private ArrayList<Integer> rotate(ArrayList<Integer> L)
	{
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		tmp.add(L.get(L.size()-1));
		for(int i=0; i<L.size()-1; i++)
			tmp.add(L.get(i));
		return tmp;
	}
}
