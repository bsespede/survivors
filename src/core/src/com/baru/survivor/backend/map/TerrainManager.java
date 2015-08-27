package com.baru.survivor.backend.map;

import java.awt.Point;
import java.util.Random;

import com.baru.survivor.Survivor;

public class TerrainManager {

	private Tile[][] tiles;
	private AutoTile autoTiles;
	
	public TerrainManager() {
		tiles = new Tile[Survivor.width][Survivor.height];
	}
	
	public TerrainManager(Tile[][] tiles, AutoTile autoTiles) {
		this.tiles = tiles;
		this.autoTiles = autoTiles;
	}
	
	public TileType getTileType(int i, int j) {
		return tiles[i][j].getType();
	}
	
	public byte getAutoTile(int x, int y){
		return autoTiles.get(x, y);
	}

	public Point getSpawnablePoint() {
		Random rand = new Random();
		while (true) {
			int x = (int) (rand.nextFloat() * Survivor.width);
			int y = (int) (rand.nextFloat() * Survivor.height);
			if (!tiles[x][y].isBlocked()){
				return new Point(x, y);
			}
		}
	}
}
