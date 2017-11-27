public class FlowEdge extends Edge
{
    public int flow;
    public int maxCapacity;
    public int resCapacity;

    public FlowEdge(int s, int d, int cap)
    {
        super(s, d);
        maxCapacity = cap;
        flow = 0;
        resCapacity = maxCapacity - flow;
    }

    public void putFlow(int f)
    {
        flow += f;
        resCapacity -= f;
    }
}
