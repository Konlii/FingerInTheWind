package com.flashoverride.fitw;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod( modid = FITW.MODID, name = FITW.NAME, version = FITW.VERSION )
public class FITW {
    public static final String MODID = "fitw";
    public static final String VERSION = "1.0";
    public static final String NAME = "Finger in the Wind";
    public static final String CHANNEL = MODID;
	
	@Instance(FITW.MODID)
	public static FITW instance;
	
	@EventHandler
	public static void preInit( FMLPreInitializationEvent event ) {
		ConfigHandler.init(event.getSuggestedConfigurationFile());
	}

	@EventHandler
	public static void init( FMLInitializationEvent event ) {
	}

	@EventHandler
	public static void postInit( FMLPostInitializationEvent event ) {
		MinecraftForge.EVENT_BUS.register(new FITWGUI(Minecraft.getMinecraft()));
	}
    
    
    
    
}
