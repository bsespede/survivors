package com.baru.survivor.frontend.canvas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.baru.survivor.frontend.Survivors;
import com.baru.survivor.frontend.sprite.AnimatedSprite;
import com.baru.survivor.frontend.sprite.SimpleSprite;

public class Layer {

	private SimpleSprite[][] tiles;
	
	public Layer() {
		tiles = new SimpleSprite[Survivors.width][Survivors.height];
	}
	
	public void setSprite(SimpleSprite newTile, int x, int y){
		tiles[x][y] = newTile;
	}
	
	public SimpleSprite getSprite(int x, int y) {
		return tiles[x][y];
	}

	public void draw(SpriteBatch batch) {
		for(int i = 0; i < Survivors.width; i++){
			for (int j = 0; j < Survivors.height; j++){
				if (tiles[i][j] != null){
					if (tiles[i][j] instanceof AnimatedSprite){
						batch.draw(((AnimatedSprite)tiles[i][j]).getTextureRegion(), 
							i * Survivors.tileSize,
							Gdx.graphics.getHeight() -(j+1) * Survivors.tileSize);
					} else {
						batch.draw(tiles[i][j].getTextureRegion(), 
								i * Survivors.tileSize,
								Gdx.graphics.getHeight() - (j+1) * Survivors.tileSize);
					}
				}
			}
		}
	}
}
