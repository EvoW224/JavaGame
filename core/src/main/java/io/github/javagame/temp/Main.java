package io.github.javagame.temp;

import com.badlogic.gdx.Game;

public class Main extends Game {

    @Override
    public void create() {
        // Start with the Main Menu screen.
        setScreen(new MainMenu(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
