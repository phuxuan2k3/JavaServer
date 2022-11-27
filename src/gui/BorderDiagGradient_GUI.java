package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Area;

import javax.swing.border.Border;

public class BorderDiagGradient_GUI implements Border {
	private Insets margin;
	private Color colorTopLeft = Color.RED;
	private Color colorBottomRight = Color.BLUE;
	private int scaledVelocity = 0;
	private int scaler = 0;
	private int dir = 0;
	private int[] res = new int[] { -1, -1, -1, -1 };
	private boolean firstTime = true;

	/**
	 * 0 = top, 1 = right, 2 = bottom, 3 = left
	 */

	public BorderDiagGradient_GUI(int top, int left, int bottom, int right, Color topLeft, Color bottomRight) {
		super();
		margin = new Insets(top, left, bottom, right);
		this.colorBottomRight = bottomRight;
		this.colorTopLeft = topLeft;
	}

	public BorderDiagGradient_GUI(int same, Color topLeft, Color bottomRight) {
		super();
		margin = new Insets(same, same, same, same);
		this.colorBottomRight = bottomRight;
		this.colorTopLeft = topLeft;
	}

	public void setSpinDiag(int scaledVelocity) {
		this.scaledVelocity = scaledVelocity;
	}

	public void resetSpinDiag(int scaledVelocity) {
		this.scaledVelocity = 0;
		this.scaler = 0;
		this.dir = 0;
	}

	private int[] scaler(int x, int y, int width, int height) {
		int[] limit = new int[] { x + width, y + height, x, y };

		this.scaler += ((width + height) / 100) * this.scaledVelocity;

		if (this.dir == 0) {
			res[0] += scaler;
			res[2] -= scaler;
			// Off limit, Change axis
			if (res[0] >= limit[0] || res[2] <= limit[2]) {
				res[0] = limit[0];
				res[2] = limit[2];
				this.scaler = 0;
				this.dir = 1;
			}
		} else if (this.dir == 1) {
			res[1] += scaler;
			res[3] -= scaler;
			// Off limit, Change axis
			if (res[1] >= limit[1] || res[3] <= limit[3]) {
				res[1] = limit[1];
				res[3] = limit[3];
				this.scaler = 0;
				this.dir = 2;
			}
		} else if (this.dir == 2) {
			res[0] -= scaler;
			res[2] += scaler;
			// Off limit, Change axis
			if (res[0] <= limit[2] || res[2] >= limit[0]) {
				res[0] = limit[2];
				res[2] = limit[0];
				this.scaler = 0;
				this.dir = 3;
			}
		} else if (this.dir == 3) {
			res[1] -= scaler;
			res[3] += scaler;
			// Off limit, Change axis
			if (res[1] <= limit[3] || res[3] >= limit[1]) {
				res[1] = limit[3];
				res[3] = limit[1];
				this.scaler = 0;
				this.dir = 0;
			}
		}

		return res;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;
		if (firstTime == true) {
			res[0] = x;
			res[1] = y;
			res[2] = x + width;
			res[3] = y + height;
			firstTime = false;
		}

		int[] pos = this.scaler(x, y, width, height);
		g2d.setPaint(new GradientPaint(pos[0], pos[1], colorTopLeft, pos[2], pos[3], colorBottomRight));

		Area border = new Area(new Rectangle(x, y, width, height));
		border.subtract(new Area(new Rectangle(x + margin.left, y + margin.top, width - margin.left - margin.right,
				height - margin.top - margin.bottom)));
		g2d.fill(border);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return margin;
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

}
