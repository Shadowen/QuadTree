package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;

import logic.Quadtree;
import logic.QTNode;
import logic.Unit;

public class DisplayPanel extends JPanel implements MouseListener,
		MouseMotionListener {
	private List<Unit> units;
	private Quadtree<Unit> qt;

	private Point mouseDownPosition;
	private Point mouseDragPosition;
	private Unit unitAtMousePoint;

	private Graphics g;

	public DisplayPanel(List<Unit> u, Quadtree<Unit> iqt) {
		units = u;
		qt = iqt;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDownPosition = e.getPoint();
		mouseDragPosition = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDownPosition = null;
		mouseDragPosition = null;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		mouseDragPosition = arg0.getPoint();
		mouseMoved(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		unitAtMousePoint = qt.getAt(arg0.getPoint());
	}

	@Override
	public void paintComponent(Graphics ig) {
		super.paintComponent(ig);
		g = ig;

		int totalDepth = qt.getDepth();
		qt.processNodes(tree -> {
			if (tree.nodes.isEmpty()) {
				Rectangle bounds = tree.getBounds();
				g.setColor(new Color(0, 0, 0, (int) (255 - (double) tree
						.getDepth() / totalDepth * 200)));
				g.drawString("Depth: " + String.valueOf(tree.getDepth()),
						bounds.x + 5, bounds.y + 15);
				g.drawString(
						"Objects: " + String.valueOf(tree.getObjects().size()),
						bounds.x + 5, bounds.y + 30);
				g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
			}
		});

		if (mouseDownPosition != null) {
			g.setColor(Color.GREEN);
			g.fillOval(mouseDownPosition.x - 5, mouseDownPosition.y - 5, 10, 10);

			int left = Math.min(mouseDragPosition.x, mouseDownPosition.x);
			int right = Math.max(mouseDragPosition.x, mouseDownPosition.x);
			int top = Math.min(mouseDragPosition.y, mouseDownPosition.y);
			int bottom = Math.max(mouseDragPosition.y, mouseDownPosition.y);
			g.drawRect(left, top, right - left, bottom - top);
		}
		if (mouseDragPosition != null) {
			g.setColor(Color.RED);
			g.fillOval(mouseDragPosition.x - 5, mouseDragPosition.y - 5, 10, 10);
		}

		g.setColor(Color.BLACK);
		for (Unit u : units) {
			drawUnit(u);
		}

		if (unitAtMousePoint != null) {
			g.setColor(Color.BLUE);
			drawUnit(unitAtMousePoint);
		}
	}

	/**
	 * Draw a single unit.
	 * 
	 * @param u
	 *            The unit to be drawn
	 */
	private void drawUnit(Unit u) {
		g.fillRect(u.shape.getBounds().x, u.shape.getBounds().y,
				u.shape.getBounds().width, u.shape.getBounds().height);
	}
}
