// A basic implementation of the dijkstra algorithm for task1.
// It calculates the cheapest path from a source airport to destination airport.
// It is assumed that time is the same between all flights and parking is not allowed.

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Dijkstra {
  private HashMap<String, Airfield> airfields;
  private HashMap<String, Airport> airports;
  private HashMap<Airport, Airport> parents;
  private HashMap<Airport, Double> costs;

  public Dijkstra(HashMap<String, Airfield> airfields, HashMap<String, Airport> airports) {
    this.airfields = airfields;
    this.airports = airports;
    parents = new HashMap<>();
    costs = new HashMap<>();
  }

  // Use a heap to get the cheapest path at each step. Thus at the end when we
  // reach to the destination airport it is the cheapest to the destinaton
  // airport.
  public String calculate(String from, String to, int time) {
    HashSet<String> visited = new HashSet<>();
    PriorityQueue<Vertex> queue = new PriorityQueue<>();

    // At start assign the cost of reaching to any airport to infinity.
    for (Airport airport : airports.values()) {
      costs.put(airport, Double.MAX_VALUE);
    }
    // Parents hashmap is stored. Because at the end we both want the cost and the
    // total path that made us enable to reach destination airport. Parents hashmap
    // is read backwards from the destination airport to the source airport. Thus
    // the path is obtained.
    queue.add(new Vertex(airports.get(from), 0));
    parents.put(airports.get(from), null);
    costs.put(airports.get(from), 0.0);

    while (visited.size() < airports.size()) {
      if (queue.isEmpty())
        break;
      // If destination airport is in the visited hashset then we don't need to
      // calculate further. Because cheapest path to the destination airport is
      // obtained.
      if (visited.contains(to)) {
        break;
      }
      Airport current = queue.poll().airport;
      // If a node is already visited this prevents calculating the paths from this
      // node. If a node is visited already, even if the current cheapest path from
      // the heap is to this node, this path can't be the cheapest path to this node.
      if (visited.contains(current.getCode()))
        continue;

      visited.add(current.getCode());
      // Calculate the cost of flying from the current node to the neighbours of the
      // current node if a neighbour is not already visited.
      for (int i = 0; i < current.totalNeighbours(); i++) {
        Airport neighbour = airports.get(current.getNextNeighbour());
        if (!visited.contains(neighbour.getCode())) {
          double cost = costs.get(current) + calculateCost(current, neighbour, time);
          if (cost < costs.get(neighbour)) {
            costs.put(neighbour, cost);
            parents.put(neighbour, current);
          }
          queue.add(new Vertex(neighbour, cost));
        }
      }
    }

    String path = "";
    Airport destination = airports.get(to);
    while (!destination.getCode().equals(from)) {
      path = destination.getCode() + " " + path;
      destination = parents.get(destination);
    }
    path = destination.getCode() + " " + path;
    return path;
  }

  public double getCost(String dest) {
    return costs.get(airports.get(dest));
  }

  // This function is an implementation of the cost function given in the project
  // explanation.
  private double calculateCost(Airport from, Airport to, int time) {
    double fromWeather = airfields.get(from.getAirfield().getName()).getWeather(time);
    double toWeather = airfields.get(to.getAirfield().getName()).getWeather(time);
    double part_1 = Math.pow(Math.sin(radians((to.getLatitude() - from.getLatitude()) / 2)), 2);
    double part_2 = Math.cos(radians(from.getLatitude())) * Math.cos(radians(to.getLatitude()));
    double part_3 = Math.pow(Math.sin(radians((to.getLongitude() - from.getLongitude()) / 2)), 2);
    double distance = 2 * 6371 * Math.asin(Math.sqrt(part_1 + part_2 * part_3));
    return 300 * fromWeather * toWeather + distance;
  }

  private double radians(double degree) {
    return degree * Math.PI / 180;
  }
}
