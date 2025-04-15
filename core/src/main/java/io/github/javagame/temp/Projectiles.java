package io.github.javagame.temp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Projectiles extends Character {
    private float speed = 20f;
    private float maxDistance = 15f;
    public boolean shouldRemove = false;
    float xspawn;
    float yspawn;
    int direction;

    public Projectiles (Viewport viewport, Texture textureFile, float spawnPositionx, float spawnPositiony, float screenWidth, PC shooter) {
        super(viewport, 0f, 5f, 5f, spawnPositionx, spawnPositiony, 1, 1, textureFile);
        this.CharacterSprite.setSize(.5f, .5f);
        xspawn = spawnPositionx;
        yspawn = spawnPositiony;
        direction = (shooter.facingRight) ? 1 : -1;;
    }

    public void update(float deltaTime) {
        this.CharacterSprite.translateX(speed * deltaTime * direction);
        System.out.println("Bullet position x: " + this.CharacterHitbox.x);

        // Update hitbox and position
        CharacterHitbox.x = this.CharacterSprite.getX();
        CharacterHitbox.y = this.CharacterSprite.getY();

        // Mark for removal if traveled far enough
        if (Math.abs(this.CharacterSprite.getX() - this.xspawn )>= maxDistance) {
            shouldRemove = true;
            this.HitPoints = 0;
        }
    }


    public void draw(SpriteBatch batch) {
        CharacterSprite.draw(batch);
    }
}
