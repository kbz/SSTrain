package com.fteams.sstrain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fteams.sstrain.screens.SplashScreen;

public class SSTrain extends Game {

    @Override
    public void create() {
        setScreen(new SplashScreen());
    }

}
