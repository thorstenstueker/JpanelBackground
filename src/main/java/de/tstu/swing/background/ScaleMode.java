package de.tstu.swing.background;

/**
 * Defines how a background image is scaled to fit the panel.
 */
public enum ScaleMode {

    /**
     * Stretch to fill the entire panel, ignoring aspect ratio.
     */
    STRETCH,

    /**
     * Scale to fit entirely within the panel, preserving aspect ratio.
     * Unused areas are left transparent (letterbox/pillarbox).
     */
    FIT,

    /**
     * Scale to fill the entire panel, preserving aspect ratio.
     * Overflowing parts of the image are clipped.
     */
    FILL,

    /**
     * Center the image at original size, no scaling.
     * Overflowing parts are clipped.
     */
    CENTER,

    /**
     * Tile the image at original size to fill the panel.
     */
    TILE
}
