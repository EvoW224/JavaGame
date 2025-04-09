package io.github.javagame.temp.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.javagame.temp.WorldLogic;
import io.github.javagame.temp.Main;

public class PauseMenu extends Menu {
    /*
     private Game game;
    private WorldLogic world;

    public PauseMenu(Game game, WorldLogic world) {
        super("Pause Menu");
        this.game = game;
        this.world = world;
        rootTable.clear();

        Label pauseLabel = new Label("PAUSED", skin);
        pauseLabel.setFontScale(2.5f);
        pauseLabel.setColor(1, 1, 1, 1);
        rootTable.add(pauseLabel).padBottom(50);
        rootTable.row();

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(world);
            }
        });
        rootTable.add(resumeButton).width(200).pad(10);
        rootTable.row();

        TextButton exitButton = new TextButton("Exit to Main Menu", skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MainMenu(game, ((Main)game).getBatch()));
            }
        });
        rootTable.add(exitButton).width(200).pad(10);
    }

     */
}
