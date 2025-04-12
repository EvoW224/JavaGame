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
import io.github.javagame.temp.tile.WallTile;
import io.github.javagame.temp.ui.PauseMenu;

public class WorldLogic extends ScreenAdapter {
    // The viewport that defines the game world.
    public FitViewport viewportWorld;
    float worldWidth;
    float worldHeight;

    // Background for the game.
    private Background background;
    // Floor tile layer to cover the ground.
    private FloorTile floorLayer;
    private WallTile wallLayer;

    // Core game entities.
    public PC player;
    public Enemy enemy;
    public Rotor rotor;  // <-- New Rotor enemy
    public ArrayList<Projectiles> projectiles;

    // Textures for entities.
    private Texture textureFilePC;
    private Texture textureFileGB;
    private Texture textureFileRotor; // Texture for Rotor (dummy, as Rotor loads its own sprite sheet internally)

    // Shared SpriteBatch provided from Main.
    private SpriteBatch spriteBatch;

    // Reference to the Game instance (used for screen switching).
    private Game game;

    public WorldLogic(Game game, SpriteBatch batch) {
        this.game = game;
        this.spriteBatch = batch;

        // Create the background using "watching.png".
        background = new Background("watching.png");
        wallLayer = new WallTile("wLeft.png", 1f, 1f, 10f);


        // Create the floor tile layer; for testing, we choose "tile1.png".
        // Each tile will be 3 units wide and 1 unit high and drawn with an offset of 0.7f.
        floorLayer = new FloorTile("tile1.png", 3f, 2f, 0.7f);

        // PUT HERE WALL TILES

        createWorld();
        viewportWorld.apply();

        // Load textures for the entities.
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
        // For Rotor: although its initFlyAnimation loads "GoomBot_Flying.png",
        // we still pass a texture to the superclass (it won’t be used).
        try {
            textureFileRotor = new Texture(Gdx.files.internal("GoomBot_Flying.png"));
            System.out.println("Rotor Texture loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading Rotor texture: " + e.getMessage());
        }

        // Create game entities.
        player = new PC(viewportWorld, 18.0f, 4f, 6f, 1, 1, 100, 15, textureFilePC);
        projectiles = new ArrayList<>();
        enemy = new Enemy(viewportWorld, 4.0f, 4f, 6f, 15, 1, 30, 25, textureFileGB, projectiles);

        // Instantiate the Rotor enemy.
        // (Parameters: viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, texture, projectiles)
        // Adjust the values as needed.
        rotor = new Rotor(viewportWorld, 5.0f, 4f, 6f, 25, 1, 30, 20, textureFileRotor, projectiles);
    }

    public void createWorld() {
        // Create a FitViewport with a fixed world size (30 x 20).
        viewportWorld = new FitViewport(30, 20);
        worldWidth = viewportWorld.getWorldWidth();
        worldHeight = viewportWorld.getWorldHeight();
    }

    // Update the game simulation.
    public void update(float delta) {
        viewportWorld.getCamera().update();

        // Update game entities.
        if (player != null) {
            player.update(delta, projectiles);
        }
        if (enemy != null) {
            enemy.update(delta, 15f, 25f, player);
        }
        if (rotor != null) {
            // Here we assign patrol boundaries for Rotor (you can adjust these numbers).
            rotor.update(delta, 25f, 35f, player);
        }

        // Update projectiles.
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            projectiles.get(i).update(delta);
        }
        // Remove projectiles flagged for removal.
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            if (projectiles.get(i).isDead()) {
                projectiles.remove(i);
            }
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseMenu(game, this));
            System.out.println("Pause input detected.");
            return;
        }
        update(delta);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        spriteBatch.setProjectionMatrix(viewportWorld.getCamera().combined);

        // Render the background.
        background.render(spriteBatch, viewportWorld);
        // Render the floor tiles.
        spriteBatch.begin();
        floorLayer.render(spriteBatch, viewportWorld);
      //  wallLayer.render(spriteBatch, viewportWorld);
        // Draw the player, enemy, and rotor.
        if (player != null) { player.CharacterSprite.draw(spriteBatch); }
        if (enemy != null) { enemy.CharacterSprite.draw(spriteBatch); }
        if (rotor != null) { rotor.CharacterSprite.draw(spriteBatch); }
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
        if (textureFileRotor != null) textureFileRotor.dispose();
        background.dispose();
        // If your FloorTile class has a dispose method uncomment below.
        // floorLayer.dispose();
    }
}
