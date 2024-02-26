// The purpose of this class is to store the weather for a given time. 
// This does not contain any reference to airfield because airfield will store an arraylist of weathers.
// This class also calculates the weather constant for a given weather.
public class Weather implements Comparable<Weather> {
  private int time;
  private double weather;

  public Weather(int time, int weather) {
    this.time = time;
    this.weather = calculateWeather(weather);
  }

  private double calculateWeather(int weather) {
    String bin = Integer.toBinaryString(weather);
    while (bin.length() < 5) {
      bin = "0" + bin;
    }
    int bb = Character.getNumericValue(bin.charAt(bin.length() - 1));
    int bh = Character.getNumericValue(bin.charAt(bin.length() - 2));
    int bs = Character.getNumericValue(bin.charAt(bin.length() - 3));
    int br = Character.getNumericValue(bin.charAt(bin.length() - 4));
    int bw = Character.getNumericValue(bin.charAt(bin.length() - 5));

    return (bw * 1.05 + 1 - bw) * (br * 1.05 + 1 - br) * (bs * 1.1 + 1 - bs) * (bh * 1.15 + 1 - bh)
        * (bb * 1.2 + 1 - bb);
  }

  public int getTime() {
    return time;
  }

  public double getWeather() {
    return weather;
  }

  public int compareTo(Weather other) {
    if (time < other.time)
      return -1;
    else if (time > other.time)
      return 1;
    return 0;
  }
}
