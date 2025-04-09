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

public class WorldLogic extends ScreenAdapter {
    // The viewport defining the game world.
    public FitViewport viewportWorld;
    float worldWidth;
    float worldHeight;

    // Core game entities.
    public PC player;
    public Enemy enemy;
    public ArrayList<Projectiles> projectiles;

    // Textures for entities.
    private Texture textureFilePC;
    private Texture textureFileGB;

    // Shared SpriteBatch provided from Main.
    private SpriteBatch spriteBatch;

    // A reference to the Game object (for screen switching, if needed).
    private Game game;

    public WorldLogic(Game game, SpriteBatch batch) {
        this.game = game;
        this.spriteBatch = batch;
        createWorld();
        viewportWorld.apply();

        // Load textures for entities.
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

        // Create gameplay entities.
        player = new PC(viewportWorld, 18.0f, 2f, 3f, 1, 1, 100, 15, textureFilePC);
        enemy = new Enemy(viewportWorld, 4.0f, 2f, 2f, 15, 1, 30, 25, textureFileGB);
        projectiles = new ArrayList<>();
    }

    public void createWorld() {
        // Create a FitViewport with a fixed world size (e.g., 30 x 20).
        viewportWorld = new FitViewport(30, 20);
        worldWidth = viewportWorld.getWorldWidth();
        worldHeight = viewportWorld.getWorldHeight();
    }

    // Update the game simulation.
    public void update(float delta) {
        // Update the camera.
        viewportWorld.getCamera().update();
        // Update the player (pass in the projectile list for shooting).
        player.update(delta, projectiles);
        // Update the enemy (provide bounds and a reference to the player).
        enemy.update(delta, 15f, 25f, player);
        // Update all projectiles.
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            projectiles.get(i).update(delta);
        }
        // Remove projectiles that are flagged for removal.
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            if (projectiles.get(i).shouldRemove) {
                projectiles.remove(i);
            }
        }
    }

    @Override
    public void render(float delta) {
        // (Optional) Handle in-game pause input; for debugging, we'll ignore pause.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // For now, you could print a message or later transition to a pause menu.
            System.out.println("Pause requested (not handled in this test mode).");
        }

        // Update simulation.
        update(delta);

        // Clear the screen.
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        spriteBatch.setProjectionMatrix(viewportWorld.getCamera().combined);

        spriteBatch.begin();
        // Draw the player.
        player.CharacterSprite.draw(spriteBatch);
        // Draw the enemy.
        enemy.CharacterSprite.draw(spriteBatch);
        // Draw each projectile.
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
    }
}
