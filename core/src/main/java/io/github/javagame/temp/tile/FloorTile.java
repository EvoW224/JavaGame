package io.github.javagame.temp.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Draws a single type of floor tile repeatedly across the bottom of the game world.
 * The class is designed so that the tiles are drawn starting at y = 0 (i.e. the collision
 * boundary) and, if a positive yOffset is specified, the tile is stretched vertically to
 * extend upward by that amount. This produces an effect where the graphic ground extends
 * above the collision floor—common in platformers like Donkey Kong Country.
 *
 * Future enhancements might add support for multiple tile types or more sophisticated tiling.
 */
public class FloorTile {
    private Texture tileTexture;
    private float tileWidth;   // width in world units
    private float tileHeight;  // base visual tile height in world units (collision height)
    private float yOffset;     // extra height to add above the collision (if positive)

    /**
     * Constructs a FloorTile.
     *
     * @param texturePath Path to the tile image (e.g., "tile1.png" or "tile2.png").
     * @param tileWidth   The width in world units for each tile.
     * @param tileHeight  The base height (collision height) in world units for the tile’s graphic.
     * @param yOffset     The extra height to add on top of tileHeight. For example, if the
     *                    collision is at y = 0 but you want the tile to visually extend upward,
     *                    set yOffset to a positive value (e.g., 0.7f). (If yOffset is negative,
     *                    this version does nothing special.)
     */
    public FloorTile(String texturePath, float tileWidth, float tileHeight, float yOffset) {
        // Load the tile texture.
        tileTexture = new Texture(Gdx.files.internal(texturePath));
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.yOffset = yOffset;
    }

    /**
     * Renders the floor by drawing the tile repeatedly along the bottom of the viewport.
     * The bottom edge is forced at y = 0; if yOffset is positive, that value is added to the
     * tile's base height, stretching the tile upward.
     *
     * @param batch    The SpriteBatch used for drawing (must be within a begin()/end() block).
     * @param viewport The game's viewport defining the visible world.
     */
    public void render(SpriteBatch batch, Viewport viewport) {
        float worldWidth = viewport.getWorldWidth();
        // Always draw the tile starting at y = 0.
        float drawY = 0f;
        // If yOffset is positive, add it to the tile's height.
        float drawHeight = tileHeight + (yOffset > 0 ? yOffset : 0f);

        // Loop across the visible world width.
        for (float x = 0; x < worldWidth; x += tileWidth) {
            batch.draw(tileTexture, x, drawY, tileWidth, drawHeight);
        }
    }

    public void dispose() {
        if (tileTexture != null)
            tileTexture.dispose();
    }
}
