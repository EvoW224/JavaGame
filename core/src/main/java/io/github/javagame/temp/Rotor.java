package io.github.javagame.temp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Rotor extends Character{
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
    ArrayList<Projectiles> gunShot;
    boolean PlayerFound;


    public Rotor(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture textureFile, ArrayList<Projectiles> bulletArray) {
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
        this.gunShot = bulletArray;
        this.PlayerFound = false;

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

        if (Target != null) {
            if ((Math.abs(Target.CharacterSprite.getX() - this.CharacterSprite.getX()) < detectionRange + 3f) && (Math.abs(Target.CharacterSprite.getY() - this.CharacterSprite.getY()) < 7f)) {
                PlayerFound = true;
            }
            if (PlayerFound) {   //(Math.abs(Target.CharacterSprite.getX() - this.CharacterSprite.getX()) < detectionRange) && (Math.abs(Target.CharacterSprite.getY() - this.CharacterSprite.getY()) < 10f)
                if (Target.CharacterSprite.getX() > this.CharacterSprite.getX()) {
                    this.CharacterSprite.translateX(maxSpeed * deltaTime);
                } else {
                    this.CharacterSprite.translateX(-maxSpeed * deltaTime);
                }
                if (Math.abs(Target.CharacterSprite.getY() + Target.CharacterSprite.getHeight() - this.CharacterSprite.getY()) <= 1f) {
                   // this.CharacterSprite.setY(Target.CharacterSprite.getY() + Target.CharacterSprite.getHeight());
                    this.CharacterSprite.translateY(0f);
                    yspeed = 0;
                }
                else if (Target.onAir && (Target.CharacterSprite.getY() > this.CharacterSprite.getY()) || Target.onGround && (Target.CharacterSprite.getY() + Target.CharacterSprite.getHeight() > this.CharacterSprite.getY())) {
                    this.CharacterSprite.translateY(maxSpeed * deltaTime);
                } else {
                    this.CharacterSprite.translateY(-maxSpeed * deltaTime);
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

        for (int i = gunShot.size() - 1; i >= 0; i--) {
            if (gunShot.get(i).entityOverlap(this.CharacterHitbox)){
                gunShot.get(i).shouldRemove = true;
                this.HitPoints -= 10;
                System.out.println(this.HitPoints);
            }
        }

        // Update hitbox position


        //this.position.set(CharacterSprite.getX(), CharacterSprite.getY());

        // Debug position
        // System.out.println("Enemy position: " + CharacterSprite.getX() + ", " + CharacterSprite.getY());
    }


    public void attack(PC player) {
        if (player != null) {
            System.out.printf("Current Cooldown: %f\n", currentCooldown);
            if (currentCooldown <= 0) {
                float distanceToPlayer = Math.abs(player.CharacterSprite.getX() - this.CharacterSprite.getX());
                float ydistanceToPlayer = Math.abs(player.CharacterSprite.getY() + player.CharacterSprite.getHeight() - this.CharacterSprite.getY());
                if (distanceToPlayer <= attackRange && ydistanceToPlayer <= attackRange) {
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

    // Getters for enemy-specific properties
    public float getPatrolSpeed() { return patrolSpeed; }
    public float getDetectionRange() { return detectionRange; }
    public float getAttackRange() { return attackRange; }
    public boolean isChasing() { return isChasing; }
}
