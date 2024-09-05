package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Tetris {

	static final Color[] COLORS = { Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA,
			Color.ORANGE, Color.YELLOW };

	public static void main(String[] args) {

		JFrame frame = new JFrame("Tetris");

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 700));

		frame.add(panel);

		frame.pack();

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.setVisible(true);

		Graphics2D graphics = (Graphics2D) panel.getGraphics();

		TetrisModel model = new TetrisModel(TetrisModel.DEFAULT_WIDTH, TetrisModel.DEFAULT_HEIGHT,
				TetrisModel.DEFAULT_COLORS_NUMBER);

		View view = new View(new Graphics() {

			@Override
			public void drawBoxAt(int i, int j, int size, int colorIndex) {
				graphics.setColor(COLORS[colorIndex]);
				graphics.fillRect(i, j, View.BOX_SIZE, View.BOX_SIZE);
				}

			@Override
			public void drawGameOver(String text, int row, int col) {
				graphics.setColor(Color.white);
				graphics.clearRect(row, col-40, 210, 50);
				graphics.setColor(COLORS[0]);
				graphics.setFont(new Font("Arial",Font.BOLD, 40));
				graphics.drawString(text, row, col);
			}
			
			@Override
			public void drawRestart(String text, int row, int col) {
				graphics.setColor(Color.white);
				graphics.clearRect(row, col-18, 210, 20);
				graphics.setColor(COLORS[0]);
				graphics.setFont(new Font("Arial",Font.BOLD, 18));
				graphics.drawString(text, row, col);
				
			}

			@Override
			public void showScore(int score) {
				graphics.setColor(Color.black);
				graphics.setFont(new Font("Arial",Font.PLAIN, 14));
				graphics.clearRect(View.ORIGIN + View.LeftBorder, 660, 100, 20);
				graphics.drawString("Score: " + score, View.ORIGIN + View.LeftBorder, 670);
				
			}

			@Override
			public void showLevel(int level) {
				graphics.setColor(Color.black);
				graphics.setFont(new Font("Arial",Font.PLAIN, 14));
				graphics.clearRect(View.ORIGIN + View.LeftBorder + 100, 660, 100, 20);
				graphics.drawString("Level: " + level, View.ORIGIN + View.LeftBorder + 100, 670);
			}

			

		});

		Controller controller = new Controller(model, view);

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT: {
					controller.moveLeft();
					break;
				}
				case KeyEvent.VK_RIGHT: {
					controller.moveRight();
					break;
				}
				case KeyEvent.VK_DOWN: {
					controller.drop();
					break;
				}
				case KeyEvent.VK_SPACE: {
					controller.slideDown();
					break;
				}
				case KeyEvent.VK_UP: {
					controller.rotate();
					break;
				}
				case KeyEvent.VK_R: {
					controller.restart(TetrisModel.DEFAULT_WIDTH, TetrisModel.DEFAULT_HEIGHT,
							TetrisModel.DEFAULT_COLORS_NUMBER);
					break;
				}
				case KeyEvent.VK_Q: {
					System.exit(0);
					break;
				}
				}
			}
		});

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(controller::slideDown, 0, 1, TimeUnit.SECONDS);
		

	}

}
