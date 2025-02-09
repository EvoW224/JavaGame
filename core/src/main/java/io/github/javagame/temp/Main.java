package io.github.javagame.temp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private GameWorld gameWorld;

    @Override
    public void create() {
        // Initialize game objects here!

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Initialize rendering utilities
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Initialize game world
        gameWorld = new GameWorld();
    }

    @Override
    public void render() {
        // Start rendering game objects here

        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update game logic
        gameWorld.update();

        // Set camera projection
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Render game world
        gameWorld.render(shapeRenderer);
    }

    @Override
    public void dispose() {
        // Deinitialize and deallocate game objects here.

        // Cleanup resources
        batch.dispose();
        shapeRenderer.dispose();
    }
}
