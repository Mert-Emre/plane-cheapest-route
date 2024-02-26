// The purpose of this class is to store the cost of reaching an airport. It doesn't store times and used just for task1.
public class Vertex implements Comparable<Vertex> {
  public double cost;
  public Airport airport;

  protected Vertex() {
    cost = Integer.MAX_VALUE;
    airport = null;
  }

  public Vertex(Airport airport, double cost) {
    this.cost = cost;
    this.airport = airport;
  }

  public int compareTo(Vertex other) {
    if (cost < other.cost)
      return -1;
    if (cost > other.cost)
      return 1;
    return 0;
  }
}
