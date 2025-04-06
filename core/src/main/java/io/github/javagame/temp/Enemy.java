package io.github.javagame.temp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

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
    boolean goingRight = false;

    public Enemy(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture textureFile) {
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, textureFile);
        System.out.println("Enemy constructor called");
        System.out.println("Texture file: " + (textureFile != null));
        System.out.println("CharacterSprite: " + (CharacterSprite != null));

        this.patrolSpeed = maxSpeed * 0.5f;  // Enemy moves at half the max speed during patrol
        this.patrolDistance = 5f;  // Distance to patrol in each direction
        this.startX = xspawn;
        this.movingRight = true;
        this.detectionRange = 10f;  // Range at which enemy can detect player
        this.isChasing = false;
        this.attackRange = 2f;  // Range at which enemy can attack
        this.attackCooldown = 3f;  // Time between attacks
        this.currentCooldown = 0f;


        // Set initial sprite position
        /*if (CharacterSprite != null) {
            CharacterSprite.setPosition(xspawn, yspawn);
            System.out.println("Enemy sprite position set to: " + xspawn + ", " + yspawn);
        }*/
    }

    public void update(float deltaTime, float leftbound, float rightbound,PC Target) {

        CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
        CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }

        if ((Math.abs(Target.CharacterSprite.getX() - this.CharacterSprite.getX()) < detectionRange) && (Target.CharacterSprite.getY() - this.CharacterSprite.getY() < 1f) ) {
            if (Target.CharacterSprite.getX() > this.CharacterSprite.getX()) {this.CharacterSprite.translateX(maxSpeed * deltaTime);}
            else {this.CharacterSprite.translateX(-maxSpeed * deltaTime);}
            this.attack(Target);
        }
        else {
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

        // Update hitbox position
        CharacterHitbox.x = CharacterSprite.getX();
        CharacterHitbox.y = CharacterSprite.getY();

        //this.position.set(CharacterSprite.getX(), CharacterSprite.getY());

        // Debug position
       // System.out.println("Enemy position: " + CharacterSprite.getX() + ", " + CharacterSprite.getY());
    }


    public void attack(PC player) {
        System.out.printf("Current Cooldown: %f\n", currentCooldown);
        if (currentCooldown <= 0) {
            float distanceToPlayer = Math.abs(player.CharacterSprite.getX() - this.CharacterSprite.getX());
            if (distanceToPlayer <= attackRange) {
                player.takeDamage(DamageStat);
                currentCooldown += attackCooldown;
                System.out.printf("Player took %d damage, %d HP left\n", DamageStat, player.HitPoints);
                if (this.CharacterSprite.getX() > player.CharacterSprite.getX()) {player.recoil(false);}
                else {player.recoil(true);}
            }
        }
    }

    // Getters for enemy-specific properties
    public float getPatrolSpeed() { return patrolSpeed; }
    public float getDetectionRange() { return detectionRange; }
    public float getAttackRange() { return attackRange; }
    public boolean isChasing() { return isChasing; }
}

/* if (CharacterSprite == null) {
            System.out.println("Warning: Enemy sprite is null!");
            return;
        }

        // Update attack cooldown
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }

        // Clamp enemy position to screen bounds
        CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
        CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));

        // Check if player is in detection range
        float distanceToPlayer = Math.abs(player.getXPosition() - this.getXPosition());
        isChasing = distanceToPlayer <= detectionRange;

        if (isChasing) {
            // Chase behavior with bounds checking
            float targetX = player.getXPosition();
            float currentX = this.getXPosition();

            // Only move if we're not too close to the screen edges
            if (targetX > currentX && currentX < viewport.getWorldWidth() - CharacterSprite.getWidth()) {
                speed = maxSpeed;
            } else if (targetX < currentX && currentX > 0) {
                speed = -maxSpeed;
            } else {
                speed = 0; // Stop if we're at the screen edge
            }
        } else {
            // Patrol behavior with bounds checking
            float currentX = this.getXPosition();

            if (movingRight) {
                if (currentX < startX + patrolDistance && currentX < viewport.getWorldWidth() - CharacterSprite.getWidth()) {
                    speed = patrolSpeed;
                } else {
                    movingRight = false;
                    speed = -patrolSpeed;
                }
            } else {
                if (currentX > startX - patrolDistance && currentX > 0) {
                    speed = -patrolSpeed;
                } else {
                    movingRight = true;
                    speed = patrolSpeed;
                }
            }
        }

        // Apply movement
        CharacterSprite.translateX(speed * deltaTime);*/
