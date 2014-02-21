package me.dylan.NNL.Visualizer;

import java.awt.Color;
import java.awt.Point;

public class NodePaint {
	Point nodeLocation;
	int size;
	Color color;
	/**
	 * This class is for painting all nodes(neurons, inputs, outputs)
	 * @param x
	 * @param y
	 * @param size The size(x and y) of the graphics to display
	 * @param nodeColor The color of the node
	 */
	public NodePaint(int x, int y, int size, Color nodeColor) {
		nodeLocation = new Point(x, y);
		color = nodeColor;
		this.size = size;
	}
	
	public void paintNode() {
		Color original = Display.getDisplayBackgroundColor();
		Display.setDisplayBackgroundColor(color);
		Display.fillOval(nodeLocation.x, nodeLocation.y, size, size);
		Display.setDisplayBackgroundColor(original);
	}
}