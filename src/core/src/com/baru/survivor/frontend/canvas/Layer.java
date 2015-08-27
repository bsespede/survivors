package com.baru.survivor.frontend.canvas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.baru.survivor.Survivor;
import com.baru.survivor.frontend.sprite.AnimatedSprite;
import com.baru.survivor.frontend.sprite.SimpleSprite;

public class Layer {

	private SimpleSprite[][] tiles;
	
	public Layer() {
		tiles = new SimpleSprite[Survivor.width][Survivor.height];
	}
	
	public void setSprite(SimpleSprite newTile, int x, int y){
		tiles[x][y] = newTile;
	}
	
	public SimpleSprite getSprite(int x, int y) {
		return tiles[x][y];
	}

	public void draw(SpriteBatch batch) {
		for(int i = 0; i < Survivor.width; i++){
			for (int j = 0; j < Survivor.height; j++){
				if (tiles[i][j] != null){
					if (tiles[i][j] instanceof AnimatedSprite){
						batch.draw(((AnimatedSprite)tiles[i][j]).getTextureRegion(), 
							i * Survivor.tileSize,
							Gdx.graphics.getHeight() -(j+1) * Survivor.tileSize);
					} else {
						batch.draw(tiles[i][j].getTextureRegion(), 
								i * Survivor.tileSize,
								Gdx.graphics.getHeight() - (j+1) * Survivor.tileSize);
					}
				}
			}
		}
	}
}
