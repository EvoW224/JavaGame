package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private final Rectangle bounds;
    private final Vector2 velocity;
    private boolean onGround;

    private static final float GRAVITY = -0.5f;
    private static final float MOVE_SPEED = 5f;
    private static final float JUMP_FORCE = 12f;

    public Player(float x, float y) {
        bounds = new Rectangle(x, y, 32, 32);

        velocity = new Vector2(0, 0);

        onGround = false;
    }

    public void update(float delta) {
        // Apply gravity
        if (!onGround) {
            velocity.y += GRAVITY;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bounds.x -= MOVE_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bounds.x += MOVE_SPEED;
        }

        if (onGround && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            velocity.y = JUMP_FORCE;
            onGround = false;
        }

        // Apply vertical movement
        bounds.y += velocity.y;

        // Simulated ground collision
        if (bounds.y <= 50) { // Ground level
            bounds.y = 50;
            velocity.y = 0;
            onGround = true;
        }
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
