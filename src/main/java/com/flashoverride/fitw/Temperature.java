package com.flashoverride.fitw;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.WeatherManager;
import com.bioxx.tfc.api.TFCBlocks;

public class Temperature {

	public static String getTemp(World world, EntityPlayer player)
	{
		int x = (int)(player.posX);
		int y = (int)(player.posY);
		int z = (int)(player.posZ);
		
		Float temp = TFC_Climate.getHeightAdjustedTemp(world, x, y, z);
		String fuzzyTemp = "";
		Float rain = TFC_Climate.getRainfall(world, x, y, z);
		String humidity = "";
		String conjunction = " and ";
		boolean isSpring = false;
		
		if (rain<250) humidity = "very dry";
		else if (rain<500) humidity = "dry";
		else if (rain<1000) conjunction = "";
		else if (rain<1500) humidity = "damp";
		else if (rain<2500) humidity = "humid";
		else if (rain<4500) humidity = "very humid";
		else humidity = "wet";
		
		if (TFC_Climate.isSwamp(world, x, y, z)) humidity = "swampy";
		
		if (world.isRaining())
			{
			conjunction = " and ";
			if (world.isThundering()) humidity = "stormy";
			else if (WeatherManager.canSnow(world, x, y, z)) humidity = "snowy";
			else humidity = "rainy";
			}
		
		
		if (temp<-40) fuzzyTemp = "deathly cold";
		else if (temp<-20) fuzzyTemp = "bone-chillingly cold";
		else if (temp<-10) fuzzyTemp = "extremely cold";
		else if (temp<0) fuzzyTemp = "freezing";
		else if (temp<5) fuzzyTemp = "very cold";
		else if (temp<10) fuzzyTemp = "cold";
		else if (temp<20) fuzzyTemp = "cool";
		else if (temp<25) fuzzyTemp = "nice";
		else if (temp<30) fuzzyTemp = "warm";
		else if (temp<35) fuzzyTemp = "hot";
		else if (temp<45) fuzzyTemp = "very hot";
		else fuzzyTemp = "burning hot";

		if (player.isInWater())
		{
			FITW.wetCooldown = 40;
		}
		
		if (player.isBurning()) return "The air is filled with smoke and the stench of burning flesh";
		else if (FITW.wetCooldown > 0)
		{
			int blockX = MathHelper.floor_double(player.posX);
		    int blockY = MathHelper.floor_double(player.boundingBox.maxY);
		    int blockZ = MathHelper.floor_double(player.posZ);
		    Block block;
		    
		    	while (blockY >= MathHelper.floor_double(player.boundingBox.minY)-1)
		    	{
		    		block = world.getBlock(blockX, blockY, blockZ);
		    		
		    		if (block.equals(TFCBlocks.hotWater) || block.equals(TFCBlocks.hotWaterStationary))
		    		{
		    			isSpring = true;
		    		}
		    		--blockY;
		    	}
		    
			FITW.wetCooldown -= 1;
			if (isSpring) return "The water feels rejuvenating";
			else return "The water feels " + fuzzyTemp;
		}
		else return "The air feels " + fuzzyTemp + conjunction + humidity;
	}
}
