package io.github.javagame.temp.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.audio.Music;
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
import io.github.javagame.temp.WorldLogic;
import io.github.javagame.temp.utils.TransitionScreen;

public class MainMenu extends Menu {
    private Game game;
    private SpriteBatch batch;
    private Music bgm;


    // Fields for the background animation.
    private TextureAtlas bgAtlas;
    private Animation<AtlasRegion> bgAnimation;
    private float bgStateTime;

    // Constructor: accepts a Game instance and a shared SpriteBatch.
    public MainMenu(Game game, SpriteBatch batch) {
        super("Main Menu");
        this.game = game;
        this.batch = batch;
        rootTable.clear();

        // Load music files!!
        bgm = Gdx.audio.newMusic(Gdx.files.internal("before.mp3"));

        // Load the texture atlas from your packed folder.
        bgAtlas = new TextureAtlas(Gdx.files.internal("packedBG/mainBG.atlas"));

        // Build an array of frames from the atlas.
        Array<AtlasRegion> frames = new Array<>();
        frames.add(bgAtlas.findRegion("bgMain0_delay-0.17s"));
        frames.add(bgAtlas.findRegion("bgMain1_delay-0.17s"));
        frames.add(bgAtlas.findRegion("bgMain2_delay-0.17s"));
        frames.add(bgAtlas.findRegion("bgMain3_delay-0.17s"));
        frames.add(bgAtlas.findRegion("bgMain4_delay-0.17s"));
        frames.add(bgAtlas.findRegion("bgMain5_delay-0.17s"));

        // Create a looping animation.
        bgAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);
        bgStateTime = 0f;

        // Style the title label.
        Label titleLabel = new Label("TRASH GAME", skin);
        titleLabel.setFontScale(5f);
        titleLabel.setColor(1, 1, 1, 1);
        rootTable.add(titleLabel).padBottom(350);
        rootTable.row();

        // Create the buttons.
        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitGameButton = new TextButton("Exit Game", skin);

        // Listener for Start Game: transition to the gameplay screen with a fade effect.
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                // Create a TransitionScreen that fades for 1 second then switches to the WorldLogic screen.
                game.setScreen(new TransitionScreen(game, new WorldLogic(game, batch), 1f, batch));
            }
        });

        // Listeners for Settings and Exit (implement as needed).
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                // TODO: Set to a Settings screen.
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


        // Music settings, for now! Stuff like volume, looping, pausing, etc.
        bgm.setVolume(0.5f);
        bgm.setLooping(true);
        bgm.play();
         /* other methods to maybe use
            bgm.stop();
            bgm.pause();
            set booleans like bgm.isPlaying() and bgm.isLooping();
            bgm.getPosition() to find where we are in the song in seconds
         */

        // Clear the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the background animation.
        bgStateTime += delta;
        TextureRegion currentFrame = bgAnimation.getKeyFrame(bgStateTime, true);
        batch.begin();
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw the UI.
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        bgAtlas.dispose();
        bgm.dispose();
    }
}
