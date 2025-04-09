package io.github.javagame.temp.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TransitionScreen extends ScreenAdapter {
   /* private Game game;
    private com.badlogic.gdx.Screen targetScreen;
    private float transitionTime;
    private float elapsed;
    private SpriteBatch batch;
    private Texture whitePixel;

    public TransitionScreen(Game game, com.badlogic.gdx.Screen targetScreen, float transitionTime, SpriteBatch batch) {
        this.game = game;
        this.targetScreen = targetScreen;
        this.transitionTime = transitionTime;
        this.elapsed = 0;
        this.batch = batch;
        this.whitePixel = createWhitePixel();
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
    public void render(float delta) {
        elapsed += delta;
        float alpha = Math.min(elapsed / transitionTime, 1f);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(1, 1, 1, alpha);
        batch.draw(whitePixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        batch.setColor(1, 1, 1, 1); // Reset color

        if (elapsed >= transitionTime) {
            game.setScreen(targetScreen);
        }
    }

    @Override
    public void dispose() {
        whitePixel.dispose();
    }

    */
}
