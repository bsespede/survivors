package com.baru.survivor.frontend.canvas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.baru.survivor.Survivor;
import com.baru.survivor.backend.pheromones.Pheromones;

public class PheromonePainter{

	Sprite pheromoneSprite;
	Pheromones pheromones;
	
	public PheromonePainter(){
		Pixmap pherPm = new Pixmap(Survivor.tileSize, Survivor.tileSize, Pixmap.Format.RGBA8888);
		pherPm.setColor(1f, 0f, 0f, 1f);
		pherPm.fill();
		this.pheromoneSprite = new Sprite(new Texture(pherPm));
	}
	
	public void draw(SpriteBatch batch) {
		for (int x = 0; x < Survivor.width; x++) {
			for (int y = 0; y < Survivor.height; y++) {
				pheromoneSprite.setPosition(x * Survivor.tileSize, Gdx.graphics.getHeight()-(y+1)*Survivor.tileSize);
				pheromoneSprite.draw(batch, pheromones.getIntensity(x, y) / pheromones.getMaxPheromones());
			}
		}
	}
	
	public void dispose(){
		pheromoneSprite.getTexture().dispose();
	}

	public void update(Pheromones pheromones) {
		this.pheromones = pheromones;
	}
}
