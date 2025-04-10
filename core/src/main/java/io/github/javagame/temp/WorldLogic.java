package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.ArrayList;
import com.badlogic.gdx.Game;
import io.github.javagame.temp.bg.Background;
import io.github.javagame.temp.tile.FloorTile;

public class WorldLogic extends ScreenAdapter {
    public FitViewport viewportWorld;
    float worldWidth;
    float worldHeight;
    private Background background;
    private FloorTile floorLayer;
    public PC player;
    public Enemy enemy;
    public ArrayList<Projectiles> projectiles;
    private Texture textureFilePC;
    private Texture textureFileGB;
    private SpriteBatch spriteBatch;
    private Game game;

    public WorldLogic(Game game, SpriteBatch batch) {
        this.game = game;
        this.spriteBatch = batch;
        background = new Background("watching.png");
        floorLayer = new FloorTile("tile1.png", 3f, 1f, .7f);
        createWorld();
        viewportWorld.apply();

        try {
            textureFilePC = new Texture(Gdx.files.internal("libgdx.png"));
            System.out.println("PC Texture loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading PC texture: " + e.getMessage());
        }
        try {
            textureFileGB = new Texture(Gdx.files.internal("PC sprite holder.png"));
            System.out.println("Enemy Texture loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading Enemy texture: " + e.getMessage());
        }

        player = new PC(viewportWorld, 18.0f, 4f, 6f, 1, 1, 100, 15, textureFilePC);
        enemy = new Enemy(viewportWorld, 4.0f, 4f, 6f, 15, 1, 30, 25, textureFileGB);
        projectiles = new ArrayList<>();
    }

    public void createWorld() {
        viewportWorld = new FitViewport(30, 20);
        worldWidth = viewportWorld.getWorldWidth();
        worldHeight = viewportWorld.getWorldHeight();
    }

    public void update(float delta) {
        viewportWorld.getCamera().update();
        player.update(delta, projectiles);
        enemy.update(delta, 15f, 25f, player);
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            projectiles.get(i).update(delta);
        }
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            if (projectiles.get(i).shouldRemove) {
                projectiles.remove(i);
            }
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new io.github.javagame.temp.ui.PauseMenu(game, this));
            System.out.println("Pause input detected.");
            return;
        }

        update(delta);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        spriteBatch.setProjectionMatrix(viewportWorld.getCamera().combined);
        background.render(spriteBatch, viewportWorld);
        spriteBatch.begin();
        floorLayer.render(spriteBatch, viewportWorld);
        player.CharacterSprite.draw(spriteBatch);
        enemy.CharacterSprite.draw(spriteBatch);
        for (Projectiles shot : projectiles) {
            shot.CharacterSprite.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewportWorld.update(width, height, true);
    }

    @Override
    public void dispose() {
        if (textureFilePC != null) textureFilePC.dispose();
        if (textureFileGB != null) textureFileGB.dispose();
        background.dispose();
        // Optionally dispose floorLayer if needed.
    }
}
