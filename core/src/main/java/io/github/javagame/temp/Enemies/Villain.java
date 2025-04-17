package io.github.javagame.temp.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.javagame.temp.Character;
import io.github.javagame.temp.PC;
import io.github.javagame.temp.Projectiles;

import java.util.ArrayList;

public class Villain extends Character {
     float patrolSpeed;
     float patrolDistance;
     float startX;
     boolean movingRight;
     float detectionRange;
     boolean isChasing;
     float attackRange;
     float attackCooldown;
     float currentCooldown;
     boolean goingRight = true;
    ArrayList<Projectiles> gunShot;
    boolean hasGravity = true;

    public Villain(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture textureFile, ArrayList<Projectiles> bulletArray, boolean gravity) {
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
        this.hasGravity = gravity;
    }

    public void update () {
        // Clamp sprite to viewport
        CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
        CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));

// Cooldown
        if (currentCooldown > 0) {
            currentCooldown -= deltaTime;
        }

// Gravity and ground collision check
        if (hasGravity) {
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
        }

// Update hitbox position
        CharacterHitbox.x = CharacterSprite.getX();
        CharacterHitbox.y = CharacterSprite.getY();


// Projectile collision (Grapple & Rotor original form)
        for (int i = gunShot.size() - 1; i >= 0; i--) {
            /*if (gunShot.get(i).CharacterHitbox.overlaps(this.CharacterHitbox)) {*/
            if ((((gunShot.get(i).CharacterSprite.getX() < this.CharacterSprite.getX()) && (gunShot.get(i).CharacterSprite.getX() + gunShot.get(i).CharacterSprite.getWidth() > this.CharacterSprite.getX())) || ((gunShot.get(i).CharacterSprite.getX() > this.CharacterSprite.getX()) && (gunShot.get(i).CharacterSprite.getX() < this.CharacterSprite.getX() + this.CharacterSprite.getWidth()))) && ((gunShot.get(i).CharacterSprite.getY() > this.CharacterSprite.getY()) && (gunShot.get(i).CharacterSprite.getY() + gunShot.get(i).CharacterSprite.getHeight() < this.CharacterSprite.getY() + this.CharacterSprite.getHeight()))) {
                gunShot.get(i).shouldRemove = true;
                gunShot.get(i).HitPoints = 0;
                this.HitPoints -= 10;
                System.out.println(this.HitPoints);
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
                    if (CharacterSprite.getX() > player.CharacterSprite.getX()) {
                        player.recoil(false);
                    } else {
                        player.recoil(true);
                    }
                }
            }
        }
    }


}


/*
// Projectile collision (Enemy.java original form)
        for (int i = gunShot.size() - 1; i >= 0; i--) {
            if ((((gunShot.get(i).CharacterSprite.getX() < this.CharacterSprite.getX()) &&
                (gunShot.get(i).CharacterSprite.getX() + gunShot.get(i).CharacterSprite.getWidth() > this.CharacterSprite.getX())) ||
                ((gunShot.get(i).CharacterSprite.getX() > this.CharacterSprite.getX()) &&
                    (gunShot.get(i).CharacterSprite.getX() < this.CharacterSprite.getX() + this.CharacterSprite.getWidth()))) &&
                ((gunShot.get(i).CharacterSprite.getY() > this.CharacterSprite.getY()) &&
                    (gunShot.get(i).CharacterSprite.getY() + gunShot.get(i).CharacterSprite.getHeight() < this.CharacterSprite.getY() + this.CharacterSprite.getHeight()))) {
                gunShot.get(i).shouldRemove = true;
                gunShot.get(i).HitPoints = 0;
                this.HitPoints -= 10;
                System.out.println(this.HitPoints);
            }
        }
 */
