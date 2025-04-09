package io.github.javagame.temp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class PC extends Character {

    ShapeRenderer HPBar = new ShapeRenderer();
    boolean takingRecoil = false;
    float recoilTimer = 0;
    Texture projectileTexture = new Texture ("libgdx.png");




    public PC(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture CTexture) {
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, CTexture);
    }

    public void update(float deltaTime, ArrayList<Projectiles> projectiles) {
            //Character Clamp to avoid leaving screen
            CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
            CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));
        if (!takingRecoil) {
            //Blaster Attack
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                float px = this.CharacterSprite.getX() + this.CharacterSprite.getWidth();
                float py = this.CharacterSprite.getY() + this.CharacterSprite.getHeight() / 2f;

                projectiles.add(new Projectiles(this.viewport, projectileTexture, px, py, this.viewport.getWorldWidth()));
            }
            // Handle horizontal movement
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                this.speed += dvdt;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                this.speed -= dvdt;
            } else {
                // Deceleration when no movement keys are pressed
                if (speed > 0f) {
                    this.speed -= dvdt;
                    if (speed < 0f) {
                        speed = 0f;
                    }
                } else if (speed < 0f) {
                    this.speed += dvdt;
                    if (speed > 0f) {
                        speed = 0f;
                    }
                }
                if (Math.abs(speed) <= 0.3f) {
                    speed = 0f;
                }
            }

            // Apply speed limits
            if (this.speed > this.maxSpeed) {
                this.speed = this.maxSpeed;
            }
            if (this.speed < -this.maxSpeed) {
                this.speed = -this.maxSpeed;
            }
            if (Math.abs(this.yspeed) > this.terminalVelocity) {
                this.yspeed = this.terminalVelocity * Math.signum(this.yspeed);
            }

            // Ground collision check - moved before jump check
            if (CharacterSprite.getY() <= 1f) {
                CharacterSprite.setY(1f);
                this.yspeed = 0f;
                this.onAir = false;
                this.onGround = true;
                this.jumpTimer = 0f;
            }

            // Handle jumping with variable height
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (onGround) {
                    // Initial jump
                    this.yspeed = jumpStrength;
                    onAir = true;
                    onGround = false;
                    jumpTimer = 0.15f;
                } else if (onAir && jumpTimer > 0) {
                    // Continue applying upward force while space is held
                    this.yspeed = Math.min(this.yspeed + jumpStrength * 0.5f, jumpStrength * 1.5f);
                    jumpTimer -= deltaTime;
                }
            } else {
                // If space is released, stop applying upward force
                if (onAir && this.yspeed > 0) {
                    this.yspeed *= 0.5f;
                }
            }
        }
            // Apply movement
            CharacterSprite.translateX(speed * deltaTime);
            CharacterSprite.translateY(yspeed * deltaTime);


            // Apply gravity when in air
            if (onAir) {
                this.yspeed -= this.gravity;
            }

        // Ground collision check - moved before jump check
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
                if (speed < 0f) {
                    speed = 0f;
                }
            } else if (speed < 0f) {
                this.speed += dvdt;
                if (speed > 0f) {
                    speed = 0f;
                }
            }
            if (Math.abs(speed) <= 0.3f) {
                speed = 0f;
            }

        }

        if (recoilTimer > .6f) {
            takingRecoil = false;
            recoilTimer = 0.0f;
        }
        if (takingRecoil) {
            recoilTimer += deltaTime;
        }


            // Update hitbox position
            CharacterHitbox.x = CharacterSprite.getX();
            CharacterHitbox.y = CharacterSprite.getY();

            System.out.println(takingRecoil + " " + recoilTimer);

    }
    public void renderHPBar() {
        HPBar.begin(ShapeRenderer.ShapeType.Filled);
        HPBar.setColor(Color.RED); // Set rectangle color to RED
        HPBar.rect(1,4,1,0.2f); // x, y, width, height
    }
    public void endHpBar() {
        HPBar.end();
    }

    public void takeDamage(int damage) {
        this.HitPoints -= damage;
    }

    public void recoil (boolean toTheRight) {
        takingRecoil = true;

            //Recoil "Animation"
            this.yspeed = jumpStrength * 1.4f;
            this.speed = -1f * (this.speed);
            onAir = true;
            onGround = false;
            //end of recoil "Animation"



    }





}

