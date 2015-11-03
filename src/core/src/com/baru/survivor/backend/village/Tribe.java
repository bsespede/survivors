package com.baru.survivor.backend.village;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baru.survivor.Survivor;
import com.baru.survivor.backend.agents.Agent;
import com.baru.survivor.backend.agents.Bag;
import com.baru.survivor.backend.resources.Resource;
import com.baru.survivor.backend.resources.ResourceType;

public class Tribe implements Serializable{

	private List<Agent> members = new ArrayList<Agent>();
	private Bag villageFoodVault = new Bag(Survivor.villageSlots);
	private Bag villageWaterVault = new Bag(Survivor.villageSlots);
	private Point villageLocation;
	
	public Tribe(Point villageLocation) {
		this.villageLocation = villageLocation;
	}
	
	public Bag getWaterVault(){
		return villageWaterVault;
	}
	
	public Bag getFoodVault(){
		return villageFoodVault;
	}
	
	public void addMember(Agent agent) {
		members.add(agent);
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

	public List<Agent> getAgents() {
		return members;
	}
}
