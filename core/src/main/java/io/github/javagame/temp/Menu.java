package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class Menu extends ScreenAdapter {
// all the staging and UI elements, skin is for the visual appearance (font, buttons, etc.)
// table is for formatting and consistent spacing
    protected Stage stage;
    protected Skin skin;
    protected Table rootTable;
    protected String menuName;

    public Menu(String menuName) {
        this.menuName = menuName;

        // Create the stage with a viewport that automatically adjusts to screen size
        stage = new Stage(new ScreenViewport());
        // Set this stage as the input processor so that UI events are handled by Scene2D
        Gdx.input.setInputProcessor(stage);

        // Load a common skin for the UI.
        FileHandle skinFile = Gdx.files.internal("uiskin.json"); // change the UI elements here.
        skin = new Skin(skinFile);

        // Create a root table that will be the main container for your UI elements.
        rootTable = new Table();
        // Fill the entire stage to handle different screen sizes/resolutions
        rootTable.setFillParent(true);
        // Optionally, enable debugging on the table during development:
        // rootTable.setDebug(true);
        stage.addActor(rootTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {

        stage.clear();

        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
