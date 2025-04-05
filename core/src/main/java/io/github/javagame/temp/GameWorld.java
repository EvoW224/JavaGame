package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld extends ScreenAdapter {
    private final com.badlogic.gdx.Game game;
    private final Rectangle floor;
    private final Array<Rectangle> platforms;
    private final Player player;
    private final BitmapFont font;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    public GameWorld(com.badlogic.gdx.Game game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();

        // Define floor.
        floor = new Rectangle(0, 0, 800, 50);

        // Define platforms.
        platforms = new Array<>();
        platforms.add(new Rectangle(200, 150, 150, 20));
        platforms.add(new Rectangle(450, 250, 150, 20));

        // Initialize player.
        player = new Player(6, 60);

        // Initialize text rendering.
        font = new BitmapFont();
    }

    public void update(float delta) {
        player.update(delta);
        checkCollisions();
    }

    private void checkCollisions() {
        for (Rectangle platform : platforms) {
            if (player.getBounds().overlaps(platform) && player.getVelocity().y <= 0) {
                player.getBounds().y = platform.y + platform.height;
                player.setOnGround(true);
                return;
            }
        }
    }

    @Override
    public void render(float delta) {
        // Check for Pause Menu activation.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseMenu(game, this));
            return;
        }

        update(delta);

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Draw floor.
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(floor.x, floor.y, floor.width, floor.height);
        // Draw platforms.
        shapeRenderer.setColor(1, 1, 1, 1);
        for (Rectangle platform : platforms) {
            shapeRenderer.rect(platform.x, platform.y, platform.width, platform.height);
        }
        // Draw player.
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(player.getBounds().x, player.getBounds().y,
            player.getBounds().width, player.getBounds().height);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "Press ESC to pause", 300, 460);
        batch.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
