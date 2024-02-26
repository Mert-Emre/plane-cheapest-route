// This is an extension of vertex class. It also stores the times thus used in the task2.
public class VertexExtended extends Vertex {
  public int time;
  public VertexExtended parent;

  public VertexExtended(Airport airport, double cost, int time, VertexExtended parent) {
    super(airport, cost);
    this.time = time;
    this.parent = parent;
  }
}
