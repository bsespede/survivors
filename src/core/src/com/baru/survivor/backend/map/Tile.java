package com.baru.survivor.backend.map;


public class Tile {

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
