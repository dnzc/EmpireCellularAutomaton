package util;

import java.awt.Color;

public class Config {

	public static final String IMAGE_PATH = "res/world_discrete_connect.png";

	public static final int CANVAS_WIDTH = 1692;
	public static final int CANVAS_HEIGHT = 780;

	public static final int TIMER_STEP = 30;

	public static final Color[] COLONIES = new Color[] { new Color(0, 0, 0), new Color(255, 255, 255),
			new Color(255, 0, 0), new Color(200, 200, 0), new Color(255, 0, 255), new Color(0, 200, 200),
			new Color(120, 120, 120), new Color(255, 127, 0), new Color(100, 0, 100), new Color(0, 100, 0) };

	public static final Color MENU_BAR_COLOUR = new Color(30, 30, 30);

	// whether cells in the same colony spread disease to each other
	public static final boolean SPREAD_DISEASE = true;
	// whether cells in the same colony move around each other - leads to more stable colonies if true
	public static final boolean COLONY_DIFFUSION = false;

	public static final int REPRODUCTION_THRESHOLD = 7;

	public static final int MIN_START_STRENGTH = 450;
	public static final int MAX_START_STRENGTH = 600;

	// percentages
	public static final float DISEASE_CHANCE = 0.1f;
	public static final float DISEASE_CURED_CHANCE = 15f;
	public static final float SMALL_MUTATION_CHANCE = 25f;

}
