package Json;

import Model.Fish;

public class JsonState {
    public int money;
    public int rodLevel;
    public Fish[] inventory;
    public String lure;
    public int lureDuration;
    public String[] caught;
    public String location;
    public String waterType;

    public JsonState(int money, int rodLevel, Fish[] inventory, String lure, int lureDuration, String[] caught,
            String location, String waterType) {
        this.money = money;
        this.rodLevel = rodLevel;
        this.inventory = inventory;
        this.lure = lure;
        this.lureDuration = lureDuration;
        this.caught = caught;
        this.location = location;
        this.waterType = waterType;
    }    
}
