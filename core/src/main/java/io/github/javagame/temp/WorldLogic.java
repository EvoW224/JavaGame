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
    // The viewport that defines the game world.
    public FitViewport viewportWorld;
    float worldWidth;
    float worldHeight;

    // Background for the game.
    private Background background;
    // Floor tile layer to cover the ground.
    private FloorTile floorLayer;

    // Core game entities.
    public PC player;
    public Enemy enemy;
    public ArrayList<Projectiles> projectiles;
    public Rotor rotor; // New rotor enemy

    // Textures for entities.
    private Texture textureFilePC;
    private Texture textureFileGB;
    private Texture textureRotor;


    // Shared SpriteBatch provided from Main.
    private SpriteBatch spriteBatch;

    // Reference to the Game instance (used for screen switching).
    private Game game;

    Room room1;

    public WorldLogic(Game game, SpriteBatch batch) {
        this.game = game;
        this.spriteBatch = batch;

        // Create the background using "watching.png".
        background = new Background("watching.png");

        // Create the floor tile layer; for testing, we choose "tile1.png".
        // Here, each tile is defined to be 3 units wide and 1 unit high in the world.
        floorLayer = new FloorTile("tile1.png", 3f, 1f, .7f);

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
        try {
            textureRotor = new Texture(Gdx.files.internal("GoomBot_Flying.png"));
            System.out.println("Rotor Texture loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading Rotor texture: " + e.getMessage());
        }
        //Room Textures


        // Create game entities.

        player = new PC(viewportWorld, 18.0f, 4f, 6f, 1, 1, 100, 15, textureFilePC);
        projectiles = new ArrayList<>();
        enemy = new Enemy(viewportWorld, 4.0f, 4f, 6f, 15, 1, 30, 25, textureFileGB, projectiles);
        rotor = new Rotor(viewportWorld, 4.0f, 4f, 6f, 15, 10, 30, 25, textureRotor, projectiles);

    }

    public void createWorld() {
        // Create a FitViewport with a fixed world size (30 x 20).
        viewportWorld = new FitViewport(30, 20);
        worldWidth = viewportWorld.getWorldWidth();
        worldHeight = viewportWorld.getWorldHeight();
        room1 = new Room(viewportWorld);
        room1.setTiles();

    }

    // Update the game simulation.
    public void update(float delta) {
        viewportWorld.getCamera().update();

        // Update game entities.
        if (player != null) { player.update(delta, projectiles); }
        if (enemy != null) {enemy.update(delta, 15f, 25f, player); }
        if (rotor != null) {rotor.update(delta, 15f, 25f, player); }

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

   // @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new io.github.javagame.temp.ui.PauseMenu(game, this));
            System.out.println("Pause input detected.");
            return;
        }

        for (int i = projectiles.size() - 1; i >= 0; i--) {
            if (projectiles.get(i).shouldRemove) {projectiles.remove(i);}
        }
        if (player != null) { if (player.isDead()) { player  = null;} }
        if (enemy != null) { if (enemy.isDead()) { enemy = null; } }
        if (rotor != null) { if (rotor.isDead()) { rotor = null; } }

        /*if (Apache != null) { if (Apache.isDead()) { Apache = null; } }
        if (Handy != null) {if (Handy.isDead()) { Handy = null; }}*/

        update(delta);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        spriteBatch.setProjectionMatrix(viewportWorld.getCamera().combined);

        // Render the background.
        background.render(spriteBatch, viewportWorld);
        // Render the floor tiles.

        spriteBatch.begin();
        for (int i = 0; i < 150; ++i) {
            if(room1.CurrentRoom.get(i) != null) {
                room1.CurrentRoom.get(i).TileSprite.draw(spriteBatch);
            }
        }
        floorLayer.render(spriteBatch, viewportWorld);
        if (player != null) {player.CharacterSprite.draw(spriteBatch);}
        if (enemy != null) {enemy.CharacterSprite.draw(spriteBatch);}
        if (rotor != null) {rotor.CharacterSprite.draw(spriteBatch);}
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
        if (textureRotor != null) textureRotor.dispose();
        background.dispose();
       // floorLayer.dispose();
    }
}
