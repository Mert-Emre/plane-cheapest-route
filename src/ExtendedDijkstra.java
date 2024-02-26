// More detailed implementation of dijkstra. Parking is allowed. 
// Time passes between flight so the weathers and the costs of flying changes dynamically.

import java.util.HashMap;
import java.util.Locale;
import java.util.PriorityQueue;

public class ExtendedDijkstra {
  private HashMap<String, Airfield> airfields;
  private HashMap<String, Airport> airports;
  private HashMap<String, Double> costs;
  private int plane;

  public ExtendedDijkstra(HashMap<String, Airfield> airfields, HashMap<String, Airport> airports, int plane) {
    this.airfields = airfields;
    this.airports = airports;
    this.plane = plane;
    costs = new HashMap<>();
  }

  public String calculate(String from, String to, int time, int deadline) {
    PriorityQueue<VertexExtended> queue = new PriorityQueue<>();
    VertexExtended destinationVertex = null;

    // Cost to reaching an airport is again set to infinity at first but with a
    // difference. This time costs are saved with a time. This way algorithm saves
    // the cheapest path to a given node at an instant of a time.
    for (Airport airport : airports.values()) {
      costs.put(airport.getCode() + time, Double.MAX_VALUE);
    }
    VertexExtended source = new VertexExtended(airports.get(from), 0, time, null);
    queue.add(source);
    costs.put(source.airport.getCode() + time, 0.0);

    while (!queue.isEmpty()) {
      // Paths that violates the deadline are not added to the heap. But this check is
      // added to guarantee that we don't calculate the paths that violates the
      // deadline.
      if (queue.peek().time > deadline) {
        queue.poll();
        continue;
      }
      VertexExtended currentVertex = queue.poll();
      Airport current = currentVertex.airport;
      if (current.getCode().equals(to)) {
        destinationVertex = currentVertex;
        break;
      }
      // Like in the simple dijkstra, calculate the cost to flying to a
      // neihgbour. This time we don't have a visited hashset. This makes cycles
      // possible. Instead for a given airport if we can reach to this airport at a
      // specific time, cheaper than the current the cheapest cost of reaching to this
      // airport at the same specific time then we need to use this new path instead
      // of the old one.
      for (int i = 0; i < current.totalNeighbours(); i++) {
        Airport neighbour = airports.get(current.getNextNeighbour());

        double distance = calcDistance(current, neighbour);
        int timePassed = calculateTime(distance, plane);
        double cost = costs.get(current.getCode() + currentVertex.time)
            + calculateCost(current, neighbour, currentVertex.time, currentVertex.time + timePassed, distance);
        if (currentVertex.time + timePassed <= deadline
            && cost < costs.getOrDefault(neighbour.getCode() + (currentVertex.time + timePassed), Double.MAX_VALUE)) {
          costs.put(neighbour.getCode() + (currentVertex.time + timePassed), cost);
          queue.add(new VertexExtended(neighbour, cost, currentVertex.time + timePassed, currentVertex));
        }
      }
      // In addition to flying to the neighbours of an airport, a plane can also park
      // at the current airport. Add this path to the queue.
      if (currentVertex.time + 6 * 60 * 60 <= deadline
          && currentVertex.cost + current.getParkCost() < costs
              .getOrDefault(current.getCode() + (currentVertex.time + 6 * 60 * 60), Double.MAX_VALUE)) {
        queue.add(
            new VertexExtended(current, currentVertex.cost + current.getParkCost(),
                currentVertex.time + 6 * 60 * 60, currentVertex));
        costs.put(current.getCode() + (currentVertex.time + 6 * 60 * 60), currentVertex.cost + current.getParkCost());
      }
    }

    // If queue becomes empty before reaching to the destination airport, this means
    // that there does not exist a path such that does not violate the deadline
    // constraint.
    String path = "";
    if (destinationVertex == null) {
      return "No possible solution.";
    }
    if (destinationVertex != null) {
      path = String.format(Locale.US, "%.5f",
          destinationVertex.cost);
    }
    // If the parent of a node is itself, this means that the plane parked at this
    // airport.
    while (destinationVertex.parent != null) {
      if (destinationVertex.parent.airport != destinationVertex.airport) {
        path = destinationVertex.airport.getCode() + " " + path;
      } else {
        path = "PARK " + path;
      }
      destinationVertex = destinationVertex.parent;
    }
    path = destinationVertex.airport.getCode() + " " + path;
    return path;
  }

  private double calculateCost(Airport from, Airport to, int from_time, int to_time, double distance) {
    double fromWeather = airfields.get(from.getAirfield().getName()).getWeather(from_time);
    double toWeather = airfields.get(to.getAirfield().getName()).getWeather(to_time);
    return 300 * fromWeather * toWeather + distance;
  }

  private double calcDistance(Airport from, Airport to) {
    double part_1 = Math.pow(Math.sin(radians((to.getLatitude() - from.getLatitude()) / 2)), 2);
    double part_2 = Math.cos(radians(from.getLatitude())) * Math.cos(radians(to.getLatitude()));
    double part_3 = Math.pow(Math.sin(radians((to.getLongitude() - from.getLongitude()) / 2)), 2);
    double distance = 2 * 6371 * (Math.asin(Math.sqrt(part_1 + part_2 * part_3)));
    return distance;
  }

  // plane == 0 => Carreiedas
  // plane == 1 => Orion 3
  // plane == 2 => Skyfleet
  // plane == 3 => T-16 Skyhopper
  private int calculateTime(double distance, int plane) {
    if (plane == 0) {
      if (distance <= 175) {
        return 6 * 60 * 60;
      } else if (distance <= 350) {
        return 12 * 60 * 60;
      }
      return 18 * 60 * 60;
    } else if (plane == 1) {
      if (distance <= 1500) {
        return 6 * 60 * 60;
      } else if (distance <= 3000) {
        return 12 * 60 * 60;
      }
      return 18 * 60 * 60;
    } else if (plane == 2) {
      if (distance <= 500) {
        return 6 * 60 * 60;
      } else if (distance <= 1000) {
        return 12 * 60 * 60;
      }
      return 18 * 60 * 60;
    } else if (plane == 3) {
      if (distance <= 2500) {
        return 6 * 60 * 60;
      } else if (distance <= 5000) {
        return 12 * 60 * 60;
      }
      return 18 * 60 * 60;
    }
    return Integer.MAX_VALUE;
  }

  private double radians(double degree) {
    return degree * Math.PI / 180;
  }
}
