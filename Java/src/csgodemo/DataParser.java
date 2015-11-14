package csgodemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import javax.script.ScriptException;

class DataParser {

	public static ArrayList<Player> getData(String demo)
			throws IOException, InterruptedException, ScriptException, NoSuchMethodException, ParseException {

	
		ArrayList<Player> players = new ArrayList<>();

		Runtime rt = Runtime.getRuntime();
		String[] commands = { "./ParseData.exe", demo };
		Process proc = rt.exec(commands);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		
		String steamid = "";
		float velx = -1, vely = -1;
		String weapon = "";
		String name = "";

		String s = null;
		while ((s = stdInput.readLine()) != null) {
			
			//System.out.println(s);
			
			if (s.startsWith("velx")){
				s = s.replaceAll(",", ".");
				velx = Float.parseFloat(s.split("=")[1]);
				continue;
			}
			if (s.startsWith("vely")){
				s = s.replaceAll(",", ".");
				vely = Float.parseFloat(s.split("=")[1]);
				continue;
			}
			if (s.startsWith("weapon")){
				weapon = s.split("=")[1];
				continue;
			}
			if (s.startsWith("playername")){
				name = s.split("=")[1];
				continue;
			}
			if (s.startsWith("playersteamid")){
				steamid = s.split("=")[1];
				continue;
			}
			if (s.startsWith("}")){
				if (Player.getPlayerBySteamID(players, steamid)==null){
					players.add(new Player(steamid, name));
				}
				Player.getPlayerBySteamID(players, steamid).addShot(weapon, (float) Math.sqrt(velx*velx + vely*vely));
			}
			
			if (s.startsWith("End")){
				break;
			}
			
		}
		
		return players;

	}
}