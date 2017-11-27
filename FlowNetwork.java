import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class FlowNetwork
{
    public Vertex s, t;
    public ArrayList<Vertex> L = new ArrayList<Vertex>();
    public ArrayList<Vertex> R = new ArrayList<Vertex>();
    public ArrayList<FlowEdge> edges = new ArrayList<FlowEdge>();

    public FlowNetwork(ArrayList<Vertex> cons)
    {
        s = new Vertex(-1); // create and add S - universal source
        t = new Vertex(-2); // create and add T - universal target
        for(Vertex v: cons)
        {
            L.add(new Vertex(v.name));
            R.add(new Vertex(v.name + cons.size()));

            for(Vertex v2: v.possibleCandid) { edges.add(new FlowEdge(v.name, v2.name + cons.size(), 999)); }

        }
        for(Vertex v: L){ edges.add(new FlowEdge(-1, v.name, 1)); }
        for(Vertex v: R){ edges.add(new FlowEdge(v.name, -2, 1)); }
        return;
    }

    public ArrayList<Vertex> fordFulkerson()
    {
        ArrayList<Vertex> p = flowBFS(), fv = new ArrayList<>();
        int n = 1000;

        while(p.size() > 0) {
            for (int i = 0; i < p.size() - 1; i++) {
                for (FlowEdge fe : edges)
                    if (fe.source == p.get(i).name && fe.dest == p.get(i + 1).name)
                        n = Integer.min(n, fe.resCapacity);
            }

            for (int i = 0; i < p.size() - 1; i++)
            {
                for (FlowEdge fe : edges)
                    if (fe.source == p.get(i).name && fe.dest == p.get(i + 1).name)
                        fe.putFlow(n);

            }
            p = flowBFS();
        }

        for(FlowEdge fe: edges)
        { if(fe.source!=-1 && fe.dest!=-2 && fe.flow>0) fv.add(new Vertex(fe.dest)); }

        for(Vertex v: fv) { v.name -= fv.size(); }

        return fv;
    }

    private ArrayList<Vertex> flowBFS()
    {
        int v_num = L.size() + R.size() + 2;
        int [] pi = new int[v_num];
        int [] col = new int[v_num];
        int [] dis = new int[v_num];
        Queue<Vertex> q = new LinkedList<Vertex>();
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<Vertex> path = new ArrayList<>();

        vertices.add(new Vertex(t));
        vertices.add(new Vertex(s));

        for(Vertex v: L) { vertices.add(new Vertex(v)); }
        for(Vertex v: R) { vertices.add(new Vertex(v)); }

        for(Vertex v: vertices) { v.name +=2; }

        for(int i=0; i<v_num; i++)
        {
            col[i] = 0;
            dis[i] = 2147000000;
            pi[i] = 0;
        }

        col[1] = 1;
        dis[1] = 0;
        pi[1] = 0;

        q.add(vertices.get(1));

        while(!q.isEmpty())
        {
            Vertex tmp = q.poll();

            for(FlowEdge fe: edges)
            {
                if(fe.source+2 == tmp.name && fe.resCapacity > 0 && col[(fe.dest + 2)] == 0)
                {
                    col[(fe.dest + 2)] = 1;
                    dis[(fe.dest + 2)] = dis[(tmp.name)] + 1;
                    pi[(fe.dest + 2)] = (tmp.name);
                    q.add(vertices.get(fe.dest + 2));
                }
            }
            col[(tmp.name)] = 2;
        }

        if(col[0] == 2)
        {
            Vertex v = vertices.get(0);
            path.add(new Vertex(v));
            path.get(path.size()-1).name -= 2;
            while(v.name != 1)
            {
                v = vertices.get(pi[v.name]);
                path.add(new Vertex(v));
                path.get(path.size()-1).name -= 2;
            }
            Collections.reverse(path);
        }

        return path;
    }
}
