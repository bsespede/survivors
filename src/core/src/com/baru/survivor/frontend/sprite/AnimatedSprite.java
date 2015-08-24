package com.baru.survivor.frontend.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite extends SimpleSprite {

	private final int elapsedNanos = 200000000;
	private long lastTime = System.nanoTime();
	private int curFrame = 0;
	
	public AnimatedSprite(Texture spriteSheet, int[] spriteIndexes) {
		super(spriteSheet, spriteIndexes);
	}
	
	public TextureRegion getTextureRegion() {
		updateCurrent();
		return getTextureRegionByIndex(curFrame);
	}

	private void updateCurrent() {
		long curTime = System.nanoTime();
		if (curTime - lastTime > elapsedNanos){
			if (curFrame == spriteIndexes.length - 1){
				curFrame = 0;
			} else {
				curFrame += 1;
			}
			lastTime = curTime;
		}		
	}
}
