package com.baru.survivor.backend.map;

import com.baru.survivor.frontend.Survivors;

public class Terrain {

	private Tile[][] tiles;
	private AutoTile autoTiles;
	
	public Terrain() {
		tiles = new Tile[Survivors.width][Survivors.height];
	}
	
	public Terrain(Tile[][] tiles, AutoTile autoTiles) {
		this.tiles = tiles;
		this.autoTiles = autoTiles;
	}
	
	public TileType getTileType(int i, int j) {
		return tiles[i][j].getType();
	}
	
	public byte getAutoTile(int x, int y){
		return autoTiles.get(x, y);
	}

	public boolean canSpawn(int x, int y) {
		return !tiles[x][y].isBlocked();
	}
}
