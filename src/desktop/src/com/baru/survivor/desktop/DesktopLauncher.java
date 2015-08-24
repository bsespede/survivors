package com.baru.survivor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.baru.survivor.Survivor;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Survivor";
		config.width = 48 * 32;
		config.height = 25 * 32;
		config.resizable = false;
		
		new LwjglApplication(new Survivor(), config);
	}
}
