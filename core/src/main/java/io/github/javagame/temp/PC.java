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
    // Constants for the idle sprite sheet.
    private static final int IDLE_FRAME_COLS = 56;
    private static final int IDLE_FRAME_ROWS = 1;
    public boolean facingRight = true;

    // Animation fields for the jump state.
    private Animation<TextureRegion> jumpAnimation;
    private float jumpStateTime = 0f;
    // Constants for the jump sprite sheet.
    private static final int JUMP_FRAME_COLS = 47;
    private static final int JUMP_FRAME_ROWS = 1;

    // Animation fields for the run state.
    private Animation<TextureRegion> runAnimation;
    private float runStateTime = 0f;
    // Constants for the run sprite sheet.
    // Adjust these values (e.g. number of columns/rows) according to your "HeroBot_Run.png" asset.
    private static final int RUN_FRAME_COLS = 23;   // for example
    private static final int RUN_FRAME_ROWS = 1;

    public PC(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture CTexture) {
        // Call the superclass constructor (which sets up the character using CTexture).
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, CTexture);
        // Initialize animations.
        initIdleAnimation();
        initJumpAnimation();
        initRunAnimation();
    }

    private void initIdleAnimation() {
        // Load the idle sprite sheet.
        Texture idleSheet = new Texture(Gdx.files.internal("HeroBot_Idle.png"));
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
        idleAnimation = new Animation<>(0.15f, idleFrameArray, Animation.PlayMode.LOOP);
        idleStateTime = 0f;
        CharacterSprite.setRegion(idleFrameArray.get(0));
        // Optionally: idleSheet.dispose();
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
        // Optionally: jumpSheet.dispose();
    }

    private void initRunAnimation() {
        // Load the run sprite sheet.
        Texture runSheet = new Texture(Gdx.files.internal("HeroBot_Run.png"));
        int cols = RUN_FRAME_COLS;
        int rows = RUN_FRAME_ROWS;
        TextureRegion[][] tmp = TextureRegion.split(runSheet,
            runSheet.getWidth() / cols,
            runSheet.getHeight() / rows);
        TextureRegion[] runFrames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                runFrames[index++] = tmp[i][j];
            }
        }
        Array<TextureRegion> runFrameArray = new Array<>(runFrames);
        // Create a looping run animation.
        runAnimation = new Animation<>(0.02f, runFrameArray, Animation.PlayMode.LOOP);
        runStateTime = 0f;
        // Do not set the region here; update() will select the animation based on movement.
        // Optionally: runSheet.dispose();
    }

    public void update(float deltaTime, ArrayList<Projectiles> projectiles) {
        // Decide which animation to play:
        // If the character is in the air, play jump animation.
        // Else if the horizontal speed is significantly nonzero (i.e. the player is moving), play run animation.
        // Otherwise, play idle animation.
        if (onAir) {
            jumpStateTime += deltaTime;
            TextureRegion currentJumpFrame = jumpAnimation.getKeyFrame(jumpStateTime, false);
            CharacterSprite.setRegion(currentJumpFrame);
            // Also reset runStateTime to prevent run animation from continuing in the background.
            runStateTime = 0f;
        } else if (Math.abs(speed) > 0.1f) {
            // Player is moving on the ground – use run animation.
            runStateTime += deltaTime;
            TextureRegion currentRunFrame = runAnimation.getKeyFrame(runStateTime, true);
            CharacterSprite.setRegion(currentRunFrame);
            // Reset idleStateTime so idle animation restarts when the player stops moving.
            idleStateTime = 0f;
        } else {
            idleStateTime += deltaTime;
            TextureRegion currentIdleFrame = idleAnimation.getKeyFrame(idleStateTime, true);
            CharacterSprite.setRegion(currentIdleFrame);
            // Also reset runStateTime.
            runStateTime = 0f;
            jumpStateTime = 0f;
        }

        // Update sprite flipping based on movement direction:
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            facingRight = false;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            facingRight = true;
        }
        // Center origin for flipping.
        CharacterSprite.setOriginCenter();
        CharacterSprite.setScale(facingRight ? 1f : -1f, 1f);

        // Clamp the player's position within the viewport.
        CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
        CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));

        if (!takingRecoil) {
            // Blaster attack.
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && gunTimer < 0.0f) {
                float px = (facingRight) ? this.CharacterSprite.getX() + this.CharacterSprite.getWidth() : this.CharacterSprite.getX();
                float py = this.CharacterSprite.getY() + this.CharacterSprite.getHeight() / 2f;
                projectiles.add(new Projectiles(this.viewport, projectileTexture, px, py, this.viewport.getWorldWidth(), this));
                gunTimer = 0.4f;
            }
            gunTimer -= deltaTime;
            if (gunTimer < -1000000f) { gunTimer = -1f; }

            // Handle horizontal movement.
            if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                this.speed += dvdt;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                this.speed -= dvdt;
            } else {
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
                    this.yspeed = jumpStrength;
                    onAir = true;
                    onGround = false;
                    jumpTimer = 0.15f;
                } else if (onAir && jumpTimer > 0) {
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
