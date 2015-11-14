package csgodemo;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
	
	String name, steamid;
	
	HashMap <String, ArrayList<Float>> shots;
	// weapon name - velocities of when shots were made
	
	public Player(String steamid, String name){
		shots = new HashMap<String, ArrayList<Float>>();
		this.steamid = steamid;
		this.name = name;
	}
	
	
	public void addShot(String weapon, float vel){
		
		if (!shots.containsKey(weapon)){
			shots.put(weapon, new ArrayList<>());
		}
		shots.get(weapon).add(vel);
	}
	
	static Player getPlayerBySteamID(ArrayList<Player> p, String steamid){
		for (Player player : p){
			if (player.steamid.equals(steamid)){
				return player;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}


	public float getAverageAccuracy(String weapon) {
		float res = 0;
		ArrayList<Float> f = shots.get(weapon);
		for (Float ff : f){
			res += ff;
		}
		res = res / f.size();
		return res;
	}

}
