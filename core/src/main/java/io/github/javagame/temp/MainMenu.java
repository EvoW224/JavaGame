package io.github.javagame.temp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import io.github.javagame.temp.utils.TransitionScreen;

public class MainMenu extends Menu {
    private Game game;
    private SpriteBatch batch;

    // Fields for the background animation.
    private TextureAtlas bgAtlas;
    private Animation<AtlasRegion> bgAnimation;
    private float bgStateTime;

    // Constructor accepts both a Game instance and a shared SpriteBatch.
    public MainMenu(Game game, SpriteBatch batch) {
        super("Main Menu");
        this.game = game;
        this.batch = batch;
        rootTable.clear();

        // Load the texture atlas from your packed folder.
        // Use a path relative to the assets folder (no leading slash).
        bgAtlas = new TextureAtlas(Gdx.files.internal("packedBG/mainBG.atlas"));

        // Build an array of frames using the region names exactly as in your atlas.
        Array<AtlasRegion> frames = new Array<>();
        frames.add(bgAtlas.findRegion("bgMain0_delay-0.17s"));
        frames.add(bgAtlas.findRegion("bgMain6_delay-0.17s"));
        // If you have more unique frames, add them in the order you desire.
        // For example:
        // frames.add(bgAtlas.findRegion("bgMain1_delay-0.17s"));
        // frames.add(bgAtlas.findRegion("bgMain2_delay-0.17s"));
        // ... etc.

        // Create a looping animation with a frame duration (adjust as desired).
        bgAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        bgStateTime = 0f;

        // Create and style the title label.
        Label titleLabel = new Label("TRASH GAME", skin);
        titleLabel.setFontScale(3f); // Larger font for bold appearance
        titleLabel.setColor(1, 1, 1, 1);
        rootTable.add(titleLabel).padBottom(50);
        rootTable.row();

        // Create the buttons.
        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitGameButton = new TextButton("Exit Game", skin);

        // Listener for Start Game: switch screens using a transition.
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                // TransitionScreen will fade for 1 second then switch to WorldLogic.
                game.setScreen(new TransitionScreen(game, (Screen) new GameWorld(game, batch), 1f, batch));
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new SettingsMenu(game));
            }
        });

        exitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                Gdx.app.exit();
            }
        });

        rootTable.add(startGameButton).width(200).pad(10);
        rootTable.row();
        rootTable.add(settingsButton).width(200).pad(10);
        rootTable.row();
        rootTable.add(exitGameButton).width(200).pad(10);
    }

    @Override
    public void render(float delta) {
        // Clear the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the animation state.
        bgStateTime += delta;
        TextureRegion currentFrame = bgAnimation.getKeyFrame(bgStateTime, true);

        // Draw the background animation covering the entire screen.
        batch.begin();
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw the UI on top.
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bgAtlas.dispose();
    }
}
