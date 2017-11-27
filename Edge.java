public class Edge implements Comparable<Edge>
{
    public int source;
    public int dest;

    public Edge(int s, int d) { source = s; dest = d;}

    @Override
    public boolean equals(Object object)
    {
        if (object != null && object instanceof Edge)
            return (this.source == ((Edge) object).source) && (this.dest == ((Edge) object).dest);

        return false;
    }

    public int compareTo(Edge cmp)
    {
        //ascending order
        return this.source - ((Edge) cmp).source;
    }
}
