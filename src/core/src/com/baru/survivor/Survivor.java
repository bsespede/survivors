package com.baru.survivor;

import com.badlogic.gdx.ApplicationAdapter;
import com.baru.survivor.backend.Status;
import com.baru.survivor.frontend.UI;

public class Survivor extends ApplicationAdapter {
	
	public static final int width = 48;
	public static final int height = 25;
	public static final int tileSize = 32;
	public static final int tickTime = 250;
	public static final int secondsPerDay = 60;
	public static final String spriteSheet = "spriteSheet-big.png";
	public static final int dayTicks = (1000/Survivor.tickTime) * Survivor.secondsPerDay;
	public static final int agentSlots = 3;
	public static final int villageSlots = 15;	
	
	Status status;
	UI ui;
	
	@Override
	public void create() {
		status = new Status();
		ui = new UI();
		status.create(2, 2, 50, 5, 50, 5);
		ui.create(status);
	}

	public void dispose(){
		
		ui.dispose();
	}
	
	@Override
	public void render() {
		status.nextState(System.currentTimeMillis());
		ui.render(status);
	}
}