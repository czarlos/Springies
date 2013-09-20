package springies;

import java.util.ArrayList;
import java.util.HashMap;

import jboxGlue.Mass;
import jboxGlue.Spring;
import jboxGlue.Wall;
import jgame.platform.JGEngine;

public class Controls {
	int initialArea = 10; //Initial wall margin
	//int toggleGravity = 0; //Gravity on/off
	
	private ArrayList<HashMap<String, Mass>> massMaps;
	private ArrayList<ArrayList<Spring>> springArrays;
	private ArrayList<Wall> wallList;

	private Springies springies;
	private BoardSetup boardSetup;
	public Controls(Springies springies, BoardSetup boardSetup, ArrayList<HashMap<String, Mass>> massMaps,
			ArrayList<ArrayList<Spring>> springArrays, ArrayList<Wall> wallList) {
		this.springArrays = springArrays;
		this.massMaps = massMaps;
		this.springies = springies;
		this.wallList = wallList;
		this.boardSetup = boardSetup;
	}
	
	public void checkUserInput() {
		//Object is made out of bounds if made
		//after the first object
		//Read new item toggle
		if (springies.getKey('N')) {
			springies.clearKey('N');
			boardSetup.makeAssembly(massMaps, springArrays);
		}
		//Clear toggle
		if (springies.getKey('C')) {
			springies.clearKey('C');

			for (ArrayList<Spring> springArray : springArrays) {
				for (Spring s : springArray) {
					s.remove();
				}
			}
			
			for (HashMap<String, Mass> massList : massMaps) {
				for (Mass m : massList.values()) {
					m.remove();
				}
			}

			massMaps.clear();
			springArrays.clear();
		}
		
		//Gravity toggle
		if (springies.getKey('G')) {
			if(springies.toggleGravity==1){
				springies.toggleGravity = 0;
			}
			else{
				springies.toggleGravity = 1;
			}
		}
		if (springies.getKey(JGEngine.KeyUp)){
			for(Wall wall : wallList){
				wall.remove();
			}
			boardSetup.setWalls(initialArea-=10);
		}
		if (springies.getKey(JGEngine.KeyDown)){
			for(Wall wall : wallList){
				wall.remove();
			}
			boardSetup.setWalls(initialArea+=10);
		}
	}
}
