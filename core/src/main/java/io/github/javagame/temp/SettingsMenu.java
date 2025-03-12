package io.github.javagame.temp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SettingsMenu extends Menu {

    private Game game;
    // Stub current volume level (e.g., 5 out of 10)
    private int currentVolume = 5;

    public SettingsMenu(Game game) {
        super("Settings Menu");
        this.game = game;

        // Clear any default layout from the base Menu
        rootTable.clear();

        // Title for the Settings screen
        Label settingsTitle = new Label("SETTINGS", skin);
        settingsTitle.setFontScale(2f);
        settingsTitle.setColor(1, 1, 1, 1);
        rootTable.add(settingsTitle).padBottom(30);
        rootTable.row();

        // Sound section header
        Label soundLabel = new Label("Sound", skin);
        soundLabel.setFontScale(1.5f);
        soundLabel.setColor(1, 1, 1, 1);
        rootTable.add(soundLabel).padBottom(10);
        rootTable.row();

        // Volume bar (stub)
        Table volumeTable = new Table();
        // Create 10 "dot" indicators for volume levels
        for (int i = 1; i <= 10; i++) {
            Label dot = new Label("■", skin);
            // Filled levels are white, empty ones are gray
            if (i <= currentVolume) {
                dot.setColor(Color.WHITE);
            } else {
                dot.setColor(Color.GRAY);
            }
            // Add slight padding to simulate a bit of an offset/crooked feel
            volumeTable.add(dot).pad(2).padRight(4);
        }
        rootTable.add(volumeTable).padBottom(30);
        rootTable.row();

        // Controls section header
        Label controlsLabel = new Label("Controls", skin);
        controlsLabel.setFontScale(1.5f);
        controlsLabel.setColor(1, 1, 1, 1);
        rootTable.add(controlsLabel).padBottom(10);
        rootTable.row();

        // Stub for control mappings
        Table controlsTable = new Table();
        Label moveLabel = new Label("Move: A/D or Left/Right", skin);
        Label jumpLabel = new Label("Jump: Space", skin);
        Label interactLabel = new Label("Interact: E", skin);
        Label lol = new Label("this is all just temporary testing lol", skin);
        controlsTable.add(moveLabel).left().pad(5);
        controlsTable.row();
        controlsTable.add(jumpLabel).left().pad(5);
        controlsTable.row();
        controlsTable.add(interactLabel).left().pad(5);
        controlsTable.add(lol).left().pad(5);
        rootTable.add(controlsTable).padBottom(30);
        rootTable.row();

        // Back button to return to the main menu
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });
        rootTable.add(backButton).width(150).padTop(20);
    }
}
