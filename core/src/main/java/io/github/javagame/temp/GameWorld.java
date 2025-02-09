package io.github.javagame.temp;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld {
    private final Rectangle floor;
    private final Array<Rectangle> platforms;
    private final Player player;
    private final BitmapFont font;
    private final SpriteBatch batch;

    public GameWorld() {
        // Define floor
        floor = new Rectangle(0, 0, 800, 50);

        // Define platforms
        platforms = new Array<>();
        platforms.add(new Rectangle(200, 150, 150, 20));
        platforms.add(new Rectangle(450, 250, 150, 20));

        // Initialize player
        player = new Player(100, 60);

        // Initialize text rendering
        font = new BitmapFont();
        batch = new SpriteBatch();
    }

    public void update() {
        player.update();
        checkCollisions();
    }

    private void checkCollisions() {
        // Prevent player from falling through platforms
        for (Rectangle platform : platforms) {
            if (player.getBounds().overlaps(platform) && player.getVelocity().y <= 0) {
                player.getBounds().y = platform.y + platform.height;
                player.setOnGround(true);
                player.getVelocity().y = 0;
                return;
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw floor
        shapeRenderer.setColor(0, 0, 0, 1); // Black
        shapeRenderer.rect(floor.x, floor.y, floor.width, floor.height);

        // Draw platforms
        shapeRenderer.setColor(1, 1, 1, 1); // White
        for (Rectangle platform : platforms) {
            shapeRenderer.rect(platform.x, platform.y, platform.width, platform.height);
        }

        // Draw player
        shapeRenderer.setColor(0, 1, 0, 1); // Green placeholder
        shapeRenderer.rect(player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);

        shapeRenderer.end();

        // Render text message
        batch.begin();
        font.draw(batch, "platforms r bugged lol. gotta jump off. try fixing?", 300, 460);
        batch.end();
    }
}
