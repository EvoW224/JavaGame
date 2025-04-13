package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Enemy extends Character {
    private float patrolSpeed;
    private float patrolDistance;
    private float startX;
    private boolean movingRight;
    private float detectionRange;
    private boolean isChasing;
    private float attackRange;
    private float attackCooldown;
    private float currentCooldown;
    // This flag indicates the enemy's current movement direction.
    // If true, the enemy should face right; if false, the enemy should face left.
    private boolean goingRight = true;
    ArrayList<Projectiles> gunShot;

    // Animation fields for the enemy's idle state.
    private Animation<TextureRegion> idleAnimation;
    private float idleStateTime;
    // Constants for the idle sprite sheet.
    // Adjust these according to your "GoomBot_Ground.png" asset.
    private static final int ENEMY_FRAME_COLS = 30;
    private static final int ENEMY_FRAME_ROWS = 1;

    public Enemy(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture textureFile, ArrayList<Projectiles> bulletArray) {
        // Call the superclass constructor.
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, textureFile);
        System.out.println("Enemy constructor called");
        System.out.println("Texture file passed in: " + (textureFile != null));
        System.out.println("CharacterSprite exists: " + (CharacterSprite != null));

        // Initialize movement properties.
        this.patrolSpeed = maxSpeed * 0.5f;
        this.patrolDistance = 5f;
        this.startX = xspawn;
        this.movingRight = true;
        this.detectionRange = 10f;
        this.isChasing = false;
        this.attackRange = 2f;
        this.attackCooldown = 3f;
        this.currentCooldown = 0f;
        gunShot = bulletArray;

        // Initialize the enemy idle animation.
        initIdleAnimation();



        // Set the origin of the sprite to its center for proper flipping.
        CharacterSprite.setOriginCenter();
    }

    private void initIdleAnimation() {
        // Load the idle sprite sheet.
        Texture idleSheet = new Texture(Gdx.files.internal("GoomBot_Ground.png"));
        // Split the texture into regions.
        TextureRegion[][] tmp = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / ENEMY_FRAME_COLS,
                idleSheet.getHeight() / ENEMY_FRAME_ROWS);
        // Flatten the 2D array into a 1D array.
        TextureRegion[] idleFrames = new TextureRegion[ENEMY_FRAME_COLS * ENEMY_FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < ENEMY_FRAME_ROWS; i++) {
            for (int j = 0; j < ENEMY_FRAME_COLS; j++) {
                idleFrames[index++] = tmp[i][j];
            }
        }
        Array<TextureRegion> frameArray = new Array<>(idleFrames);
        // Create a looping animation.
        idleAnimation = new Animation<>(0.05f, frameArray, Animation.PlayMode.LOOP);
        idleStateTime = 0f;
        // Set the initial frame.
        CharacterSprite.setRegion(frameArray.get(0));
        // Optionally dispose the idleSheet if no longer needed:
        // idleSheet.dispose();
    }

    /**
     * Updates the enemy’s behavior and animation.
     * @param deltaTime The time elapsed since the last frame.
     * @param leftbound The left boundary for patrol.
     * @param rightbound The right boundary for patrol.
     * @param Target The player character.
     */
    public void update(float deltaTime, float leftbound, float rightbound, PC Target) {
        // Save the current X position before moving.
        float prevX = CharacterSprite.getX();

        // Update idle animation timer and set current frame.
        idleStateTime += deltaTime;
        TextureRegion currentFrame = idleAnimation.getKeyFrame(idleStateTime, true);
        CharacterSprite.setRegion(currentFrame);

        // Clamp the enemy's sprite position within the viewport bounds.
        CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
        CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));

        // Update attack cooldown.
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }

        // Chase logic: if the target is close enough, move toward it.
        if (Target != null) {
            if ((Math.abs(Target.CharacterSprite.getX() - CharacterSprite.getX()) < detectionRange) &&
                (Math.abs(Target.CharacterSprite.getY() - CharacterSprite.getY()) < 1f)) {
                if (Target.CharacterSprite.getX() > CharacterSprite.getX()) {
                    CharacterSprite.translateX(maxSpeed * deltaTime);
                } else {
                    CharacterSprite.translateX(-maxSpeed * deltaTime);
                }
                attack(Target);
            } else {  // Patrol logic
                if (CharacterSprite.getX() > rightbound) {
                    goingRight = false;
                }
                if (CharacterSprite.getX() < leftbound) {
                    goingRight = true;
                }
                if (goingRight) {
                    CharacterSprite.translateX(patrolSpeed * deltaTime);
                } else {
                    CharacterSprite.translateX(-patrolSpeed * deltaTime);
                }
            }
        }

        // Gravity logic.
        if (CharacterSprite.getY() > 1f) { onAir = true; }
        if (Math.abs(yspeed) > terminalVelocity) {
            yspeed = terminalVelocity * Math.signum(yspeed);
        }
        if (CharacterSprite.getY() <= 1f) {
            CharacterSprite.setY(1f);
            yspeed = 0f;
            onAir = false;
            onGround = true;
            jumpTimer = 0f;
        }
        if (onAir) {
            yspeed -= gravity;
        }
        CharacterSprite.translateY(yspeed * deltaTime);

        // New Flip logic based on delta X

        float newX = CharacterSprite.getX();

        if (newX > prevX) {
            // Moving right.
            goingRight = true;
        } else if (newX < prevX) {
            // Moving left.
            goingRight = false;
        }
        // Set origin first for proper flipping.
        CharacterSprite.setOriginCenter();
        // Assuming the default (unflipped) sprite faces right.
        if (!goingRight) {
            if (CharacterSprite.isFlipX()) {
                CharacterSprite.flip(false, false);
            }
        } else {
            if (!CharacterSprite.isFlipX()) {
                CharacterSprite.flip(true, false);
            }
        }

        // Update hitbox position.
        CharacterHitbox.x = CharacterSprite.getX();
        CharacterHitbox.y = CharacterSprite.getY();

        // Process collisions with projectiles.
        for (int i = gunShot.size() - 1; i >= 0; i--) {
            if (gunShot.get(i).entityOverlap(CharacterHitbox)) {
                gunShot.get(i).shouldRemove = true;
                gunShot.get(i).HitPoints = 0;
                HitPoints -= 10;
                System.out.println(HitPoints);
            }
        }
    }

    public void attack(PC player) {
        if (player != null) {
            if (currentCooldown <= 0) {
                float distanceToPlayer = Math.abs(player.CharacterSprite.getX() - CharacterSprite.getX());
                if (distanceToPlayer <= attackRange) {
                    player.takeDamage(DamageStat);
                    currentCooldown += attackCooldown;
                    System.out.printf("Player took %d damage, %d HP left\n", DamageStat, player.HitPoints);
                    if (CharacterSprite.getX() > player.CharacterSprite.getX()) {
                        player.recoil(false);
                    } else {
                        player.recoil(true);
                    }
                }
            }
        }
    }

    // Getters for enemy-specific properties.
    public float getPatrolSpeed() { return patrolSpeed; }
    public float getDetectionRange() { return detectionRange; }
    public float getAttackRange() { return attackRange; }
    public boolean isChasing() { return isChasing; }
}

/*
public void update(float deltaTime, float leftbound, float rightbound,PC Target) {



    this.CharacterSprite.setX(MathUtils.clamp(this.CharacterSprite.getX(), 0, viewport.getWorldWidth() - this.CharacterSprite.getWidth()));
    this.CharacterSprite.setY(MathUtils.clamp(this.CharacterSprite.getY(), 0, viewport.getWorldHeight() - this.CharacterSprite.getHeight()));
    if (currentCooldown > 0) {
        currentCooldown -= deltaTime;
    }

    if (Target != null) {
        if ((Math.abs(Target.CharacterSprite.getX() - this.CharacterSprite.getX()) < detectionRange) && (Math.abs(Target.CharacterSprite.getY() - this.CharacterSprite.getY()) < 1f)) {
            if (Target.CharacterSprite.getX() > this.CharacterSprite.getX()) {
                this.CharacterSprite.translateX(maxSpeed * deltaTime);
            } else {
                this.CharacterSprite.translateX(-maxSpeed * deltaTime);
            }
            this.attack(Target);
        } else {
            if (this.CharacterSprite.getX() > rightbound) {
                goingRight = false;
            }
            if (this.CharacterSprite.getX() < leftbound) {
                goingRight = true;
            }
            if (goingRight) {
                this.CharacterSprite.translateX(patrolSpeed * deltaTime);
            } else {
                this.CharacterSprite.translateX(-patrolSpeed * deltaTime);
            }


        }


    }


    this.CharacterHitbox.x = CharacterSprite.getX();
    this.CharacterHitbox.y = CharacterSprite.getY();



    // Update hitbox position


    //this.position.set(CharacterSprite.getX(), CharacterSprite.getY());

    // Debug position
    // System.out.println("Enemy position: " + CharacterSprite.getX() + ", " + CharacterSprite.getY());
}


public void attack(PC player) {

}

 */
