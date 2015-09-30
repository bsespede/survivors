package com.baru.survivor.backend.map;

import java.io.Serializable;


public class Tile implements Serializable{

	private boolean blocked;
	private TileType type;
	
	public Tile(TileType type, boolean blocked) {
		this.type = type;
		this.blocked = blocked;
	}
	
	public boolean isBlocked() {
		return blocked;
	}
	
	public TileType getType() {
		return type;
	}
	
}
