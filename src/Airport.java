// The purpose of this file is to store the location, parking cost and neighbours of a given airfield.

import java.util.ArrayList;

public class Airport {
  private double latitude;
  private double longitude;
  private String code;
  private Airfield field;
  private int parkCost;
  private ArrayList<String> neighbours;
  private int lastNeighbour = -1;

  public Airport(String code, Airfield field, double latitude, double longitude, int parkCost) {
    this.code = code;
    this.field = field;
    this.latitude = latitude;
    this.longitude = longitude;
    this.parkCost = parkCost;
    neighbours = new ArrayList<>();
  }

  public void addDirection(String neighbour) {
    neighbours.add(neighbour);
  }

  public String getCode() {
    return code;
  }

  public Airfield getAirfield() {
    return field;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public double getLongitude() {
    return this.longitude;
  }

  // This is like an iterator for neighbours. The purpose is ensuring that the
  // neighbours of an airport is private and not changed by accident.
  public String getNextNeighbour() {
    lastNeighbour = lastNeighbour + 1 < neighbours.size() ? lastNeighbour + 1 : 0;
    return neighbours.get(lastNeighbour);
  }

  public int totalNeighbours() {
    return neighbours.size();
  }

  public int getParkCost() {
    return parkCost;
  }
}
