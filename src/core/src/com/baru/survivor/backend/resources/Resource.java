package com.baru.survivor.backend.resources;

import java.io.Serializable;


public class Resource implements Serializable{

	private ResourceType resourceType;
	
	public Resource(ResourceType resourceType){
		this.resourceType = resourceType;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}
	
}
