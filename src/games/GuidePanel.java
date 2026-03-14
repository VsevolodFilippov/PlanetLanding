package games;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;


public class GuidePanel extends JPanel{
	public void paintComponent(Graphics g){
		Graphics2D g2D=(Graphics2D)g;
		super.paintComponent(g2D);
		int x=(int)((PlanetLanding.landerX+PlanetLanding.landerWidth/2)*getWidth()/PlanetLanding.landscapeWidth-5);
		Rectangle2D.Double guideRectangle=new Rectangle2D.Double(x, 1, 10, getHeight()-30);
		g2D.setPaint(Color.GREEN);
		g2D.fill(guideRectangle);
		x = (int)((PlanetLanding.padX+PlanetLanding.padWidth/2)*getWidth()/PlanetLanding.landscapeWidth-5);
		guideRectangle = new Rectangle2D.Double(x, 1, 10, getHeight()-40);
		g2D.setPaint(Color.RED);
		g2D.fill(guideRectangle);
		g2D.dispose();		
	}
}
