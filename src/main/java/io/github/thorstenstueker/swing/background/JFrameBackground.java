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
 * Replaces the frame's content pane with a {@link BackgroundPanel} that
 * wraps the original content pane as a transparent child. The original pane
 * retains its layout manager and all its children unchanged.
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
     * Wraps the existing content pane inside the BackgroundPanel.
     * The original pane keeps its layout manager (avoiding issues with
     * GroupLayout, which is tied to exactly one container) and is made
     * transparent so the background image shows through.
     */
    private static BackgroundPanel replaceContentPane(JFrame frame, BackgroundPanel bgPanel) {
        Container oldPane = frame.getContentPane();

        if (oldPane instanceof JComponent) {
            ((JComponent) oldPane).setOpaque(false);
        }

        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(oldPane, BorderLayout.CENTER);

        frame.setContentPane(bgPanel);
        frame.revalidate();
        frame.repaint();

        return bgPanel;
    }
}
