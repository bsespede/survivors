package com.baru.survivor.backend.resources;

import com.baru.survivor.backend.map.TileType;

public class Resource {

	private long createTime;
	private TileType resourceType;
	private int amount;
	
	public Resource(TileType resourceType, long curTime, int amount){
		this.createTime = curTime;
		this.amount = amount;
		this.resourceType = resourceType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public TileType getResourceType() {
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
