package com.flashoverride.fitw;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.WeatherManager;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TileEntities.TEFireEntity;

public class Temperature {

	private static int temperatureLevel = 0;

	static Random rand = new Random();
	static ItemStack itemHead,itemChest,itemLegs,itemFeet;

	
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
		
		applyTemperature(world, player);
		
		
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
			if (WeatherManager.canSnow(world, x, y, z)) humidity = "snowy";
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

		return "The air feels " + fuzzyTemp + conjunction + humidity;
	}

	private static void applyTemperature(World world, EntityPlayer player)
	{
		temperatureLevel += applyTemperatureFromEnvironment(world, player);
	}

	public static int applyTemperatureFromEnvironment(World world, EntityPlayer player)
	{
		int x = (int)(player.posX);
		int y = (int)(player.posY);
		int z = (int)(player.posZ);
		
		float temperature = TFC_Climate.getHeightAdjustedTemp(world, x, y, z);
		temperature += applyTemperatureFromHeatSources(world, player);

		//if it's cold
		if(temperature <= 10)
		{
			int modifier = (int)((temperature - 10) * 15);
			if(rand.nextInt(1200 + modifier) < 10)
				return -1;
		}
		//if it's warm
		else if(temperature >= 30)
		{
			int modifier = Math.min(1199, (int)((temperature - 30) * 15));
			if(rand.nextInt(1200 - modifier) < 10)
				return 1;
		}
		else if(temperature <20)
		{
			if(temperatureLevel <= -1)
			{
				if(rand.nextInt(1200 -  Math.min(1199,(int)(temperature - 10)*15))<100)
					return 1;
			}
			else if(temperature >= 1)
			{
				if(rand.nextInt(1200 -  Math.min(1199,(int)(temperature - 10) * 15)) < 100)
					return -1;
			}
		}
		else if(temperature > 20)
		{
			if(temperatureLevel <= 1)
			{
				if(rand.nextInt(1200 -  Math.min(1199,(int)(temperature - 20)*10))<100)
					return 1;
			}
			else if(temperature > 1)
			{
				if(rand.nextInt(1200 -  Math.min(1199,(int)(temperature - 20)*10))<100)
					return -1;
			}
		}
		return 0;
	}

	public static float applyTemperatureFromHeatSources(World world, EntityPlayer player)
	{
		int x = (int)(player.posX);
		int y = (int)(player.posY);
		int z = (int)(player.posZ);
		
		float temperatureMod = 0;

		for(int i = x - 7; i<x +7;i++)
		{
			for(int j = y-3;j<y+3;j++)
			{
				for(int k = z-7;k<z+7;k++)
				{
					TileEntity te = world.getTileEntity(i, j, k);
					if((world.getBlock(i, j, k) == Blocks.lava ||
							world.getBlock(i, j, k) == TFCBlocks.Lava) ||
							(te != null && te instanceof TEFireEntity))
					{
						//returnAmount += (rand.nextInt(2000 - 198*(10-( (int)player.getDistance(i, j, k) )) )<10?1:0);
						//Lava averages 700-1200 C = 950 C, assume source is lava.
						double tempValue = 950;

						//if there is a firepit, use it's heat instead.
						if(te instanceof TEFireEntity)
							tempValue = ((TEFireEntity)te).fireTemp;

						//Just to make sure it's not 0
						double distanceSq = player.getDistanceSq(i, j, k) + 0.05;

						//radiation isn't perfect, so I don't know what a good numerator is, but it decreases with the square of the distance.
						//We can assume that the temperature of the actual heat source is when the player is directly touching it, which we have assigned to
						//a distanceSq of 0.05, therefore, the heat from such a heat source is = to the heat value * 0.05 divided by the distance squared
						tempValue *= (0.05)/distanceSq;
						temperatureMod += tempValue;
					}
				}
			}
		}
		return temperatureMod;
	}
}
