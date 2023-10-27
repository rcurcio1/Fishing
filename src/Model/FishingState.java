package Model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import Json.JsonState;
import SpeciesInfo.FishLocations;

public class FishingState {
    private int money;
    private String location;
    private int rodLevel;
    private List<Fish> inventory;
    private Water waterType;
    private Lure lure;
    private int lureDuration;
    private Map<String, Map<Water, Map<String, Boolean>>> almanac;

    public FishingState(FishLocations fl) {
        this.money = 0;
        this.location = "Newtown Square";
        this.rodLevel = 1;
        this.inventory = new ArrayList<Fish>();
        this.waterType = Water.CREEK;
        this.almanac = fl.makeAlmanac();
        this.lure = Lure.NO_LURE;
        this.lureDuration = 0;
    }

    public FishingState(FishLocations fl, String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));
        JsonState js = gson.fromJson(reader, JsonState.class);
        this.money = js.money;
        this.location = js.location;
        this.rodLevel = js.rodLevel;
        this.inventory = Arrays.asList(js.inventory);
        this.inventory = new ArrayList<>(this.inventory);
        this.waterType = Water.valueOf(js.waterType);
        this.almanac = fl.makeAlmanac();
        this.initializeAlmanac(Arrays.asList(js.caught));
        this.lure = Lure.valueOf(js.lure);
        this.lureDuration = js.lureDuration;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRodLevel() {
        return rodLevel;
    }

    public void setRodLevel(int rodLevel) {
        this.rodLevel = rodLevel;
    }

    public List<Fish> getInventory() {
        return inventory;
    }

    public void setInventory(List<Fish> inventory) {
        this.inventory = inventory;
    }

    public Water getWaterType() {
        return waterType;
    }

    public void setWaterType(Water waterType) {
        this.waterType = waterType;
    }

    public Lure getLure() {
        return lure;
    }

    public void setLure(Lure lure) {
        this.lure = lure;
    }

    public int getLureDuration() {
        return lureDuration;
    }

    public void setLureDuration(int lureDuration) {
        this.lureDuration = lureDuration;
    }

    public Map<String, Map<Water, Map<String, Boolean>>> getAlmanac() {
        return almanac;
    }

    public void setAlmanac(Map<String, Map<Water, Map<String, Boolean>>> almanac) {
        this.almanac = almanac;
    }

    public void addToInventory(Fish f) {
        this.inventory.add(f);
    }

    public void removeFromInventory(int i) {
        this.inventory.remove(i);
    }

    public void updateAlmanac(String name) {
        this.almanac.get(this.location).get(this.waterType).put(name, true);
    }

    public void initializeAlmanac(List<String> names) {
        for (String lkey : this.almanac.keySet()) {
            for (Water wkey : this.almanac.get(lkey).keySet()) {
                for (String fish : this.almanac.get(lkey).get(wkey).keySet()) {
                    if (names.contains(fish)) {
                        this.almanac.get(lkey).get(wkey).put(fish, true);
                    }
                }
            }
        }
    }

    public JsonObject makeJsonFromState() {
        JsonObject json = new JsonObject();
        json.add("money", new JsonPrimitive(this.money));
        json.add("rodLevel", new JsonPrimitive(this.rodLevel));
        json.add("inventory", this.getInventoryJson());
        json.add("lure", new JsonPrimitive(this.lure.toString()));
        json.add("lureDuration", new JsonPrimitive(this.lureDuration));
        json.add("caught", this.getCaughtFishJson());
        json.add("location", new JsonPrimitive(this.location));
        json.add("waterType", new JsonPrimitive(this.waterType.toString()));
        return json;
    }

    private JsonArray getInventoryJson() {
        JsonArray array = new JsonArray();
        for (Fish f: this.inventory) {
            JsonObject json = new JsonObject();
            json.add("name", new JsonPrimitive(f.getName()));
            json.add("worth", new JsonPrimitive(f.getWorth()));
            json.add("weight", new JsonPrimitive(f.getWeight()));
            array.add(json);
        }
        return array;
    }

    private JsonArray getCaughtFishJson() {
        JsonArray caught = new JsonArray();
        for (String lkey : this.almanac.keySet()) {
            for (Water wkey : this.almanac.get(lkey).keySet()) {
                for (String fish : this.almanac.get(lkey).get(wkey).keySet()) {
                    if (this.almanac.get(lkey).get(wkey).get(fish) == true) {
                        caught.add(fish);
                    }
                }
            }
        }
        return caught;
    }
}
