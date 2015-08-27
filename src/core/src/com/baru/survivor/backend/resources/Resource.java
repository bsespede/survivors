package com.baru.survivor.backend.resources;

public class Resource {

	private long createTime;
	private ResourceType resourceType;
	private int amount;
	
	public Resource(ResourceType resourceType, long curTime, int amount){
		this.createTime = curTime;
		this.amount = amount;
		this.resourceType = resourceType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public int getAmount() {
		return amount;
	}

	public boolean consumeResource() {
		if (--amount == 0) {
			return false;
		} else {
			return true;
		}
	}
	
}
