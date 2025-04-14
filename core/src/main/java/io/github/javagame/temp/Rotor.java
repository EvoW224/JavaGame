package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Rotor extends Character {
    private float patrolSpeed;
    private float patrolDistance;
    private float startX;
    private boolean movingRight;
    private float detectionRange;
    private boolean isChasing;
    private float attackRange;
    private float attackCooldown;
    private float currentCooldown;
    // This flag indicates the rotor's current movement direction.
    // If true, the rotor moves to the right; if false, to the left.
    private boolean goingRight = true;
    ArrayList<Projectiles> gunShot;
    private boolean PlayerFound;

    // --- New animation fields for the flying animation ---
    private Animation<TextureRegion> flyAnimation;
    private float flyStateTime;
    // Adjust these constants based on your "GoomBot_Flying.png" asset.
    private static final int FLY_FRAME_COLS = 30; // For example, 20 frames across
    private static final int FLY_FRAME_ROWS = 1;    // 1 row

    public Rotor(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture textureFile, ArrayList<Projectiles> bulletArray) {
        // Call the superclass constructor.
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, textureFile);
        System.out.println("Rotor constructor called");
        System.out.println("Texture file: " + (textureFile != null));
        System.out.println("CharacterSprite exists: " + (CharacterSprite != null));

        // Initialize movement properties.
        this.patrolSpeed = maxSpeed * 0.5f;  // Moves at half max speed
        this.patrolDistance = 5f;            // Patrol distance
        this.startX = xspawn;
        this.movingRight = true;
        this.detectionRange = 10f;           // Detection range
        this.isChasing = false;
        this.attackRange = 2f;               // Attack range
        this.attackCooldown = 3f;            // Time between attacks
        this.currentCooldown = 0f;
        this.gunShot = bulletArray;
        this.PlayerFound = false;

        // Initialize the rotor's flying animation.
        initFlyAnimation();

        // Set the origin of the sprite to its center for proper flipping.
        CharacterSprite.setOriginCenter();
    }

    /**
     * Initializes the flying animation using the "GoomBot_Flying.png" sprite sheet.
     */
    private void initFlyAnimation() {
        // Load the flying sprite sheet.
        Texture flySheet = new Texture(Gdx.files.internal("GoomBot_Flying.png"));
        int cols = FLY_FRAME_COLS;
        int rows = FLY_FRAME_ROWS;
        // Split the sprite sheet into a 2D array of TextureRegions.
        TextureRegion[][] tmp = TextureRegion.split(flySheet,
            flySheet.getWidth() / cols,
            flySheet.getHeight() / rows);
        // Flatten the 2D array into a 1D array.
        TextureRegion[] flyFrames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flyFrames[index++] = tmp[i][j];
            }
        }
        Array<TextureRegion> flyFrameArray = new Array<>(flyFrames);
        // Create a looping animation for the flying animation.
        flyAnimation = new Animation<>(0.08f, flyFrameArray, Animation.PlayMode.LOOP);
        flyStateTime = 0f;
        // Set the initial frame.
        CharacterSprite.setRegion(flyFrameArray.get(0));
        // Optionally, if you don't need the flySheet after, you can dispose it:
        // flySheet.dispose();
    }

    /**
     * Updates the rotor’s behavior and animation.
     * Uses the flight animation (instead of an idle animation).
     *
     * @param deltaTime The time elapsed since the last frame.
     * @param leftbound The left boundary for patrol.
     * @param rightbound The right boundary for patrol.
     * @param Target    The player character.
     */
    public void update(float deltaTime, float leftbound, float rightbound, PC Target) {
        // Update the flying animation timer and set the current frame.
        flyStateTime += deltaTime;
        TextureRegion currentFrame = flyAnimation.getKeyFrame(flyStateTime, true);
        CharacterSprite.setRegion(currentFrame);

        // Clamp the rotor's sprite position within the viewport bounds.
        CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
        CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));

        // Update attack cooldown.
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }

        // Detection and chase logic.
        if (Target != null) {
            if ((Math.abs(Target.CharacterSprite.getX() - this.CharacterSprite.getX()) < detectionRange + 3f)
                && (Math.abs(Target.CharacterSprite.getY() - this.CharacterSprite.getY()) < 7f)) {
                PlayerFound = true;
            }
            if (PlayerFound) {
                // Horizontal movement: move towards the player's x position.
                if (Target.CharacterSprite.getX() > this.CharacterSprite.getX()) {
                    this.CharacterSprite.translateX(maxSpeed * deltaTime);
                    goingRight = true;
                } else {
                    this.CharacterSprite.translateX(-maxSpeed * deltaTime);
                    goingRight = false;
                }
                // Vertical movement: simple logic to approach player's vertical position.
                if (Math.abs(Target.CharacterSprite.getY() + Target.CharacterSprite.getHeight() - this.CharacterSprite.getY()) <= 1f) {
                    this.CharacterSprite.translateY(0f);
                    yspeed = 0f;
                } else if (Target.onAir && (Target.CharacterSprite.getY() > this.CharacterSprite.getY())
                    || Target.onGround && (Target.CharacterSprite.getY() + Target.CharacterSprite.getHeight() > this.CharacterSprite.getY())) {
                    this.CharacterSprite.translateY(maxSpeed * deltaTime);
                } else {
                    this.CharacterSprite.translateY(-maxSpeed * deltaTime);
                }
                this.attack(Target);
            } else {
                // If the player has not been found, patrol.
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

        // Update hitbox position.
        CharacterHitbox.x = CharacterSprite.getX();
        CharacterHitbox.y = CharacterSprite.getY();

        // Process collisions with projectiles in the gunShot list.
        for (int i = gunShot.size() - 1; i >= 0; i--) {
            if (gunShot.get(i).entityOverlap(this.CharacterHitbox)) {
                gunShot.get(i).shouldRemove = true;
                this.HitPoints -= 10;
                System.out.println("Rotor HP: " + this.HitPoints);
            }
        }

        // --- Orientation/Flipping Logic ---
        // For Rotor, if it is moving right (PlayerFound and player's x > rotor's x, or while patrolling)
        // ensure that the sprite faces right; otherwise, face left.
        if (goingRight) {
            if (!CharacterSprite.isFlipX()) {
                // If not already flipped, flip to face right.
                CharacterSprite.flip(true, false);
            }
        } else {
            if (CharacterSprite.isFlipX()) {
                // If currently flipped, flip back to face left.
                CharacterSprite.flip(true, false);
            }
        }
    }

    public void attack(PC player) {
        if (player != null) {
            //System.out.printf("Current Cooldown: %f\n", currentCooldown);
            if (currentCooldown <= 0) {
                float distanceToPlayer = Math.abs(player.CharacterSprite.getX() - this.CharacterSprite.getX());
                if (distanceToPlayer <= attackRange) {
                    player.takeDamage(DamageStat);
                    currentCooldown += attackCooldown;
                    System.out.printf("Player took %d damage, %d HP left\n", DamageStat, player.HitPoints);
                    if (this.CharacterSprite.getX() > player.CharacterSprite.getX()) {
                        player.recoil(false);
                    } else {
                        player.recoil(true);
                    }
                }
            }
        }
    }

    // Getters for rotor-specific properties.
    public float getPatrolSpeed() { return patrolSpeed; }
    public float getDetectionRange() { return detectionRange; }
    public float getAttackRange() { return attackRange; }
    public boolean isChasing() { return isChasing; }
}
