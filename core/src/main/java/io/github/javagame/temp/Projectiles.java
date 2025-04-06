package io.github.javagame.temp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Projectiles extends Character {
    private final float speed = 10f;
    private final float maxDistance;
    public boolean shouldRemove = false;

    public Projectiles (Viewport viewport, Texture textureFile, float spawnPositionx, float spawnPositiony, float screenWidth) {
        super(viewport, 0f, 0.05f, 0.05f, spawnPositionx, spawnPositiony, 1, 1, textureFile);
        this.CharacterSprite.setSize(0.0f, 0.5f);
        this.maxDistance = screenWidth / 2f;  // Travel half screen
    }

    public void update(float deltaTime) {
        CharacterSprite.translateX(speed * deltaTime);

        // Update hitbox and position
        CharacterHitbox.x = CharacterSprite.getX();
        CharacterHitbox.y = CharacterSprite.getY();

        // Mark for removal if traveled far enough
        if (this.getXPosition() - this.position.x >= maxDistance) {
            shouldRemove = true;
        }
    }

    public void draw(SpriteBatch batch) {
        CharacterSprite.draw(batch);
    }
}
