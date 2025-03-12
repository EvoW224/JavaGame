package io.github.javagame.temp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MainMenu extends io.github.javagame.temp.Menu {

    private Game game;

    public MainMenu(Game game) {
        super("Main Menu");
        this.game = game;

        // Clear any existing layout from the base Menu
        rootTable.clear();

        // Create and style the title label.
        // skin comes from the assets folder, default libGDX stuff
        // check out Menu for details on how to work that.
        // can import custom assets if want
        Label titleLabel = new Label("TRASH GAME", skin);
        titleLabel.setFontScale(3f); // Big and bold
        titleLabel.setColor(1, 1, 1, 1); // Light text color
        rootTable.add(titleLabel).padBottom(50);
        rootTable.row();
        Label lol2 = new Label("Start Game does nothing rn, working on game logic separate.", skin);
        rootTable.add(lol2).padBottom(50);
        rootTable.row();

        // Create the buttons.
        // A lot of the magic here happens in Menu if you wanna check that out.
        // Basically you just make another one of these for each button you want.
        TextButton startGameButton = new TextButton("Start Game", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitGameButton = new TextButton("Exit Game", skin);

        // Add listeners to the buttons.
        startGameButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                // TODO: switch to GameWorld/GameScreen (for now, this is a stub)
            }
        });

        settingsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new SettingsMenu(game));
            }
        });

        exitGameButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                com.badlogic.gdx.Gdx.app.exit();
            }
        });

        // Add the buttons to the table, center-justified.
        // this is where you add them and format them, but you need to initialize them somewhere else
        rootTable.add(startGameButton).width(200).pad(10);
        rootTable.row();
        rootTable.add(settingsButton).width(200).pad(10);
        rootTable.row();
        rootTable.add(exitGameButton).width(200).pad(10);
    }
}
