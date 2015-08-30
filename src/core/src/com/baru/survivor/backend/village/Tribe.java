package com.baru.survivor.backend.village;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.baru.survivor.backend.agents.Agent;

public class Tribe {

	private List<Agent> members = new ArrayList<Agent>();
	private List<Agent> insideVillage = new ArrayList<Agent>();
	private Point villageLocation;
	
	public Tribe(Point villageLocation) {
		this.villageLocation = villageLocation;
	}

	public int getVillageX(){
		return villageLocation.x;
	}
	
	public int getVillageY(){
		return villageLocation.y;
	}
	
	public void addMember(Agent agent) {
		members.add(agent);
	}
	
	public void leaveVillage(Agent agent){
		if (insideVillage.contains(agent)) {
			insideVillage.remove(agent);
		}
	}
	
	public void enterVillage(Agent agent){
		insideVillage.add(agent);
	}
	
	public boolean insideVillage(Agent agent) {
		return insideVillage.contains(agent);
	}
}
