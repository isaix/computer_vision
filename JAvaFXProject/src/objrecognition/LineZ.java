package objrecognition;

import org.opencv.core.Point;

public class LineZ   {

	Point p1;
	Point p2;

	public LineZ(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}


	@Override
	public boolean equals(Object o) {
		boolean moved = false;
		LineZ compareLine = (LineZ) o;
		if (p1.x - 8.0 < compareLine.p1.x) {
			if (compareLine.p2.x < p2.x + 8.0) {//
				if (p1.y - 8.0 < compareLine.p1.y) { //
					if (compareLine.p2.y < p2.y + 8.0) { //
						if (p1.x + 8.0 > compareLine.p1.x) { //
							if (compareLine.p2.x > p2.x - 8.0) { //
								if (p1.y + 8.0 > compareLine.p1.y) { //
									if (compareLine.p2.y > p2.y - 8.0) {
										if (p1.x - 8.0 > compareLine.p1.x) {
											if (compareLine.p2.x < p2.x + 8.0) {
												return true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}