import java.util.*;
import java.lang.Math;

public class Tree extends Graph {

    private int height;
    private long id;


    public Tree(Graph g)
    {
        super(g);
        height = 0;
        ArrayList<Vertex> t = findCenter();
        id = getNewHash();
        prepareRoot();   //innitalize vertices
        reRoot(t.get(0));
    }

    public void prepareRoot()
    {
        for(Vertex v: vertices)
        {
            v.depth = 0;
            v.parent = null;
            v.children = new ArrayList<Vertex>();
        }
    }

    public void reRoot(Vertex r)
    {
        for(Vertex v : r.neighbours)
        {
            if(!v.children.contains(r))
                v.parent = r;

            if(r.parent != v)
                    r.children.add(v);
        }

        if(r.parent != null) r.depth = r.parent.depth + 1;

        height = Math.max(r.depth, height);

        if(!r.children.isEmpty())
            for(Vertex v : r.children)
                reRoot(v);
    }

    private ArrayList<Vertex> findCenter()
    {
        int r = v_num;
        int [] cd = new int[v_num];
        ArrayList<Vertex> s = new ArrayList<Vertex>();
        ArrayList<Vertex> t;

        for(Vertex v : vertices)
        {
            cd[v.name] = v.getRank();
            if(cd[v.name] <= 1)
            {
                s.add(v);
                r--;
            }
        }

        while(r > 0)
        {
            t = new ArrayList<Vertex>();
            for(Vertex v : s)
                for(Vertex w : v.neighbours)
                {
                    cd[(w.name)] = cd[(w.name)] - 1;
                    if(cd[(w.name)] == 1)
                    {
                        t.add(w);
                        r--;
                    }
                }

            s.clear();
            for(Vertex v: t)
                s.add(v);
        }

        return s;
    }

    public List<List<Vertex>> Isomorphic(Tree other)
    {
        List<List<Vertex>> M = new ArrayList<>();
        ArrayList<Vertex> c1 = this.findCenter();
        ArrayList<Vertex> c2 = other.findCenter();

        if(this.e_num == other.e_num && this.v_num == other.v_num)
        {
            this.prepareRoot();
            this.reRoot(c1.get(0));
            other.prepareRoot();
            other.reRoot(c2.get(0));

            M = rootedIsomorphic(other);
            if(M.isEmpty() && c2.size() > 1)
            {
                other.prepareRoot();
                other.reRoot(c2.get(1));
                M = rootedIsomorphic(other);
            }
        }

        return M;
    }

    private List<List<Vertex>> rootedIsomorphic(Tree other)
    {
        int h = Math.max(this.height, other.height);
        List<List<Vertex>> M = new ArrayList<>();
        List<List<Vertex>> L = new ArrayList<>();

        for(int i = 0; i <= h; i++)
        {
            L.add(new ArrayList<Vertex>());
            for (Vertex v1 : this.vertices) if (v1.depth == i) L.get(i).add(v1);
            for (Vertex v2 : other.vertices) if (v2.depth == i) L.get(i).add(v2);
        }

        for(int i = 0; i < L.size(); i++)
            for(Vertex v: L.get(i))
            {
                v.label = 0;
                v.orderedlabels = new ArrayList<Integer>();
                v.orderedchildren = new ArrayList<Vertex>();
            }

        for(int i = h-1; i >= 0; i--)
        {
            for(int j = 0; j <= L.get(i+1).size()-1; j++)
            {
                Vertex v = L.get(i+1).get(j);
                v.parent.orderedlabels.add(v.label);
                v.parent.orderedchildren.add(v);
                Collections.sort(v.parent.orderedlabels);
                v.parent.orderedchildren.sort(Comparator.comparing(Vertex::getLabel));
            }

            L.set(i, sortVertices(L.get(i)));

            for(Vertex v: L.get(i))
                v.label = indexOfLabel(L.get(i), v);

        }

        if(L.get(0).get(0).label == L.get(0).get(1).label)
        {
           generateMapping(L.get(0).get(0), L.get(0).get(1), M);
        }

        return M;
    }

    private int indexOfLabel(List<Vertex> L, Vertex v)
    {
        int c = 0, i;
        for(i = 0; i<L.size(); i++)
        {
            if(i>0)
                if(cmporderedLabels(L.get(i), L.get(i-1)) == 0)
                    c++;

            if(cmporderedLabels(L.get(i), v) == 0)
                break;
        }
        return i-c;
    }

    private List<Vertex> sortVertices(List<Vertex> verts)
    {
        int n = verts.size();
        Vertex temp;

        for(int i = 0; i < n; i++)
            for (int j = 1; j < (n - i); j++)
                if (cmporderedLabels(verts.get(j - 1), verts.get(j)) == 1)
                {
                    temp = verts.get(j - 1);
                    verts.set(j - 1, verts.get(j));
                    verts.set(j, temp);
                }

        return verts;
    }

    //compares label lists, returns 1 if v1 bigger, -1 if v2 bigger, 0 if equal
    private int cmporderedLabels(Vertex v1, Vertex v2)
    {
        for(int i = 0; i < Math.min(v1.orderedlabels.size(), v2.orderedlabels.size()); i++)
        {
            if(v1.orderedlabels.get(i) > v2.orderedlabels.get(i)) return 1;
            if(v1.orderedlabels.get(i) < v2.orderedlabels.get(i)) return -1;
        }

        if(v2.orderedlabels.size() > v1.orderedlabels.size()) return -1;
        if(v2.orderedlabels.size() < v1.orderedlabels.size()) return 1;
        return 0;
    }

    private void generateMapping(Vertex v, Vertex w, List<List<Vertex>> M)
    {
        ArrayList<Vertex> pair = new ArrayList<Vertex>();
        pair.add(v);
        pair.add(w);
        M.add(pair);
        for(int i = 0; i<v.orderedchildren.size(); i++)
        {
            Vertex x = new Vertex(v.orderedchildren.get(i));
            Vertex y = new Vertex(w.orderedchildren.get(i));
            generateMapping(x, y, M);
        }
    }


    public long getNewHash()
    {
        long ID;
        Random r = new Random();
        ID = (r.nextInt(2147000000)+1);
        ID = ((Object)ID).hashCode() + System.currentTimeMillis();
        ID = Math.abs((long)((Object)ID).hashCode());
        return ID;
    }

    public void print()
    {
        System.out.println("-------------------------------------------------");
        System.out.println("Tree info:");
       // System.out.println("Tree ID HASH: " + this.id + "\n");
        System.out.println(this.path);
        for(int i = 0; i < v_num; i++)
        {
            for(int j = 0; j < v_num; j++)
            {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Number of verticies: " + v_num);
        System.out.println("Number of edges: " + e_num);
        System.out.println("Tree Height: " + height);
        System.out.print("Vertices and ranks: ");
        for(Vertex v : vertices)
        {
            v.print();
        }
        System.out.println();
        System.out.println("Triangles in graph: " + tri_num);
        System.out.println("Squares in graph: " + squ_num);
        System.out.println("Symmetric: " + symmetric);
        System.out.println("Anti-reflexive: " + anti_reflex);
        System.out.println("Tree: " + tree);

        System.out.println("-------------------------------------------------");

    }
}
