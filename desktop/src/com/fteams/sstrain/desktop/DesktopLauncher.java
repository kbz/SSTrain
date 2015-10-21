package com.fteams.sstrain.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.fteams.sstrain.SSTrain;
import com.fteams.sstrain.config.GlobalConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		GlobalConfiguration.appVersionName = "debug build.";
		new LwjglApplication(new SSTrain(), "SS Train", 1280, 720);
	}
}
