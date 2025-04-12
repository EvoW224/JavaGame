package io.github.javagame.temp.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class Menu extends ScreenAdapter {

    protected Stage stage;
    protected Skin skin;
    protected Table rootTable;
    protected String menuName;

    public Menu(String menuName) {
        this.menuName = menuName;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        FileHandle skinFile = Gdx.files.internal("uiskin.json");
        skin = new Skin(skinFile);

        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
    }

    @Override
    public void render(float delta) {
        // Clear the screen before drawing the menu
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);

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
