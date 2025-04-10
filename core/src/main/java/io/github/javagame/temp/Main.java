package io.github.javagame.temp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Input.Keys;

import java.util.ArrayList;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    WorldLogic World;
    PC Player;
    Enemy Goombabot;
    Rotor Apache;
    Grapple Handy;
    Texture TextureFilePC;
    Texture TextureFileGB;
    //Sprite testSprite;
    SpriteBatch spriteBatch;
    float deltaTimes;
    ArrayList<Projectiles> projectiles;



    @Override
    public void create() {
        World = new WorldLogic();
        World.createWorld();
        World.viewportWorld.apply();

        // Debug texture loading
        try {
            TextureFilePC = new Texture (Gdx.files.internal("libgdx.png"));
            System.out.println("PC Texture loaded successfully");
            System.out.println("PC Texture path: " + Gdx.files.internal("libgdx.png").path());
        } catch (Exception e) {
            System.out.println("Error loading PC texture: " + e.getMessage());
            System.out.println("Current working directory: " + System.getProperty("user.dir"));
        }

        try {
            TextureFileGB = new Texture (Gdx.files.internal("PC sprite holder.png"));
            System.out.println("Enemy Texture loaded successfully");
            System.out.println("Enemy texture path: " + Gdx.files.internal("PC sprite holder.png").path());
            System.out.println("Enemy texture dimensions: " + TextureFileGB.getWidth() + "x" + TextureFileGB.getHeight());
        } catch (Exception e) {
            System.out.println("Error loading Enemy texture: " + e.getMessage());
            System.out.println("Current working directory: " + System.getProperty("user.dir"));
        }

        System.out.println("Viewport dimensions: " + World.viewportWorld.getWorldWidth() + "x" + World.viewportWorld.getWorldHeight());

        Player = new PC(World.viewportWorld, 18.0f, 2f, 3f, 1,1,100, 15, TextureFilePC);
        projectiles = new ArrayList<>();
        Goombabot = new Enemy(World.viewportWorld, 3.0f, 2f, 2f, 15, 13, 30, 25, TextureFileGB, projectiles);
        Apache = new Rotor(World.viewportWorld, 4.0f, 2f, 2f, 15, 15, 30, 25, TextureFileGB, projectiles);
        Handy = new Grapple(World.viewportWorld, 4.0f, 2f, 2f, 25, 1, 30, 25, TextureFileGB, projectiles);


        // Debug sprite creation
        System.out.println("Enemy sprite created: " + (Goombabot.CharacterSprite != null));
        if (Goombabot.CharacterSprite != null) {
            System.out.println("Enemy sprite texture: " + (Goombabot.CharacterSprite.getTexture() != null));
            System.out.println("Enemy sprite dimensions: " + Goombabot.CharacterSprite.getWidth() + "x" + Goombabot.CharacterSprite.getHeight());
            System.out.println("Enemy sprite position: " + Goombabot.CharacterSprite.getX() + ", " + Goombabot.CharacterSprite.getY());
        }

        spriteBatch = new SpriteBatch();
    }


    @Override
    public void resize(int width, int height) {
        World.resize(width,height);
    }


    @Override
    public void render() {
        deltaTimes = Gdx.graphics.getDeltaTime();;
       // System.out.println(deltaTimes);
        input();
        logic();
        draw();

    }

    private void input() {
        World.viewportWorld.getCamera().update();
        if (Player != null) {Player.update(deltaTimes, projectiles);}
        if (Goombabot != null) { Goombabot.update(deltaTimes,15f,25f, Player);  }// Update enemy with player reference
        if (Apache != null) { Apache.update(deltaTimes,15f,25f, Player);  }
        if (Handy != null) {Handy.update(deltaTimes,15f,25f, Player);}
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            projectiles.get(i).update(deltaTimes);
            System.out.println(projectiles.get(i).shouldRemove);
        }

    }

    private void logic() {
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            if (projectiles.get(i).shouldRemove) {projectiles.remove(i);}
        }
        if (Player != null) { if (Player.isDead()) { Player  = null;} }
        if (Goombabot != null) { if (Goombabot.isDead()) { Goombabot = null; } }
        if (Apache != null) { if (Apache.isDead()) { Apache = null; } }
        if (Handy != null) {if (Handy.isDead()) { Handy = null; }}
    }

    private void draw() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        World.renderWorld();
        spriteBatch.setProjectionMatrix(World.viewportWorld.getCamera().combined);

        spriteBatch.begin();

        // Draw player first
        if (Player != null) {
            Player.CharacterSprite.draw(spriteBatch);
            // System.out.println("Drawing player at: " + Player.CharacterSprite.getX() + ", " + Player.CharacterSprite.getY());
        }
        // Draw enemy second
        if (Goombabot != null) {
            Goombabot.CharacterSprite.draw(spriteBatch);
            // System.out.println("Drawing enemy at: " + Goombabot.CharacterSprite.getX() + ", " + Goombabot.CharacterSprite.getY());
        }

        if (Apache != null) {
            Apache.CharacterSprite.draw(spriteBatch);
            // System.out.println("Drawing enemy at: " + Goombabot.CharacterSprite.getX() + ", " + Goombabot.CharacterSprite.getY());
        }

        if (Handy != null) {
            Handy.CharacterSprite.draw(spriteBatch);
            // System.out.println("Drawing enemy at: " + Goombabot.CharacterSprite.getX() + ", " + Goombabot.CharacterSprite.getY());
        }


        //Bullet Rendering
        for (Projectiles shot : projectiles) {
            shot.CharacterSprite.draw(spriteBatch);
            //System.out.println("X: " + shot.getXPosition() + " Y: " + shot.getYPosition());
        }
        // Draw HP bar last

        spriteBatch.end();
    }



    @Override
    public void dispose() {

    }


}

