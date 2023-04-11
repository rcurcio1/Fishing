package SpeciesInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Model.Water;

public class FishLocations {
    private Map<String, List<Species>> speciesLocations;
    private List<String> locations;
    private Map<String, List<Water>> waterLocations;
    private final String FISH_FILE = "data.txt";

    public FishLocations(){
        this.speciesLocations = new HashMap<String, List<Species>>();
        this.waterLocations = new HashMap<String, List<Water>>();
        this.locations = new ArrayList<String>();
        try {
            this.readFile(FISH_FILE);
        }
        catch(IOException e) {
            System.out.println("File not found :/\n");
        }
    }

    private void readFile(String f) throws IOException{
        File data = new File(f);
        Scanner fs = new Scanner(data);
        boolean inLocation = false;
        boolean inWater = false;
        while (fs.hasNextLine()) {
            String location = fs.nextLine();
            if (fs.nextLine().equals("{")) {
                inLocation = true;
                this.locations.add(location);
            }
            List<Water> waters = new ArrayList<Water>();
            List<Species> species = new ArrayList<Species>();
            while(inLocation) {
                String water = fs.nextLine();
                if (water.equals("}")) {
                    inLocation = false;
                }
                else if (fs.nextLine().equals("{")) {
                    inWater = true;
                    waters.add(Water.valueOf(water));
                }
                while(inWater) {
                    String fishName = fs.nextLine();
                    if (fishName.equals("}")) {
                        inWater = false;
                    }
                    else {
                        double min = fs.nextDouble();
                        double max = fs.nextDouble();
                        double cost = fs.nextDouble();
                        Rarity rare = Rarity.valueOf(fs.next());
                        Species specie = new Species(fishName, min, max, Water.valueOf(water), cost, rare);
                        species.add(specie);
                        fs.nextLine();
                    }
                }
                this.speciesLocations.putIfAbsent(location, species);
            }
            this.waterLocations.put(location, waters);
        }
        fs.close();
    }

    public Map<String, List<Species>> getFishLocations() {
        return this.speciesLocations;
    }

    public Map<String, List<Water>> getWaterLocations() {
        return this.waterLocations;
    }

    public List<String> getLocations() { return this.locations; }

    public Map<String, Map<Water, Map<String, Boolean>>> makeAlmanac() {
        Map<String, Map<Water, Map<String, Boolean>>> almanac = new HashMap<>();
        for (String loc: this.locations) {
            Map<Water, Map<String, Boolean>> waterMap = new HashMap<>();
            for (Water water: this.waterLocations.get(loc)) {
                Map<String, Boolean> speciesMap = new HashMap<>();
                for (Species spec : this.speciesLocations.get(loc)) {
                    if (spec.getWaterType() == water) {
                        speciesMap.put(spec.getSpecies(), false);
                    }
                }
                waterMap.put(water, speciesMap);
            }
            almanac.put(loc, waterMap);
        }
        return almanac;
    }

    public int getNumSpecies() {
        int count = 0;
        for (String loc : this.speciesLocations.keySet()) {
            count += this.speciesLocations.get(loc).size();
        }
        return count;
   }
}
