package io.github.defective4.pi.pcd8544;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.Map;

public class GraphicsWrapper extends Graphics2D {
    private static final int black = Color.black.getRGB();
    private final BufferedImage canvas = new BufferedImage(PCDConstants.LCDWIDTH, PCDConstants.LCDHEIGHT,
            BufferedImage.TYPE_BYTE_BINARY);
    private final Graphics2D gfx = canvas.createGraphics();

    private final PCD8544 lcd;

    public GraphicsWrapper(PCD8544 lcd) {
        this.lcd = lcd;
        clear();
    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {
        gfx.addRenderingHints(hints);
    }

    public void clear() {
        gfx.setColor(Color.white);
        gfx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gfx.setColor(Color.black);
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        gfx.clearRect(x, y, width, height);
    }

    @Override
    public void clip(Shape s) {
        gfx.clip(s);
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {
        gfx.clipRect(x, y, width, height);
    }

    public void commit(boolean allowPartialUpdates) {
        byte[][] buffer = new byte[canvas.getWidth()][];
        for (int x = 0; x < buffer.length; x++) {
            byte[] yBuf = new byte[canvas.getHeight()];
            Arrays.fill(yBuf, (byte) 0);
            for (int y = 0; y < yBuf.length; y++) {
                int rgb = canvas.getRGB(x, y);
                if (rgb == black) yBuf[y] = 1;
            }
            buffer[x] = yBuf;
        }
        lcd.set2DBuffer(buffer, allowPartialUpdates);
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        gfx.copyArea(x, y, width, height, dx, dy);
    }

    @Override
    public Graphics create() {
        return gfx.create();
    }

    @Override
    public Graphics create(int x, int y, int width, int height) {
        return gfx.create(x, y, width, height);
    }

    @Override
    public void dispose() {
        gfx.dispose();
    }

    @Override
    public void draw(Shape s) {
        gfx.draw(s);
    }

    @Override
    public void draw3DRect(int x, int y, int width, int height, boolean raised) {
        gfx.draw3DRect(x, y, width, height, raised);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        gfx.drawArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawBytes(byte[] data, int offset, int length, int x, int y) {
        gfx.drawBytes(data, offset, length, x, y);
    }

    @Override
    public void drawChars(char[] data, int offset, int length, int x, int y) {
        gfx.drawChars(data, offset, length, x, y);
    }

    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {
        gfx.drawGlyphVector(g, x, y);
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
        gfx.drawImage(img, op, x, y);
    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return gfx.drawImage(img, xform, obs);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return gfx.drawImage(img, x, y, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return gfx.drawImage(img, x, y, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return gfx.drawImage(img, x, y, width, height, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return gfx.drawImage(img, x, y, width, height, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
            Color bgcolor, ImageObserver observer) {
        return gfx.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
            ImageObserver observer) {
        return gfx.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        gfx.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        gfx.drawOval(x, y, width, height);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        gfx.drawPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawPolygon(Polygon p) {
        gfx.drawPolygon(p);
    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
        gfx.drawPolyline(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        gfx.drawRect(x, y, width, height);
    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
        gfx.drawRenderableImage(img, xform);
    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
        gfx.drawRenderedImage(img, xform);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        gfx.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        gfx.drawString(iterator, x, y);
    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        gfx.drawString(iterator, x, y);
    }

    @Override
    public void drawString(String str, float x, float y) {
        gfx.drawString(str, x, y);
    }

    @Override
    public void drawString(String str, int x, int y) {
        gfx.drawString(str, x, y);
    }

    @Override
    public void fill(Shape s) {
        gfx.fill(s);
    }

    @Override
    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        gfx.fill3DRect(x, y, width, height, raised);
    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        gfx.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        gfx.fillOval(x, y, width, height);
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        gfx.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void fillPolygon(Polygon p) {
        gfx.fillPolygon(p);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        gfx.fillRect(x, y, width, height);
    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        gfx.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public Color getBackground() {
        return gfx.getBackground();
    }

    @Override
    public Shape getClip() {
        return gfx.getClip();
    }

    @Override
    public Rectangle getClipBounds() {
        return gfx.getClipBounds();
    }

    @Override
    public Rectangle getClipBounds(Rectangle r) {
        return gfx.getClipBounds(r);
    }

    @Override
    public Rectangle getClipRect() {
        return gfx.getClipRect();
    }

    @Override
    public Color getColor() {
        return gfx.getColor();
    }

    @Override
    public Composite getComposite() {
        return gfx.getComposite();
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return gfx.getDeviceConfiguration();
    }

    @Override
    public Font getFont() {
        return gfx.getFont();
    }

    @Override
    public FontMetrics getFontMetrics() {
        return gfx.getFontMetrics();
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return gfx.getFontMetrics(f);
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return gfx.getFontRenderContext();
    }

    public int getHeight() {
        return canvas.getHeight();
    }

    @Override
    public Paint getPaint() {
        return gfx.getPaint();
    }

    @Override
    public Object getRenderingHint(Key hintKey) {
        return gfx.getRenderingHint(hintKey);
    }

    @Override
    public RenderingHints getRenderingHints() {
        return gfx.getRenderingHints();
    }

    @Override
    public Stroke getStroke() {
        return gfx.getStroke();
    }

    @Override
    public AffineTransform getTransform() {
        return gfx.getTransform();
    }

    public int getWidth() {
        return canvas.getWidth();
    }

    @Override
    public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
        return gfx.hit(rect, s, onStroke);
    }

    @Override
    public boolean hitClip(int x, int y, int width, int height) {
        return gfx.hitClip(x, y, width, height);
    }

    @Override
    public void rotate(double theta) {
        gfx.rotate(theta);
    }

    @Override
    public void rotate(double theta, double x, double y) {
        gfx.rotate(theta, x, y);
    }

    @Override
    public void scale(double sx, double sy) {
        gfx.scale(sx, sy);
    }

    @Override
    public void setBackground(Color color) {
        gfx.setBackground(color);
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        gfx.setClip(x, y, width, height);
    }

    @Override
    public void setClip(Shape clip) {
        gfx.setClip(clip);
    }

    @Override
    public void setColor(Color c) {
        gfx.setColor(c);
    }

    @Override
    public void setComposite(Composite comp) {
        gfx.setComposite(comp);
    }

    @Override
    public void setFont(Font font) {
        gfx.setFont(font);
    }

    @Override
    public void setPaint(Paint paint) {
        gfx.setPaint(paint);
    }

    @Override
    public void setPaintMode() {
        gfx.setPaintMode();
    }

    @Override
    public void setRenderingHint(Key hintKey, Object hintValue) {
        gfx.setRenderingHint(hintKey, hintValue);
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {
        gfx.setRenderingHints(hints);
    }

    @Override
    public void setStroke(Stroke s) {
        gfx.setStroke(s);
    }

    @Override
    public void setTransform(AffineTransform Tx) {
        gfx.setTransform(Tx);
    }

    @Override
    public void setXORMode(Color c1) {
        gfx.setXORMode(c1);
    }

    @Override
    public void shear(double shx, double shy) {
        gfx.shear(shx, shy);
    }

    @Override
    public void transform(AffineTransform Tx) {
        gfx.transform(Tx);
    }

    @Override
    public void translate(double tx, double ty) {
        gfx.translate(tx, ty);
    }

    @Override
    public void translate(int x, int y) {
        gfx.translate(x, y);
    }

}
