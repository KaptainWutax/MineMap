package kaptainwutax.minemap.util.ui.icons;

import kaptainwutax.minemap.init.Icons;
import kaptainwutax.minemap.util.ui.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ButtonIcon extends JButton {
    public final int size;
    public final int inset;
    public final float factor;
    public final boolean background;
    public Color backgroundColor;
    public final Class<? extends ButtonIcon> clazz;

    public ButtonIcon(int size, int inset, float factor, boolean background, Color backgroundColor, Class<? extends ButtonIcon> clazz) {
        super();
        this.size = size;
        this.inset = inset;
        this.factor = factor;
        this.background = background;
        this.backgroundColor = backgroundColor;
        this.clazz = clazz;
    }

    public ButtonIcon(int size, int inset, float factor, Class<? extends ButtonIcon> clazz) {
        this(size, inset, factor, false, Color.WHITE, clazz);
    }

    public ButtonIcon(int size, int inset, Class<? extends ButtonIcon> clazz) {
        this(size, inset, 1.7F, clazz);
    }

    public ButtonIcon(int size, Class<? extends ButtonIcon> clazz) {
        this(size, 1, clazz);
    }

    public ButtonIcon(Class<? extends ButtonIcon> clazz) {
        this(16, clazz);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(30, 30);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorder(new RoundedBorder(size - 2, 30)); //10 is the radius
        this.setForeground(Color.DARK_GRAY);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        if (this.background) {
            Color old = g.getColor();
            g.setColor(backgroundColor);
            int bSize = 30;
            int bRadius = size - 2;
            int bDiff = bSize / 2 - bRadius;
            g.fillRoundRect(bDiff, bDiff, bRadius * 2 - 2, bRadius * 2 - 2, bSize, bSize);

            g.setColor(old);
        }

        BufferedImage icon = Icons.REGISTRY.get(clazz);
        int iconSizeX, iconSizeZ;
        int defaultValue = size;
        if (icon.getRaster().getWidth() > icon.getRaster().getHeight()) {
            iconSizeX = defaultValue;
            iconSizeZ = (int) (defaultValue * (float) icon.getRaster().getHeight() / icon.getRaster().getWidth());
        } else {
            iconSizeZ = defaultValue;
            iconSizeX = (int) (defaultValue * (float) icon.getRaster().getWidth() / icon.getRaster().getHeight());
        }
        g.drawImage(icon, (defaultValue - iconSizeX) / 2 + inset, (defaultValue - iconSizeZ) / 2 + inset, (int) (iconSizeX * factor), (int) (iconSizeZ * factor), null);
    }

    public void changeBColor(Color color){
        this.backgroundColor=color;
        this.revalidate();
        this.repaint();
    }
}