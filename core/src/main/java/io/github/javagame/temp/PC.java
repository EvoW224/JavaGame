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
    float gunTimer = 0.4f;
    boolean facingRight = true;




    public PC(Viewport viewport, float maxSpeed, float width, float height, float xspawn, float yspawn, int HP, int damageStat, Texture CTexture) {
        super(viewport, maxSpeed, width, height, xspawn, yspawn, HP, damageStat, CTexture);
    }

    public void update(float deltaTime, ArrayList<Projectiles> projectiles) {
            //Character Clamp to avoid leaving screen
            CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
            CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 0, viewport.getWorldHeight() - CharacterSprite.getHeight()));
        if (!takingRecoil) {
            //Blaster Attack
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && gunTimer < 0.0f) {
                float px = (facingRight) ? this.CharacterSprite.getX() + this.CharacterSprite.getWidth() : this.CharacterSprite.getX();
                float py = this.CharacterSprite.getY() + this.CharacterSprite.getHeight() / 2f;

                projectiles.add(new Projectiles(this.viewport, projectileTexture, px, py, this.viewport.getWorldWidth(),this));
                gunTimer = 0.4f;
            }
            gunTimer -= deltaTime;

            if (gunTimer < -1000000f) {gunTimer = -1f;}

            // Handle horizontal movement
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                this.speed += dvdt;
                facingRight = true;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                this.speed -= dvdt;
                facingRight = false;
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

           // System.out.println(takingRecoil + " " + recoilTimer);

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


    /*public void createPlayer () {
        characterCreation(TextureFile);

    }*/
  /*  public void update(float deltaTime) {
        //Character Clamp to avoid leaving screen
        CharacterSprite.setX(MathUtils.clamp(CharacterSprite.getX(), 0, viewport.getWorldWidth() - CharacterSprite.getWidth()));
        CharacterSprite.setY(MathUtils.clamp(CharacterSprite.getY(), 1, viewport.getWorldHeight() - CharacterSprite.getHeight()));
        if (!onAir || this.getYPosition() < 1f) {
            this.CharacterSprite.setY(1f);
            this.yspeed = 0f;
        }
        if (this.speed > this.maxSpeed) {this.speed = this.maxSpeed;}
        if (Math.abs(this.yspeed) > this.terminalVelocity) { this.yspeed = this.terminalVelocity*this.yspeed/Math.abs(this.yspeed);}

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.speed += dvdt;
        } else if
        (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.speed -= dvdt;
        }
        CharacterSprite.translateX(speed * deltaTime);
        CharacterSprite.translateY(yspeed * deltaTime);// move the PC right
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && jumpTimer < 0.4f) {
            this.yspeed += gravity;
            jumpTimer += deltaTime;
            onAir = true;
            onGround = false;
            // PC jump

        }
        if (!Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.speed -= (speed > 0f) ? dvdt : 0f;
            if (speed < 0f) {speed += 0.3f;}
            if (Math.abs(speed) <= 0.3f) {speed = 0f;}
        }



        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && onAir || jumpTimer >= 0.4f) {
            this.yspeed -= this.gravity;
        }

        if (this.getYPosition() < 1.0f) {CharacterSprite.setY(1f);this.yspeed = 0f; this.onAir = false; onGround = true; jumpTimer = 0f;}
        System.out.print(CharacterSprite.getY());

    }
    */








    /* public Sprite PCharacter;
    Rectangle PChitbox;
    boolean onGround = false;
    boolean canJump = false;
    float speed = 3f;
    float jumpCooldown = 0;
    boolean onLoopJump = false;
    double jumpTimer = 0;
    private Texture PCholder;


    @Override
    public void createPC () {
        PCholder = new Texture("PC sprite holder.png");
        PCharacter = new Sprite(PCholder);
        PCharacter.setSize(1,1);
    }

    @Override
    public void renderPC () {
        private void input() {
            float jumpPower = 12f;
            float deltaTime = Gdx.graphics.getDeltaTime();


            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                PCharacter.translateX(speed * deltaTime); // move the PC right
            } else if
            (Gdx.input.isKeyPressed(Input.Keys.A)) {
                PCharacter.translateX(-speed * deltaTime); // move the PC left
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && jumpTimer < .4f) {
                PCharacter.translateY(jumpPower * deltaTime);
                jumpTimer += deltaTime;
                // PC jump
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                onLoopJump = true;
            }
            if (onLoopJump) {
                jumpCooldown += deltaTime;
                canJump = false;
            }
            if (jumpCooldown > 1f) {
                canJump = true;
                onLoopJump = false;
                jumpCooldown = 0;
            }


            if (canJump) {
                jumpTimer = 0;
            }
        }
    }

    @Override
    public void disposePC () {


    }

}

// Apply gravity if not on ground
        if (!onGround) {
            acceleration.y = gravity;
        } else {
            acceleration.y = 0;
            velocity.y = 0; // Stop vertical movement when landing
        }

        // Apply acceleration to velocity
        velocity.add(acceleration.x * deltaTime, acceleration.y * deltaTime);

        // Clamp horizontal speed
        if (velocity.x > maxSpeed) velocity.x = maxSpeed;
        if (velocity.x < -maxSpeed) velocity.x = -maxSpeed;

        // Apply friction if no movement keys are pressed
        if (acceleration.x == 0) {
            velocity.x *= friction;
        }

        // Update position
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        // Ensure character stays within the viewport
        if (position.x < 0) position.x = 0;
        if (position.x > viewport.getWorldWidth() - 50) position.x = viewport.getWorldWidth() - 50; // Assuming character width is 50

        // Check if character lands on the ground
        if (position.y <= 0) {
            position.y = 0;
            onGround = true;
            onAir = false;
            isJumping = false;
            jumpTimer = 1;
        } else {
            onGround = false;
            onAir = true;
        }

    }

    private void handleInput(float deltaTime) {
        // Move left
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            acceleration.x = -1;
        }
        // Move right
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            acceleration.x = 1;
        }
        // Stop movement if no input
        else {
            acceleration.x = 0;
        }

        // Handle jump (variable height jump mechanic)
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && onGround) {
            isJumping = true;
            jumpTimer += deltaTime;
            if (jumpTimer > maxJumpTime) jumpTimer = maxJumpTime; // Cap jump time
            velocity.y = jumpStrength * (1 + jumpTimer); // Increase jump strength based on hold time
            onGround = false;
            onAir = true;
        }

        // If spacebar is released, stop jump
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && isJumping) {
            isJumping = false;
        }*/
