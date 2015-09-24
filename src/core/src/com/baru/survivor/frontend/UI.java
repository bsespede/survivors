package com.baru.survivor.frontend;

import java.awt.Point;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.baru.survivor.Survivor;
import com.baru.survivor.backend.Status;
import com.baru.survivor.backend.agents.Agent;
import com.baru.survivor.backend.agents.DayCycle;
import com.baru.survivor.frontend.canvas.Grid;

public class UI {

	private SpriteBatch batch;
	private Grid grid;
	private Sprite night;

	private BitmapFont font;

	private Texture text;
	private TextureRegion barBg;
	private TextureRegion barHg;
	private TextureRegion barTh;
	
	public void create(Status status) {

		grid = new Grid();		
		grid.fillTerrainLayers(status.getTerrainManager(), status.getTribeManager());
		grid.fillAgentVisuals(status.getAgentsManager());
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 10;
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.borderWidth = 1;
		parameter.borderStraight = true;
		font = generator.generateFont(parameter);
		generator.dispose();
		text = new Texture(Gdx.files.internal(Survivor.spriteSheet));
		barBg = new TextureRegion(text, 32, 896, 32, 10);
		barHg = new TextureRegion(text, 0, 896, 32, 5);
		barTh = new TextureRegion(text, 0, 901, 32, 5);
		
		
		Pixmap nightPm = new Pixmap(Survivor.width * Survivor.tileSize, Survivor.height * Survivor.tileSize, Pixmap.Format.RGBA8888);
		nightPm.setColor(0.3f, 0.25f, 0.34f, 1.0f);
		nightPm.fill();
		night = new Sprite(new Texture(nightPm));

		batch = new SpriteBatch();
	}

	public void render(Status status) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		grid.updateLayer(status.getAgentsManager(), status.getResourceManager());
		grid.draw(batch);
		if (status.getCycle() == DayCycle.NIGHT){
			night.draw(batch, 0.5f);			
		}else{
		}
		for (int x = 0; x < Survivor.width; x++) {
			for (int y = 0; y < Survivor.height; y++) {
				Point position = new Point(x, y);
				Agent agent = status.getAgentsManager().getAgentAt(position);
				if (agent != null) {
					drawAgentName(agent, x, y);
					drawAgentBars(agent, x, y);
				}
			}			
		}
		
		batch.end();
	}

	private void drawAgentBars(Agent agent, int x, int y) {
		if (!agent.isDead()) {
			batch.draw(barBg, x*Survivor.tileSize, Gdx.graphics.getHeight()-(y+1)*Survivor.tileSize-10);
			batch.draw(barHg, x*Survivor.tileSize, Gdx.graphics.getHeight()-(y+1)*Survivor.tileSize-5,
					32 * agent.getHunger(), 5);
			batch.draw(barTh, x*Survivor.tileSize, Gdx.graphics.getHeight()-(y+1)*Survivor.tileSize-10,
					32 * agent.getThirst(), 5);
		}
	}

	private void drawAgentName(Agent agent, int x, int y) {
		font.draw(batch, agent.name()+"("+agent.foodStg()+"/"+agent.waterStg()+")", x * Survivor.tileSize - 20,
				Gdx.graphics.getHeight() - ((y+1) * Survivor.tileSize) + 36);
	}

	public void dispose() {
		grid.dispose();
		font.dispose();
		text.dispose();
		batch.dispose();
	}

}