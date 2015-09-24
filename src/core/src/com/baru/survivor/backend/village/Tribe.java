package com.baru.survivor.backend.village;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.baru.survivor.Survivor;
import com.baru.survivor.backend.agents.Agent;
import com.baru.survivor.backend.agents.Bag;
import com.baru.survivor.backend.resources.Resource;
import com.baru.survivor.backend.resources.ResourceType;

public class Tribe {

	private List<Agent> members = new ArrayList<Agent>();
	private List<Agent> insideVillage = new ArrayList<Agent>();
	private Bag villageFoodVault = new Bag(Survivor.villageSlots);
	private Bag villageWaterVault = new Bag(Survivor.villageSlots);
	private Point villageLocation;
	
	public Tribe(Point villageLocation) {
		this.villageLocation = villageLocation;
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

	public Point position() {
		return villageLocation;
	}
	
	public void depositResource(Resource resource){
		if (resource.getResourceType() == ResourceType.FOOD && !villageFoodVault.isFull()){
			villageFoodVault.addresource(resource);
		}else if(resource.getResourceType() == ResourceType.WATER && !villageWaterVault.isFull()){
			villageWaterVault.addresource(resource);
		}
	}
	
	public Resource getResource(ResourceType type){
		if (type == ResourceType.FOOD){
			return villageFoodVault.getResource();
		}else{
			return villageWaterVault.getResource();
		}
	}
}
