package io.github.javagame.temp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Grapple extends Character{

    private float startX;
    private boolean movingRight;
    private float detectionRange;
    private float attackRange;
    private float attackCooldown;
    private float currentCooldown;
    boolean goingRight = false;
    ArrayList<Projectiles> gunShot;


    public Grapple(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture textureFile, ArrayList<Projectiles> bulletArray) {
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, textureFile);
        System.out.println("Enemy constructor called");
        System.out.println("Texture file: " + (textureFile != null));
        System.out.println("CharacterSprite: " + (CharacterSprite != null));

        // Distance to patrol in each direction
        this.startX = xspawn;
        this.movingRight = true;
        this.detectionRange = 5f;  // Range at which enemy can detect player
        this.attackRange = 4f;  // Range at which enemy can attack
        this.attackCooldown = 3f;  // Time between attacks
        this.currentCooldown = 0f;
        this.gunShot = bulletArray;

        // Set initial sprite position
        /*if (CharacterSprite != null) {
            CharacterSprite.setPosition(xspawn, yspawn);
            System.out.println("Enemy sprite position set to: " + xspawn + ", " + yspawn);
        }*/
    }


    public void update(float deltaTime, float leftbound, float rightbound,PC Target) {



        this.CharacterSprite.setX(MathUtils.clamp(this.CharacterSprite.getX(), 0, viewport.getWorldWidth() - this.CharacterSprite.getWidth()));
        this.CharacterSprite.setY(MathUtils.clamp(this.CharacterSprite.getY(), 0, viewport.getWorldHeight() - this.CharacterSprite.getHeight()));
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }

        if (Target != null) {
            if ((Math.abs(Target.CharacterSprite.getX() - this.CharacterSprite.getX()) < detectionRange) && (Math.abs(Target.CharacterSprite.getY() - this.CharacterSprite.getY()) < 1f)) {
                this.attack(Target);
            }

            }


            // Ground collision check - moved before jump check



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
    // Getters for enemy-specific properties
    public float getDetectionRange() { return detectionRange; }
    public float getAttackRange() { return attackRange; }

}

