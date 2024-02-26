
// The purpose of this file is to store whe weather conditions for a given airfield.
import java.util.ArrayList;
import java.util.Collections;

public class Airfield {
  private ArrayList<Weather> weather = new ArrayList<>();
  private String name;

  public Airfield(String name, ArrayList<Weather> weather) {
    this.name = name;
    this.weather = weather;
    Collections.sort(weather);
  }

  public double getWeather(int time) {
    Weather current = weather.get(0);
    for (int i = 0; i < weather.size(); i++) {
      if (i < weather.size() - 1 && time >= weather.get(i + 1).getTime()) {
        continue;
      }
      current = weather.get(i);
      break;
    }
    return current.getWeather();
  }

  public String getName() {
    return name;
  }
}
