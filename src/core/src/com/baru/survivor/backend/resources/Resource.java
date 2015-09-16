package com.baru.survivor.backend.resources;


public class Resource {

	private ResourceType resourceType;
	
	public Resource(ResourceType resourceType){
		this.resourceType = resourceType;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}
	
}
