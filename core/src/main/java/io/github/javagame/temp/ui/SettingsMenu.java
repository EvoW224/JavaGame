package io.github.javagame.temp.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SettingsMenu extends Menu {

    private Game game;
    private int currentVolume = 5; // Stub volume level

    public SettingsMenu(Game game) {
        super("Settings Menu");
        this.game = game;
        rootTable.clear();

        Label settingsTitle = new Label("SETTINGS", skin);
        settingsTitle.setFontScale(2f);
        settingsTitle.setColor(1, 1, 1, 1);
        rootTable.add(settingsTitle).padBottom(30);
        rootTable.row();

        Label soundLabel = new Label("Sound", skin);
        soundLabel.setFontScale(1.5f);
        soundLabel.setColor(1, 1, 1, 1);
        rootTable.add(soundLabel).padBottom(10);
        rootTable.row();

        Table volumeTable = new Table();
        for (int i = 1; i <= 10; i++) {
            Label dot = new Label("■", skin);
            dot.setColor(i <= currentVolume ? Color.WHITE : Color.GRAY);
            volumeTable.add(dot).pad(2).padRight(4);
        }
        rootTable.add(volumeTable).padBottom(30);
        rootTable.row();

        Label controlsLabel = new Label("Controls", skin);
        controlsLabel.setFontScale(1.5f);
        controlsLabel.setColor(1, 1, 1, 1);
        rootTable.add(controlsLabel).padBottom(10);
        rootTable.row();

        Table controlsTable = new Table();
        Label moveLabel = new Label("Move: A/D or Left/Right", skin);
        Label jumpLabel = new Label("Jump: Space", skin);
        Label interactLabel = new Label("Interact: E", skin);
        controlsTable.add(moveLabel).left().pad(5);
        controlsTable.row();
        controlsTable.add(jumpLabel).left().pad(5);
        controlsTable.row();
        controlsTable.add(interactLabel).left().pad(5);
        rootTable.add(controlsTable).padBottom(30);
        rootTable.row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
              //  game.setScreen(new MainMenu(game, ((Main)game).getBatch()));
            }
        });
        rootTable.add(backButton).width(150).padTop(20);
    }


}
