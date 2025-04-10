package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class WorldLogic {
    float gravity = 0;
    public Texture platformHolder;
    public SpriteBatch spriteBatch;
    FitViewport viewportWorld;
    float worldWidth;
    float worldHeight;
    int CurrentRoom;
    float deltaTime = Gdx.graphics.getDeltaTime();



    public void createWorld () {
        viewportWorld = new FitViewport(30, 20);
        worldWidth = viewportWorld.getWorldWidth();
        worldHeight = viewportWorld.getWorldHeight();
    }


    public void resize(int width, int height) {
        viewportWorld.update(width, height, true); // true centers the camera
    }


    public void renderWorld () {
        /*ScreenUtils.clear(Color.BLACK);*/
        viewportWorld.apply();

    }


}


