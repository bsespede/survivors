package com.baru.survivor.frontend.sprite;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.baru.survivor.frontend.Survivors;

public class SimpleSprite {

	protected int[] spriteIndexes;
	protected final Texture texture;
	
	public SimpleSprite(Texture texture, int[] spriteIndexes) {
		this.texture = texture;
		this.spriteIndexes = spriteIndexes;
	}
	
	public TextureRegion getTextureRegion() {
		return getTextureRegionByIndex(0);
	}
	
	public TextureRegion getTextureRegionByIndex(int index) {
		int x = (spriteIndexes[index] % 7) * Survivors.tileSize;
	    int y = Survivors.tileSize * (spriteIndexes[index] / 7);
		return new TextureRegion(texture, x, y, Survivors.tileSize, Survivors.tileSize);
	}
}
