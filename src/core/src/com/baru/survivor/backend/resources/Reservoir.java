package com.baru.survivor.backend.resources;

import java.util.ArrayList;
import java.util.List;

public class Reservoir {

	private List<Resource> resources;
	private ResourceType type;
	private int maxResources;
	
	public Reservoir(ResourceType type, int maxResources){
		this.type = type;
		this.maxResources = maxResources;
		this.resources = new ArrayList<Resource>(maxResources);
		addResources(maxResources);
	}
	
	public Resource getResource(){
		int resourcesAmount = resources.size();
		if (resourcesAmount > 0){
			return resources.remove(resourcesAmount-1);			
		}else{
			return null;
		}
	}
	
	public void addResources(int amount){
		if (amount + resources.size() > maxResources){
			return;
		}
		while (amount > 0){
			resources.add(new Resource(type));
			amount--;
		}
	}

	public ResourceType type() {
		return type;
	}

	public boolean hasResource() {
		return !resources.isEmpty();
	}
	
}
