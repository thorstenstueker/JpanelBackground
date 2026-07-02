package io.github.thorstenstueker.swing.background;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class for attaching a background image to an existing JFrame.
 * <p>
 * Replaces the frame's content pane with a {@link BackgroundPanel} and
 * migrates all existing children into it. The layout manager of the original
 * content pane is preserved.
 * <p>
 * Usage:
 * <pre>
 *   JFrame frame = new JFrame("My App");
 *   // ... add components to frame as usual ...
 *   JFrameBackground.attach(frame, "bg.jpg", ScaleMode.FILL);
 * </pre>
 */
public final class JFrameBackground {

    private JFrameBackground() {}

    public static BackgroundPanel attach(JFrame frame, BufferedImage image, ScaleMode mode) {
        return replaceContentPane(frame, new BackgroundPanel(image, mode));
    }

    public static BackgroundPanel attach(JFrame frame, String imagePath, ScaleMode mode) throws IOException {
        return replaceContentPane(frame, new BackgroundPanel(imagePath, mode));
    }

    public static BackgroundPanel attach(JFrame frame, File imageFile, ScaleMode mode) throws IOException {
        return replaceContentPane(frame, new BackgroundPanel(imageFile, mode));
    }

    public static BackgroundPanel attach(JFrame frame, URL imageUrl, ScaleMode mode) throws IOException {
        return replaceContentPane(frame, new BackgroundPanel(imageUrl, mode));
    }

    public static BackgroundPanel attach(JFrame frame, InputStream imageStream, ScaleMode mode) throws IOException {
        return replaceContentPane(frame, new BackgroundPanel(imageStream, mode));
    }

    /**
     * Migrates all children and the layout manager from the current content
     * pane into the given BackgroundPanel and sets it as the new content pane.
     */
    private static BackgroundPanel replaceContentPane(JFrame frame, BackgroundPanel bgPanel) {
        Container oldPane = frame.getContentPane();

        bgPanel.setLayout(oldPane.getLayout());

        Component[] children = oldPane.getComponents();
        for (Component child : children) {
            oldPane.remove(child);
            bgPanel.add(child);
        }

        frame.setContentPane(bgPanel);
        frame.revalidate();
        frame.repaint();

        return bgPanel;
    }
}
