package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class PC extends Character {

    ShapeRenderer HPBar = new ShapeRenderer();
    boolean takingRecoil = false;
    float recoilTimer = 0;
    Texture projectileTexture = new Texture("libgdx.png");
    float gunTimer = 0.4f;

    // Animation fields for the idle state.
    private Animation<TextureRegion> idleAnimation;
    private float idleStateTime = 0f;
    // Constants for the idle sprite sheet (adjust as needed).
    private static final int IDLE_FRAME_COLS = 56;
    private static final int IDLE_FRAME_ROWS = 1;
    private boolean facingRight = true;

    // Animation fields for the jump state.
    private Animation<TextureRegion> jumpAnimation;
    private float jumpStateTime = 0f;
    // Constants for the jump sprite sheet (adjust these based on your asset).
    private static final int JUMP_FRAME_COLS = 47;
    private static final int JUMP_FRAME_ROWS = 1;

    // Field to track which direction the character is facing.

    public PC(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture CTexture) {
        // Call the superclass constructor (which sets up the character using CTexture).
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, CTexture);
        // Initialize idle and jump animations.
        initIdleAnimation();
        initJumpAnimation();
    }

    private void initIdleAnimation() {
        // Load the idle sprite sheet.
        Texture idleSheet = new Texture(Gdx.files.internal("HeroBot_Idle.png"));
        // There are 56 columns and 1 row.
        int cols = IDLE_FRAME_COLS;
        int rows = IDLE_FRAME_ROWS;
        TextureRegion[][] tmp = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / cols,
                idleSheet.getHeight() / rows);
        TextureRegion[] idleFrames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                idleFrames[index++] = tmp[i][j];
            }
        }
        Array<TextureRegion> idleFrameArray = new Array<>(idleFrames);
        // Create a looping animation.
        idleAnimation = new Animation<>(0.15f, idleFrameArray, Animation.PlayMode.LOOP);
        idleStateTime = 0f;
        CharacterSprite.setRegion(idleFrameArray.get(0));
        // Optionally, idleSheet.dispose();
    }

    private void initJumpAnimation() {
        // Load the jump sprite sheet.
        Texture jumpSheet = new Texture(Gdx.files.internal("HeroBot_Jump.png"));
        int cols = JUMP_FRAME_COLS;
        int rows = JUMP_FRAME_ROWS;
        TextureRegion[][] tmp = TextureRegion.split(jumpSheet,
                jumpSheet.getWidth() / cols,
                jumpSheet.getHeight() / rows);
        TextureRegion[] jumpFrames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                jumpFrames[index++] = tmp[i][j];
            }
        }
        Array<TextureRegion> jumpFrameArray = new Array<>(jumpFrames);
        jumpAnimation = new Animation<>(0.03f, jumpFrameArray, Animation.PlayMode.NORMAL);
        jumpStateTime = 0f;
        // Do not set the region here; update() will handle it.
        // Optionally, jumpSheet.dispose();
    }


    public void update(float deltaTime, ArrayList<Projectiles> projectiles) {
        // First, update animation based on jump state:
        if (onAir) {
            jumpStateTime += deltaTime;
            TextureRegion currentJumpFrame = jumpAnimation.getKeyFrame(jumpStateTime, false);
            CharacterSprite.setRegion(currentJumpFrame);
        } else {
            idleStateTime += deltaTime;
            TextureRegion currentIdleFrame = idleAnimation.getKeyFrame(idleStateTime, true);
            CharacterSprite.setRegion(currentIdleFrame);
            jumpStateTime = 0f;
        }

        // Update sprite flipping based on movement direction:
        // If the "A" key is pressed (moving left), ensure the sprite is flipped horizontally.
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            facingRight = false; // Player wants to move left.
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            facingRight = true;  // Player wants to move right.
        }
        // Ensure the sprite's origin is centered so that flipping works correctly:
        CharacterSprite.setOriginCenter();
        // Apply the scale flip based on the facing flag.
        CharacterSprite.setScale(facingRight ? 1f : -1f, 1f);

        // Clamp character within screen bounds.
        CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
        CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));

        if (!takingRecoil) {
            // Blaster Attack.
            //Blaster Attack
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && gunTimer < 0.0f) {
                float px = (facingRight) ? this.CharacterSprite.getX() + this.CharacterSprite.getWidth() : this.CharacterSprite.getX();
                float py = this.CharacterSprite.getY() + this.CharacterSprite.getHeight() / 2f;

                projectiles.add(new Projectiles(this.viewport, projectileTexture, px, py, this.viewport.getWorldWidth(),this));
                gunTimer = 0.4f;
            }
            gunTimer -= deltaTime;

            if (gunTimer < -1000000f) {gunTimer = -1f;}
            // Handle horizontal movement.
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                this.speed += dvdt;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.speed -= dvdt;
            } else {
                // Deceleration.
                if (speed > 0f) {
                    this.speed -= dvdt;
                    if (speed < 0f) speed = 0f;
                } else if (speed < 0f) {
                    this.speed += dvdt;
                    if (speed > 0f) speed = 0f;
                }
                if (Math.abs(speed) <= 0.3f) speed = 0f;
            }

            if (this.speed > this.maxSpeed) this.speed = this.maxSpeed;
            if (this.speed < -this.maxSpeed) this.speed = -this.maxSpeed;
            if (Math.abs(this.yspeed) > this.terminalVelocity) {
                this.yspeed = this.terminalVelocity * Math.signum(this.yspeed);
            }

            // Ground collision check.
            if (CharacterSprite.getY() <= 1f) {
                CharacterSprite.setY(1f);
                this.yspeed = 0f;
                this.onAir = false;
                this.onGround = true;
                this.jumpTimer = 0f;
            }

            // Jumping logic.
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (onGround) {
                    // Start jump.
                    this.yspeed = jumpStrength;
                    onAir = true;
                    onGround = false;
                    jumpTimer = 0.15f;
                } else if (onAir && jumpTimer > 0) {
                    // Continue jumping.
                    this.yspeed = Math.min(this.yspeed + jumpStrength * 0.5f, jumpStrength * 1.5f);
                    jumpTimer -= deltaTime;
                }
            } else {
                if (onAir && this.yspeed > 0) {
                    this.yspeed *= 0.5f;
                }
            }
        }

        // Apply movement.
        CharacterSprite.translateX(speed * deltaTime);
        CharacterSprite.translateY(yspeed * deltaTime);

        if (onAir) {
            this.yspeed -= this.gravity;
        }

        if (CharacterSprite.getY() <= 1f) {
            CharacterSprite.setY(1f);
            this.yspeed = 0f;
            this.onAir = false;
            this.onGround = true;
            this.jumpTimer = 0f;
        }
        if (takingRecoil) {
            if (speed > 0f) {
                this.speed -= dvdt;
                if (speed < 0f) speed = 0f;
            } else if (speed < 0f) {
                this.speed += dvdt;
                if (speed > 0f) speed = 0f;
            }
            if (Math.abs(speed) <= 0.3f) speed = 0f;
        }

        if (recoilTimer > 0.6f) {
            takingRecoil = false;
            recoilTimer = 0.0f;
        }
        if (takingRecoil) {
            recoilTimer += deltaTime;
        }

        // Update hitbox position.
        CharacterHitbox.x = CharacterSprite.getX();
        CharacterHitbox.y = CharacterSprite.getY();

        System.out.println(takingRecoil + " " + recoilTimer);
    }


    public void renderHPBar() {
        HPBar.begin(ShapeRenderer.ShapeType.Filled);
        HPBar.setColor(Color.RED);
        HPBar.rect(1, 4, 1, 0.2f);
    }

    public void endHpBar() {
        HPBar.end();
    }

    public void takeDamage(int damage) {
        this.HitPoints -= damage;
    }

    public void recoil(boolean toTheRight) {
        takingRecoil = true;
        this.yspeed = jumpStrength * 1.4f;
        this.speed = -1f * (this.speed);
        onAir = true;
        onGround = false;
    }
}
