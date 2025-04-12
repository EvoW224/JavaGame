package io.github.javagame.temp.bg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The Background class is responsible for rendering a single background image
 * that scales to fill the entire game world as defined by the provided viewport.
 *
 * This is a simple implementation designed for early prototypes. In the future,
 * you might extend this with multiple layers (parallax scrolling), animated backgrounds,
 * and additional logic for dynamic background effects.
 */
public class Background {

    private Texture bgTexture;

    /**
     * Constructs a Background by loading the image from the given path.
     *
     * @param pathToBackground The path to the background image (e.g., "watching.png").
     */
    public Background(String pathToBackground) {
        try {
            bgTexture = new Texture(Gdx.files.internal(pathToBackground));
        } catch (Exception e) {
            Gdx.app.error("Background", "Failed to load background texture from: " + pathToBackground, e);
            // If loading fails, you might choose to create a fallback texture or handle gracefully.
            // For now, we simply rethrow the exception to aid debugging.
            throw e;
        }
    }

    /**
     * Renders the background so that it fills the full dimensions of the viewport.
     *
     * @param batch The SpriteBatch used for drawing.
     * @param viewport The viewport defining the game world's dimensions.
     */
    public void render(SpriteBatch batch, Viewport viewport) {
        // Get world dimensions from the viewport.
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // Draw the background texture filling the entire world.
        // NOTE: Make sure that the SpriteBatch is begun and ended within this method,
        // or that it is managed appropriately by the caller.
        batch.begin();
        batch.draw(bgTexture, 0, 0, worldWidth, worldHeight);
        batch.end();
    }

    /**
     * Releases the resources used by this background.
     */
    public void dispose() {
        if (bgTexture != null) {
            bgTexture.dispose();
        }
    }
}
