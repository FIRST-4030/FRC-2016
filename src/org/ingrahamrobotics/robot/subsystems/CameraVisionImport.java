package org.ingrahamrobotics.robot.subsystems;

import org.ingrahamrobotics.robot.output.Settings;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Camera extends Subsystem
{
	private ColorImage img = null;
	private BinaryImage thresholdHSLImage = null;
	private TargetCamera camera = null;
	private int imagenumber = 0;
	private Target target = null;

	private class Target
	{
		private double height;
		private double width;
		private double x;
		private double y;
		private double centerX;
		private double centerY;
		private double certainty; //Certainty of the shot
		
		private Target(ParticleAnalysisReport report)
		{
			height=report.boundingRectHeight;
            		width=report.boundingRectWidth;
            		x=report.boundingRectLeft;
            		y=report.boundingRectTop;
            		centerX=x+width/2;
            		centerY=y+height/2;
		}
		
	}
	
	@Override
	protected void initDefaultCommand() 
	{
		
	}
	
	public boolean hasTarget()
	{
		return target!=null;
	}
	public ColorImage getImage()
	{
        return img;
    }
    public BinaryImage getThresholdHSLImage() 
    {
        return thresholdHSLImage;
    }
    public void saveimage() throws NIVisionException
    {
        String pic="/Test_Capture";
        System.out.println("Saveing picture to"+pic+imagenumber);
        thresholdHSLImage.write("/Test_Capture"+imagenumber+++".png");
    } 
    
/*
    public void refreshImage() throws NIVisionException
	{
		//try to get image
		try	{
			img = camera.getImage();
		}catch(Exception e)	{
			System.err.println("Done goofed, cannot retrieve image");
		}
		try	{
			BinaryImage = img.HSLThreshold();
		}catch(Exception e)
		{
			System.err.println("Done goofed, threshold failed");
		}
		try{
			IDTargets();
		}catch(Exception e)
		{
			System.err.println("Done goofed, could not identify targets");
		}
	}
*/
    private BinaryImage HSLThreshold() throws NIVisionException
	{
	        if(img==null)
	        {
	        	return null;
	        }
	        else
	        {
	        	return img.thresholdHSL(Settings.Key.VISION_H_LOW.getInt(), Settings.Key.VISION_H_HIGH.getInt(),  Settings.Key.VISION_S_LOW.getInt(), Settings.Key.VISION_S_HIGH.getInt(), Settings.Key.VISION_L_LOW.getInt(),Settings.Key.VISION_L_HIGH.getInt());
	        }
	}
	
    private void IDTargets() throws NIVisionException
    {
        ParticleAnalysisReport[] reports = thresholdHSLImage.getOrderedParticleAnalysisReports();
        if (reports[0].particleArea <600)
        {
        	target =null;
        }
        else
        {
        	target = new Target(reports[0]);
        }
    }
	
	private void flushImages() throws NIVisionException
	{
        target=null;
        if(img!=null){
            img.free();
        }
        if(thresholdHSLImage!=null){
            thresholdHSLImage.free();
        }
    }

}
