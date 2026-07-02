package io.github.thorstenstueker.swing.background;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * A JPanel that renders a scalable background image.
 * <p>
 * Drop-in replacement for JPanel as a content pane. Child components are
 * laid out normally on top of the background.
 * <p>
 * Usage:
 * <pre>
 *   BackgroundPanel bg = new BackgroundPanel("image.jpg", ScaleMode.FIT);
 *   frame.setContentPane(bg);
 * </pre>
 */
public class BackgroundPanel extends JPanel {

    private BufferedImage image;
    private ScaleMode scaleMode;

    public BackgroundPanel(ScaleMode scaleMode) {
        this.scaleMode = scaleMode;
        setOpaque(true);
    }

    public BackgroundPanel(BufferedImage image, ScaleMode scaleMode) {
        this(scaleMode);
        this.image = image;
    }

    public BackgroundPanel(String imagePath, ScaleMode scaleMode) throws IOException {
        this(scaleMode);
        this.image = ImageIO.read(new File(imagePath));
    }

    public BackgroundPanel(File imageFile, ScaleMode scaleMode) throws IOException {
        this(scaleMode);
        this.image = ImageIO.read(imageFile);
    }

    public BackgroundPanel(URL imageUrl, ScaleMode scaleMode) throws IOException {
        this(scaleMode);
        this.image = ImageIO.read(imageUrl);
    }

    public BackgroundPanel(InputStream imageStream, ScaleMode scaleMode) throws IOException {
        this(scaleMode);
        this.image = ImageIO.read(imageStream);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    public void setImage(String imagePath) throws IOException {
        this.image = ImageIO.read(new File(imagePath));
        repaint();
    }

    public void setImage(File imageFile) throws IOException {
        this.image = ImageIO.read(imageFile);
        repaint();
    }

    public void setImage(URL imageUrl) throws IOException {
        this.image = ImageIO.read(imageUrl);
        repaint();
    }

    public void setImage(InputStream imageStream) throws IOException {
        this.image = ImageIO.read(imageStream);
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setScaleMode(ScaleMode scaleMode) {
        this.scaleMode = scaleMode;
        repaint();
    }

    public ScaleMode getScaleMode() {
        return scaleMode;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelW = getWidth();
        int panelH = getHeight();
        int imgW = image.getWidth();
        int imgH = image.getHeight();

        switch (scaleMode) {
            case STRETCH: drawStretched(g2d, panelW, panelH); break;
            case FIT:     drawFit(g2d, panelW, panelH, imgW, imgH); break;
            case FILL:    drawFill(g2d, panelW, panelH, imgW, imgH); break;
            case CENTER:  drawCentered(g2d, panelW, panelH, imgW, imgH); break;
            case TILE:    drawTiled(g2d, panelW, panelH, imgW, imgH); break;
        }

        g2d.dispose();
    }

    private void drawStretched(Graphics2D g, int pw, int ph) {
        g.drawImage(image, 0, 0, pw, ph, null);
    }

    private void drawFit(Graphics2D g, int pw, int ph, int iw, int ih) {
        double scale = Math.min((double) pw / iw, (double) ph / ih);
        int scaledW = (int) (iw * scale);
        int scaledH = (int) (ih * scale);
        int x = (pw - scaledW) / 2;
        int y = (ph - scaledH) / 2;
        g.drawImage(image, x, y, scaledW, scaledH, null);
    }

    private void drawFill(Graphics2D g, int pw, int ph, int iw, int ih) {
        double scale = Math.max((double) pw / iw, (double) ph / ih);
        int scaledW = (int) (iw * scale);
        int scaledH = (int) (ih * scale);
        int x = (pw - scaledW) / 2;
        int y = (ph - scaledH) / 2;
        Shape oldClip = g.getClip();
        g.setClip(0, 0, pw, ph);
        g.drawImage(image, x, y, scaledW, scaledH, null);
        g.setClip(oldClip);
    }

    private void drawCentered(Graphics2D g, int pw, int ph, int iw, int ih) {
        int x = (pw - iw) / 2;
        int y = (ph - ih) / 2;
        Shape oldClip = g.getClip();
        g.setClip(0, 0, pw, ph);
        g.drawImage(image, x, y, null);
        g.setClip(oldClip);
    }

    private void drawTiled(Graphics2D g, int pw, int ph, int iw, int ih) {
        for (int y = 0; y < ph; y += ih) {
            for (int x = 0; x < pw; x += iw) {
                g.drawImage(image, x, y, null);
            }
        }
    }
}
