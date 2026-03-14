package games;

import java.awt.*;
import javax.swing.*;

public class ViewerPanel extends JPanel{
	public void paintComponent(Graphics g){
		Graphics2D g2D=(Graphics2D)g;
		super.paintComponent(g2D);		
		g2D.drawImage(PlanetLanding.landscape, 0, 0, getWidth()-1, getHeight()-1, (int)PlanetLanding.landscapeX,
				(int)PlanetLanding.landscapeY, (int)(PlanetLanding.landscapeX+getWidth()-1), 
				(int)(PlanetLanding.landscapeY+getHeight()-1), null);
		//add pad
		g2D.drawImage(PlanetLanding.pad, (int)(PlanetLanding.padX-PlanetLanding.landscapeX), 
				(int)(PlanetLanding.padY-PlanetLanding.landscapeY), null);	
		//add thrusters if applied
		if(PlanetLanding.vThrustOn) {
			g2D.drawImage(PlanetLanding.vThrust,
					(int)(PlanetLanding.landerXView+0.5*PlanetLanding.landerWidth-0.5*PlanetLanding.vThrustWidth),
					(int)(PlanetLanding.landerYView+PlanetLanding.landerHeight-PlanetLanding.vThrustHeight), null);
			PlanetLanding.vThrustOn=false;
		}		
		if (PlanetLanding.lThrustOn) {
			g2D.drawImage(PlanetLanding.hThrust,(int)(PlanetLanding.landerXView)-45,
					(int)(PlanetLanding.landerYView+95), null);
			PlanetLanding.lThrustOn=false;
		}		
		if (PlanetLanding.rThrustOn) {
			g2D.drawImage(PlanetLanding.hThrust,
					(int)(PlanetLanding.landerXView+PlanetLanding.landerWidth-PlanetLanding.hThrustWidth),
					(int)(PlanetLanding.landerYView+80), null);
			PlanetLanding.rThrustOn=false;
		}	
		//add lander		
		g2D.drawImage(PlanetLanding.lander,(int)PlanetLanding.landerXView,(int)PlanetLanding.landerYView,null);
		g2D.dispose();
	}
}
