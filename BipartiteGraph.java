import java.util.*;

public class BipartiteGraph
{
    private int vnum;
    private List<ArrayList<Integer>> cl = new ArrayList<>();
    public ArrayList<Vertex> L = new ArrayList<Vertex>();
    public ArrayList<Vertex> R = new ArrayList<Vertex>();
    public ArrayList<Edge> edges = new ArrayList<Edge>();
    public ArrayList<Vertex> vertices = new ArrayList<>();

    public BipartiteGraph(ArrayList<Vertex> cons)
    {
        for(Vertex v: cons)
        {
            L.add(new Vertex(v.name));
            R.add(new Vertex(v.name + cons.size()));

            for(Vertex v2: v.possibleCandid) { edges.add(new Edge(v.name, v2.name + cons.size())); }
        }

        for(Vertex v: L) { vertices.add(new Vertex(v)); }
        for(Vertex v: R) { vertices.add(new Vertex(v)); }
        vnum = vertices.size();
    }

    public BipartiteGraph(BipartiteGraph b, ArrayList<Vertex> match)
    {
        for(Vertex v: match) if(v.name < match.size()) v.name += match.size();

        for(Vertex v: b.L)
            L.add(new Vertex(v.name));

        for(Vertex v: b.R)
            R.add(new Vertex(v.name));

        for(Edge e: b.edges)
            edges.add(new Edge(e.source, e.dest));

        for(Edge e: edges)
            for (int i = 0; i < match.size(); i++)
                if (e.source == i && e.dest == match.get(i).name)
                {
                    int n = e.source;
                    e.source = e.dest;
                    e.dest = n;
                }

        for(Vertex v: L) { vertices.add(new Vertex(v)); }
        for(Vertex v: R) { vertices.add(new Vertex(v)); }
        vnum = vertices.size();
    }

    public BipartiteGraph(BipartiteGraph other)
    {
        for(Vertex v: other.L)
            L.add(new Vertex(v.name));

        for(Vertex v: other.R)
            R.add(new Vertex(v.name));

        for(Edge e: other.edges)
            edges.add(new Edge(e.source, e.dest));

        for(Vertex v: L) { vertices.add(new Vertex(v)); }
        for(Vertex v: R) { vertices.add(new Vertex(v)); }
        vnum = vertices.size();
    }

    public void fixate(Edge e)
    {
        ArrayList<Edge> nes = new ArrayList<>();

        for(Edge ed: edges)
        {
            if(ed.source == e.source || ed.dest == e.dest) {}

            else nes.add(new Edge(ed.source, ed.dest));
        }

        nes.add(e);
        edges = nes;
    }

    public ArrayList<Integer> getCyc()
    {
        cl = new ArrayList<>();
        int [] col = new int[vnum];
        int [] pi = new int[vnum];

        for(int i=0; i<vnum; i++)
        {
            col[i] = 0;
            pi[i] = -1;
        }

        for(Vertex v: vertices)
            if (col[v.name] == 0)
            {
                col[v.name] = 1;
                for (Edge e : edges)
                    if (e.source == v.name && col[e.dest] == 0)
                        DFSVisit(v, vertices.get(e.dest), col, pi);

                col[v.name] = 2;
            }

        for(ArrayList lst: cl)
            if (!lst.isEmpty())
                return lst;

        return new ArrayList<>();
    }

    private void DFSVisit(Vertex s, Vertex d, int[] col, int[] pi)
    {
        ArrayList<Integer> c = new ArrayList<>();
        col[d.name] = 1;
        pi[d.name] = s.name;

        for (Edge e : edges) {
            if (e.source == d.name && col[e.dest] == 0)
                DFSVisit(d, vertices.get(e.dest), col, pi);

            else if (e.source == d.name && col[e.dest] == 1)
            {
                Vertex z = vertices.get(e.source);
                c.add(e.dest);
                while (z.name != e.dest && pi[z.name] >= 0)
                {
                    c.add(z.name);
                    z = vertices.get(pi[z.name]);
                }
                Collections.reverse(c);
                cl.add(c);
            }

            col[d.name] = 2;
        }
    }
}
