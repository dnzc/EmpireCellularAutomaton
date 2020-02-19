package window;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JPanel;
import util.Config;
import util.ImageUtils;
import world.Cell;
import world.CellManager;

public class EmpireCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	public static Cell[][] cells;

	public Timer timer;

	public EmpireCanvas() {
		resetBoard();
		timer = new Timer(Config.TIMER_STEP, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CellManager.update();
			}
		});
	}

	public void resetBoard() {
		cells = new Cell[Window.WORLD_WIDTH][Window.WORLD_HEIGHT];
		for (int x = 0; x < Window.WORLD_WIDTH; x++) {
			for (int y = 0; y < Window.WORLD_HEIGHT; y++) {
				cells[x][y] = new Cell(x, y);
			}
		}
		for (int i = 0; i < Config.COLONIES.length; i++) {
			CellManager.populationCount[i] = 0;
			CellManager.totalStrength[i] = 0;
		}
	}

	public void spawnRandomFill(int percent) {
		// fill world with random people from random colonies (on land), with a specific
		// density
		Random rng = new Random();
		for (int x = 0; x < Window.WORLD_WIDTH; x++) {
			for (int y = 0; y < Window.WORLD_HEIGHT; y++) {
				if (!ImageUtils.isWater(x, y) && Math.random() * 100 < percent) {
					int colony = rng.nextInt(Config.COLONIES.length);
					cells[x][y] = new Cell(x, y, colony, 0, rng.nextInt(100), 0, true, false, false);
					CellManager.populationCount[colony]++;
					CellManager.totalStrength[colony] += cells[x][y].getStrength();

				}
			}
		}
	}

	public void spawnRandomColonies() {
		Random rng = new Random();
		for (int colony = 0; colony < Config.COLONIES.length; colony++) {
			// for each colony, spawn 50 people in a random 15*15 area on land
			Point colonySpawnPos = null;
			boolean validSpawn = false;
			while (!validSpawn) {
				validSpawn = true;
				colonySpawnPos = new Point(rng.nextInt(Window.WORLD_WIDTH - 50), rng.nextInt(Window.WORLD_HEIGHT - 50));
				for (int x = 0; x < 50; x++) {
					for (int y = 0; y < 50; y++) {
						if (ImageUtils.isWater(colonySpawnPos.x + x, colonySpawnPos.y + y)) {
							validSpawn = false;
							break;
						}
					}
					if (!validSpawn)
						break;
				}
			}
			for (int i = 0; i < 50; i++) {
				int nextX = colonySpawnPos.x + rng.nextInt(15);
				int nextY = colonySpawnPos.y + rng.nextInt(15);
				while (cells[nextX][nextY].isAlive()) {
					nextX = colonySpawnPos.x + rng.nextInt(15);
					nextY = colonySpawnPos.y + rng.nextInt(15);
				}
				cells[nextX][nextY] = new Cell(nextX, nextY, colony, 0, rng.nextInt(100), 0, true, false, false);
				CellManager.populationCount[colony]++;
				CellManager.totalStrength[colony] += cells[nextX][nextY].getStrength();
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// background image
		g.drawImage(Window.mapImage, 0, 0, null);

		// paint world
		for (int x = 0; x < Window.WORLD_WIDTH; x++) {
			for (int y = 0; y < Window.WORLD_HEIGHT; y++) {
				if (cells[x][y].isAlive()) {
					g.setColor(Config.COLONIES[cells[x][y].getID()]);
					g.fillRect((int) (x * Window.SCALEX), (int) (y * Window.SCALEY), (int) Math.ceil(Window.SCALEX),
							(int) Math.ceil(Window.SCALEY));
				}
			}
		}

		// paint stats
		if (Window.hasSpawned) {

			// get number of alive colonies
			int numAliveColonies = 0;
			for (int i : CellManager.populationCount) {
				if (i != 0)
					numAliveColonies++;
			}

			int displayHeight = 30;
			g.setColor(new Color(200, 200, 200, 200));
			g.fillRect(0, 0, 340, displayHeight * numAliveColonies);

			int index = 0;
			Font font = new Font("Arial", Font.BOLD, displayHeight / 2);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setFont(font);
			FontMetrics metrics = getFontMetrics(font);
			for (int i = 0; i < Config.COLONIES.length; i++) {
				if (CellManager.populationCount[i] != 0) {
					g2d.setColor(Config.COLONIES[i]);
					g2d.drawString(
							"Colony " + i + ": pop " + Integer.toString(CellManager.populationCount[i])
									+ ", avg. strength "
									+ Integer.toString(
											(int) (CellManager.totalStrength[i] / CellManager.populationCount[i])),
							displayHeight / 4, displayHeight * index + metrics.getHeight());
					index++;
				}
			}
		}
		repaint();
	}

}