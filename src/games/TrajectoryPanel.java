package games;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

class TrajectoryPanel extends JPanel{
	public void paintComponent(Graphics g){
		Graphics2D g2D = (Graphics2D)g;
		super.paintComponent(g2D);
		double trajectoryXScale=(double)(getWidth())/PlanetLanding.landscapeWidth;
		double trajectoryYScale=(double)(getHeight())/PlanetLanding.landscapeHeight;
		g2D.setStroke(new BasicStroke(2));
		g2D.setPaint(Color.RED);
		Line2D.Double trajectoryLine=new Line2D.Double(trajectoryXScale*(PlanetLanding.landerX0+PlanetLanding.landerWidth/2),
					trajectoryYScale*(PlanetLanding.landerY0+PlanetLanding.landerHeight),
						trajectoryXScale*(PlanetLanding.padX+PlanetLanding.padWidth/2),
							trajectoryYScale*(PlanetLanding.padY+PlanetLanding.padHeight));
		g2D.draw(trajectoryLine);
		g2D.setStroke(new BasicStroke(1));
		for (int i=0; i<PlanetLanding.trajectoryPoints.size(); i++) {
			Point2D.Double thisPoint=(Point2D.Double)PlanetLanding.trajectoryPoints.elementAt(i);
			Ellipse2D.Double trajectoryCircle=new Ellipse2D.Double(trajectoryXScale*thisPoint.getX()-3,
					trajectoryYScale*thisPoint.getY()-3,6,6);
			g2D.setPaint(Color.GREEN);
			g2D.draw(trajectoryCircle);
		}
		g2D.dispose();		
	}
}
