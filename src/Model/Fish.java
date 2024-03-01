package Model;

public class Fish {
  private final String name;
  private final double weight;
  private final int worth;

  public Fish(String name, double weight, int worth) {
    this.name = name;
    this.weight = weight;
    this.worth = worth;
  }

  public double getWeight() {
    return this.weight;
  }

  public String getName() {
    return this.name;
  }

  public int getWorth() {
    return this.worth;
  }
}