
// The task of this file is to read the input files and pass the information in these files to the path calculator functions.
//These calculator functions return the paths. The Main file writes these paths to the output files.
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Locale;

public class Main {
  public static void main(String[] args) {
    try {
      // Read weather file
      WriteHelper task_1_writer = new WriteHelper(args[4]);
      WriteHelper task_2_writer = new WriteHelper(args[5]);
      File weatherF = new File(args[2]);
      Scanner weatherReader = new Scanner(weatherF);
      HashMap<String, Airfield> airfields = new HashMap<>();
      HashMap<String, ArrayList<Weather>> weathers = new HashMap<>();

      if (weatherReader.hasNextLine())
        weatherReader.nextLine();

      while (weatherReader.hasNextLine()) {
        String[] weatherRaw = weatherReader.nextLine().trim().split(",");
        String airfield = weatherRaw[0];
        int time = Integer.parseInt(weatherRaw[1]);
        int weather = Integer.parseInt(weatherRaw[2]);
        if (!weathers.containsKey(airfield)) {
          weathers.put(airfield, new ArrayList<Weather>());
        }
        weathers.get(airfield).add(new Weather(time, weather));
      }

      for (String field : weathers.keySet()) {
        airfields.put(field, new Airfield(field, weathers.get(field)));
      }
      weatherReader.close();

      // Read airport file
      File airportF = new File(args[0]);
      Scanner airportReader = new Scanner(airportF);
      HashMap<String, Airport> airports = new HashMap<>();
      if (airportReader.hasNextLine())
        airportReader.nextLine();
      while (airportReader.hasNextLine()) {
        String[] airportRaw = airportReader.nextLine().trim().split(",");
        String airportCode = airportRaw[0];
        String airfield = airportRaw[1];
        double latitude = Double.parseDouble(airportRaw[2]);
        double longitude = Double.parseDouble(airportRaw[3]);
        int parkCost = Integer.parseInt(airportRaw[4]);
        airports.put(airportCode, new Airport(airportCode, airfields.get(airfield), latitude, longitude, parkCost));
      }
      airportReader.close();

      // Read directions file
      File directionsF = new File(args[1]);
      Scanner directionReader = new Scanner(directionsF);
      if (directionReader.hasNextLine())
        directionReader.nextLine();
      while (directionReader.hasNextLine()) {
        String[] directionRaw = directionReader.nextLine().trim().split(",");
        String from = directionRaw[0];
        String to = directionRaw[1];
        airports.get(from).addDirection(to);
      }
      directionReader.close();

      // Read missions file
      File missionsF = new File(args[3]);
      Scanner firstMission = new Scanner(missionsF);
      Scanner secondMission = new Scanner(missionsF);

      if (firstMission.hasNextLine()) {
        firstMission.nextLine().trim();
      }

      while (firstMission.hasNextLine()) {
        String[] missionRaw = firstMission.nextLine().trim().split(" ");
        String from = missionRaw[0];
        String to = missionRaw[1];
        int begin = Integer.parseInt(missionRaw[2]);
        Dijkstra dijkstra = new Dijkstra(airfields, airports);
        task_1_writer
            .write(dijkstra.calculate(from, to, begin) + String.format(Locale.US, "%.5f", dijkstra.getCost(to)) + "\n");
      }
      task_1_writer.close();
      firstMission.close();

      int plane = 0;
      if (secondMission.hasNextLine()) {
        String planeString = secondMission.nextLine().trim();
        if (planeString.equals("Carreidas 160")) {
          plane = 0;
        } else if (planeString.equals("Orion III")) {
          plane = 1;
        } else if (planeString.equals("Skyfleet S570")) {
          plane = 2;
        } else if (planeString.equals("T-16 Skyhopper")) {
          plane = 3;
        }
      }

      while (secondMission.hasNextLine()) {
        String[] missionRaw = secondMission.nextLine().trim().split(" ");
        String from = missionRaw[0];
        String to = missionRaw[1];
        int begin = Integer.parseInt(missionRaw[2]);
        int deadline = Integer.parseInt(missionRaw[3]);
        ExtendedDijkstra extDijkstra = new ExtendedDijkstra(airfields, airports, plane);
        task_2_writer
            .write(extDijkstra.calculate(from, to, begin, deadline) + "\n");
      }
      secondMission.close();
      task_2_writer.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found.");
    }
  }
}
