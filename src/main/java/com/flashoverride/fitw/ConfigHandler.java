package com.flashoverride.fitw;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigHandler {
	public static int xPos_actual;
	public static int xPos_default = 2;
	public static int yPos_actual;
	public static int yPos_default = 2;
	
	public static int color_actual;
	public static String color_default = "FFFFFF";
	
	public static void init(File configFile) {
		Configuration config = new Configuration(configFile);

		config.load();
		
		Property xPos = config.get(Configuration.CATEGORY_GENERAL, "xPosition", xPos_default);
		xPos.comment = "X position in pixels from left side of the screen";
		xPos_actual = xPos.getInt();
		
		Property yPos = config.get(Configuration.CATEGORY_GENERAL, "yPosition", yPos_default);
		yPos.comment = "Y position in pixels from top of the screen";
		yPos_actual = yPos.getInt();
				
		Property color = config.get(Configuration.CATEGORY_GENERAL, "Color", color_default);
		color.comment = "The color of the clock text in 6 digit HEX";
		try {
			color_actual = Integer.parseInt(color.getString(), 16);
		} catch (NumberFormatException nfe) {
			color_actual = 0xFFFFFF;
		}
		
		config.save();
	}
}
