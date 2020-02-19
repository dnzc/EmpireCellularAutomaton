package util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.border.EmptyBorder;

public class FlatJMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	private Cursor handCursor;
	private Cursor defaultCursor;
	private Font font;

	private String text;
	private Color colour;
	private Color textColour;

	private boolean enabled = true;

	public FlatJMenu(String text, Color colour, Color textColour) {
		super(text);

		this.text = text;
		this.colour = colour;
		this.textColour = textColour;

		handCursor = new Cursor(Cursor.HAND_CURSOR);
		defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		font = new Font("Arial", Font.BOLD, 15);

		this.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent me) {
				if (enabled)
					setCursor(handCursor);
			}

			public void mouseExited(MouseEvent me) {
				setCursor(defaultCursor);
			}
		});
		this.setBorder(new EmptyBorder(10, 20, 10, 20));
	}

	public void toggleEnabled() {
		enabled = !enabled;
		super.setEnabled(enabled);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.enabled = enabled;
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		this.text = text;
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		this.colour = bg;
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		this.textColour = fg;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (enabled)
			g.setColor(colour);
		else
			g.setColor(new Color(colour.getRed() / 3, colour.getGreen() / 3, colour.getBlue() / 3));
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		FontMetrics metrics = g2d.getFontMetrics(font);
		// Determine the X coordinate for the text
		int x = (getWidth() - metrics.stringWidth(text)) / 2;
		// Determine the Y coordinate for the text (note we add the ascent, as in java
		// 2d 0 is top of the screen)
		int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
		// Set the font
		g2d.setFont(font);
		if (enabled)
			g2d.setColor(textColour);
		else
			g2d.setColor(new Color(textColour.getRed() / 3, textColour.getGreen() / 3, textColour.getBlue() / 3));
		// Draw the String
		g2d.drawString(text, x, y);
	}

}