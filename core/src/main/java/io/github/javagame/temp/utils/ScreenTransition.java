package io.github.javagame.temp.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenTransition extends ScreenAdapter {
    private Game game;
    private com.badlogic.gdx.Screen targetScreen;
    private float transitionTime;
    private float elapsed;
    private SpriteBatch batch;
    private Texture whitePixel;
    private boolean transitioning;
    private boolean switched;

    // Constructor: expects a Game, a target Screen, a transition time, and a shared SpriteBatch.
    public ScreenTransition(Game game, com.badlogic.gdx.Screen targetScreen, float transitionTime, SpriteBatch batch) {
        this.game = game;
        this.targetScreen = targetScreen;
        this.transitionTime = transitionTime;
        this.elapsed = 0;
        this.batch = batch;
        this.whitePixel = createWhitePixel();
        this.transitioning = false;
        this.switched = false;
    }

    // Call this to start the fade transition.
    public void startTransition() {
        this.elapsed = 0;
        this.transitioning = true;
        this.switched = false;
    }

    // Returns true while the transition is still active.
    public boolean isTransitioning() {
        return transitioning && (elapsed < transitionTime);
    }

    // Update the transition progress.
    public void update(float delta) {
        if (!transitioning) return;
        elapsed += delta;
        if (elapsed >= transitionTime && !switched) {
            switched = true;
            // Once the fade is complete, switch to the target screen.
            game.setScreen(targetScreen);
            transitioning = false;
        }
    }

    // Renders the fade overlay.
    public void renderOverlay() {
        if (!transitioning) return;
        batch.begin();
        float alpha = Math.min(elapsed / transitionTime, 1);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(whitePixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        batch.setColor(1, 1, 1, 1); // Reset the color.
    }

    private Texture createWhitePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void dispose() {
        whitePixel.dispose();
    }
}
