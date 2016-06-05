package nl.defsoftware.mrgb.view.models;
public class Edge {

    protected int capacity;
    protected int lower;
    protected int flow;
    protected NodeLabel from;
    protected NodeLabel to;

    // used to construct the backwards edge
    private Edge(Edge e) {
        this.flow = e.getCapacity();// saturating this edge, so that we don't consider it in a path.
        this.capacity = e.getCapacity();
        this.lower = e.getLowerCapacity();
        this.from = e.getTo();
        this.to = e.getFrom();
    }

    protected Edge(int capacity, NodeLabel from, NodeLabel to) {
        this.capacity = capacity;
        this.from = from;
        this.to = to;
        this.flow = 0;
    }

    protected Edge(int lower, int upper, NodeLabel from, NodeLabel to) {
        this.lower = lower;
        this.capacity = upper;
        this.from = from;
        this.to = to;
        this.flow = 0;
    }

    /**
     * This increases the flow for this edge and sets the flow for the backwards (could be the residual) edge.
     * 
     * @param add
     */
    public void augmentFlow(int add) {

        assert (flow + add <= capacity);
        flow += add;
    }

    public int getLowerCapacity() {
        return lower;
    }

    public void setLowerCapacity(int lower) {
        this.lower = lower;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFlow() {
        return flow;
    }

    public NodeLabel getFrom() {
        return from;
    }

    public int getResidual() {
        return capacity - flow;
    }

    public NodeLabel getTo() {
        return to;
    }

    protected void setCapacity(int upper) {
        this.capacity = upper;
    }
}