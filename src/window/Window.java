package window;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import util.Config;
import util.FlatJButton;
import util.FlatJMenu;
import util.FlatJSeparator;
import util.ImageUtils;

public class Window extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JMenuBar menu;
	private FlatJMenu spawn;
	private FlatJButton toggle, reset;
	private JMenuItem spawnFill;
	private JMenuItem spawnColonies;

	private EmpireCanvas canvas;

	public static boolean showStats = false;

	private boolean running = false;
	public static boolean hasSpawned = false;

	public static BufferedImage originalMapImage;
	public static BufferedImage mapImage;

	public static int WORLD_WIDTH;
	public static int WORLD_HEIGHT;
	public static float SCALEX;
	public static float SCALEY;

	public static void main(String[] args) {

		// read images
		try {
			originalMapImage = ImageIO.read(new File(Config.IMAGE_PATH));
			mapImage = ImageUtils.toBufferedImage(
					originalMapImage.getScaledInstance(Config.CANVAS_WIDTH, Config.CANVAS_HEIGHT, Image.SCALE_DEFAULT));
			Window.WORLD_WIDTH = originalMapImage.getWidth();
			Window.WORLD_HEIGHT = originalMapImage.getHeight();
			Window.SCALEX = (float) Config.CANVAS_WIDTH / (float) Window.WORLD_WIDTH;
			Window.SCALEY = (float) Config.CANVAS_HEIGHT / (float) Window.WORLD_HEIGHT;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// swing
		Window f = new Window();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Empire");
		f.setSize(Config.CANVAS_WIDTH, Config.CANVAS_HEIGHT + 95);
		f.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f.getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - f.getHeight()) / 2);
		f.setResizable(false);
		f.setVisible(true);

	}

	public Window() {
		// menu
		menu = new JMenuBar();
		menu.setBackground(Config.MENU_BAR_COLOUR);
		menu.setBorder(BorderFactory.createLineBorder(Config.MENU_BAR_COLOUR, 10));
		menu.setLayout(new GridLayout());
		setJMenuBar(menu);

		spawn = new FlatJMenu("Spawn", Color.DARK_GRAY, Color.WHITE);
		spawn.setBorder(new EmptyBorder(0, 10, 0, 10));
		menu.add(spawn);

		spawnFill = new JMenuItem("Fill");
		spawnFill.addActionListener(this);
		spawn.add(spawnFill);
		spawnColonies = new JMenuItem("Colonies");
		spawnColonies.addActionListener(this);
		spawn.add(spawnColonies);
		toggle = new FlatJButton(Character.toString((char) 9658), new Color(50, 200, 50), Color.WHITE);
		toggle.addActionListener(this);
		toggle.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					showStats = !showStats;
			}
		});
		toggle.setEnabled(false);
		menu.add(new FlatJSeparator());
		menu.add(new FlatJSeparator());
		menu.add(new FlatJSeparator());
		menu.add(toggle);
		menu.add(new FlatJSeparator());
		menu.add(new FlatJSeparator());
		menu.add(new FlatJSeparator());

		reset = new FlatJButton("Reset", new Color(220, 50, 50), Color.WHITE);
		reset.addActionListener(this);
		reset.setEnabled(false);
		menu.add(reset);
		// canvas
		canvas = new EmpireCanvas();
		add(canvas);

	}

	public void toggleRunning() {
		running = !running;
		if (running) {
			toggle.setText("| |");
			toggle.setBackground(new Color(0, 100, 0));
			canvas.timer.start();
		} else {
			toggle.setText(Character.toString((char) 9658));
			toggle.setBackground(new Color(50, 200, 50));
			canvas.timer.stop();
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
		if (this.running) {
			toggle.setText("| |");
			toggle.setBackground(new Color(0, 100, 0));
			toggle.setForeground(Color.GRAY);
			canvas.timer.start();
		} else {
			toggle.setText(Character.toString((char) 9658));
			toggle.setBackground(new Color(50, 200, 50));
			toggle.setForeground(Color.WHITE);
			canvas.timer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(spawnFill)) {
			// random fill the board
			setRunning(false);
			final JFrame f = new JFrame();
			f.setSize(200, 74);
			f.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f.getWidth()) / 2,
					(Toolkit.getDefaultToolkit().getScreenSize().height - f.getHeight()) / 2);
			f.setResizable(false);
			JPanel p = new JPanel();
			p.setOpaque(false);
			f.add(p);
			p.add(new JLabel("Fill density (%): "));
			final JComboBox<Object> cb_percent = new JComboBox<Object>(
					new Object[] { "Select", 5, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 95 });
			p.add(cb_percent);
			cb_percent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (cb_percent.getSelectedIndex() > 0) {
						canvas.resetBoard();
						canvas.spawnRandomFill((Integer) cb_percent.getSelectedItem());
						hasSpawned = true;
						f.dispose();

						// update buttons
						spawn.setEnabled(false);
						toggle.setEnabled(true);
						reset.setEnabled(true);
					}
				}
			});
			f.setVisible(true);
		} else if (ae.getSource().equals(spawnColonies)) {
			// spawn colonies in random positions
			canvas.resetBoard();
			canvas.spawnRandomColonies();
			hasSpawned = true;

			// update buttons
			spawn.setEnabled(false);
			toggle.setEnabled(true);
			reset.setEnabled(true);
		} else if (ae.getSource().equals(reset)) {
			setRunning(false);
			canvas.resetBoard();
			hasSpawned = false;
			canvas.repaint();

			// update buttons
			spawn.setEnabled(true);
			toggle.setEnabled(false);
			reset.setEnabled(false);
		} else if (ae.getSource().equals(toggle)) {
			toggleRunning();
		}
	}

}