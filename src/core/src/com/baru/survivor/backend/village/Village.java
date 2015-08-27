package com.baru.survivor.backend.village;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.baru.survivor.backend.agents.Agent;

public class Village {

	private Point villageCoord;
	private List<Agent> inVillage = new ArrayList<Agent>();
	
	public Village(Point villageCoord) {
		this.villageCoord = villageCoord;
	}

	public int getVillageX(){
		return villageCoord.x;
	}
	
	public int getVillageY(){
		return villageCoord.y;
	}
	
	public void leaveVillage(Agent agent){
		if (inVillage.contains(agent)) {
			inVillage.remove(agent);
		}
	}
	
	public void enterVillage(Agent agent){
		inVillage.add(agent);
	}
	
	public boolean insideVillage(Agent agent) {
		return inVillage.contains(agent);
	}
}
