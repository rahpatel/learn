package drawings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Program to do recursive drawings.
 * 
 * @author David Matuszek
 * @author Rahul Patel
 * @version 1.0
 */
public class RecursiveDrawings extends JFrame implements ActionListener {
	private JPanel drawingPanel = new JPanel();
	private JPanel controlPanel = new JPanel();
	private JRadioButton[] depthButtons = new JRadioButton[6];
	private ButtonGroup group = new ButtonGroup();
	private JButton[] drawingButtons = new JButton[6];

	private int depth; // maximum depth of the recursion
	private int drawingNumber; // which drawing to make

	/**
	 * Main method for this application.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {
		new RecursiveDrawings().run();
	}

	/**
	 * Runs this RecursiveDrawings application.
	 */
	public void run() {
		createWidgets();
		layOutGui();
		setSize(400, 500);
		attachListeners();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Creates all the components needed by the application.
	 */
	private void createWidgets() {
		for (int i = 0; i < 6; i++) {
			depthButtons[i] = new JRadioButton(" " + (i + 1));
			group.add(depthButtons[i]);
		}
		depthButtons[0].setSelected(true);
		for (int i = 0; i < 6; i++) {
			drawingButtons[i] = new JButton("Drawing " + (i + 1));
		}
		drawingButtons[0].setText("Squares");
		drawingButtons[1].setText("Circles");
		drawingButtons[2].setText("Triangles");
		drawingButtons[3].setText("Tree");
		drawingButtons[4].setText("CirclesIn");
		drawingButtons[5].setText("SquareOut");

	}

	/**
	 * Arranges the components for this application.
	 */
	private void layOutGui() {
		add(drawingPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
		controlPanel.setLayout(new GridLayout(3, 4));
		for (int i = 0; i < 3; i++) {
			controlPanel.add(depthButtons[i]);
			controlPanel.add(depthButtons[i + 3]);
			controlPanel.add(drawingButtons[i]);
			controlPanel.add(drawingButtons[i + 3]);
		}
		setSize(800, 800);
		setLocation(400, 100);
	}

	/**
	 * Attaches this RecursiveDrawings object as a listener for all the drawing
	 * buttons. The depth radio buttons don't get listeners.
	 */
	private void attachListeners() {
		for (int i = 0; i < 6; i++) {
			drawingButtons[i].addActionListener(this);
		}
	}

	/**
	 * Responds to a button press by setting the global variables
	 * <code>depth</code> and <code>drawingNumber</code>, then requesting that
	 * the painting be done.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < 6; i++) {
			if (depthButtons[i].isSelected())
				depth = i + 1;
			if (e.getSource() == drawingButtons[i])
				drawingNumber = i + 1;
		}
		repaint();
	}

	/**
	 * Paints one of the drawings, based on the global variables
	 * <code>depth</code> and <code>drawingNumber</code>.
	 * 
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		int x1, x2, x3, y1, y2, y3, xn, yn;
		super.paint(g);
		int width = drawingPanel.getWidth();
		int height = drawingPanel.getHeight();

		// clear panel
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.BLACK);

		g.setClip(0, 0, width, height);
		int x = drawingPanel.getWidth() / 2;
		int y = drawingPanel.getHeight() / 2 + 10;
		int length = Math.min(x, y - 20);

		xn = x - length / 2;
		yn = y - length / 2;
		x1 = xn;
		y1 = yn;
		x2 = xn;
		x3 = xn + length;
		y2 = yn + length;
		y3 = yn + length;

		int[] xs = { x1, x2, x3 };
		int[] ys = { y1, y2, y3 };

		if (drawingNumber == 1)
			squares(g, x, y, length, depth);
		else if (drawingNumber == 2)
			circles(g, x, y, length, depth);
		else if (drawingNumber == 3)
			triangles(g, xs, ys, length, depth);
		else if (drawingNumber == 4)
			lines(g, x, y, length / 2, depth);
		else if (drawingNumber == 5)
			circlesinside(g, x, y, length, depth);
		else if (drawingNumber == 6)
			surroundSquares(g, x, y, length, depth);
	}

	private void squares(Graphics g, int x, int y, int length, int depth) {
		if (depth == 0)
			return;

		g.drawRect(x - length / 2, y - length / 2, length, length);
		int x1 = x - length / 2;
		int x2 = x + length / 2;
		int y1 = y - length / 2;
		int y2 = y + length / 2;

		squares(g, x1, y1, length / 2, depth - 1);
		squares(g, x2, y1, length / 2, depth - 1);
		squares(g, x1, y2, length / 2, depth - 1);
		squares(g, x2, y2, length / 2, depth - 1);

	}

	private void circles(Graphics g, int x, int y, int length, int depth) {

		if (depth % 2 == 0)
			g.setColor(Color.MAGENTA);
		else
			g.setColor(Color.CYAN);

		if (depth == 0)
			return;

		g.drawOval(x - length / 2, y - length / 2, length, length);
		int x1 = x - length / 2 - length / 4;
		int x2 = x + length / 2 + length / 4;
		int y1 = y - length / 2 - length / 4;
		int y2 = y + length / 2 + length / 4;

		circles(g, x, y1, length / 2, depth - 1);
		circles(g, x2, y, length / 2, depth - 1);
		circles(g, x, y2, length / 2, depth - 1);
		circles(g, x1, y, length / 2, depth - 1);

	}

	public void drawTriangle(Graphics g, int[] x, int[] y) {
		g.setColor(Color.BLUE);
		g.drawPolygon(x, y, 3);
	}

	public void triangles(Graphics g, int[] ax, int[] ay, int length, int depth) {
		if (depth == 0)
			return;

		drawTriangle(g, ax, ay);
		int x1, y1, x2, y2, x3, y3;
		x1 = ax[0];
		x2 = ax[1];
		x3 = ax[2];
		y1 = ay[0];
		y2 = ay[1];
		y3 = ay[2];

		int[] ax1 = { x1, x2, x2 + length / 2 };
		int[] ay1 = { y1, y2 - length / 2, y2 - length / 2 };
		int[] ax2 = { x2, x2, x2 + length / 2 };
		int[] ay2 = { y2 - length / 2, y2, y2 };
		int[] ax3 = { x2 + length / 2, x2 + length / 2, x3 };
		int[] ay3 = { y2 - length / 2, y2, y3 };

		triangles(g, ax1, ay1, length / 2, depth - 1);
		triangles(g, ax2, ay2, length / 2, depth - 1);
		triangles(g, ax3, ay3, length / 2, depth - 1);
	}

	public void lines(Graphics g, int x, int y, int length, int depth) {

		if (depth == 0)
			return;

		if (depth % 2 == 0)
			g.setColor(Color.YELLOW);
		else
			g.setColor(Color.GREEN);

		int x1, y1, x2, y2;

		x1 = (int) (x + length * 0.5);
		y1 = (int) (y - length * 1.732 * 0.5);

		x2 = (int) (x - length * 0.5);
		y2 = (int) (y - length * 1.732 * 0.5);

		g.drawLine(x, y, x1, y1);
		g.drawLine(x, y, x2, y2);

		lines(g, x1, y1, length / 2, depth - 1);
		lines(g, x2, y2, length / 2, depth - 1);

	}

	private void circlesinside(Graphics g, int x, int y, int length, int depth) {
		if (depth == 0)
			return;

		if (depth % 2 == 0)
			g.setColor(Color.BLUE);
		else
			g.setColor(Color.MAGENTA);

		g.fillOval(x - length / 2, y - length / 2, length, length);
		int x1 = x - length / 4;
		int x2 = x + length / 4;

		circlesinside(g, x1, y, length / 2, depth - 1);
		circlesinside(g, x2, y, length / 2, depth - 1);

	}

	public void surroundSquares(Graphics g, int x, int y, int length, int depth) {
		if (depth == 0)
			return;

		if (depth % 2 == 0)
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLUE);
		g.fillRect(x - length / 2, y - length / 2, length, length);
		int x1 = x - length;
		int x2 = x + length;
		int y1 = y - length;
		int y2 = y + length;

		surroundSquares(g, x, y1, length / 3, depth - 1);
		surroundSquares(g, x2, y, length / 3, depth - 1);
		surroundSquares(g, x, y2, length / 3, depth - 1);
		surroundSquares(g, x1, y, length / 3, depth - 1);

	}

}
