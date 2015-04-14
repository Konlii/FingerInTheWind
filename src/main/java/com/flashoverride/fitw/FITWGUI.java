package com.flashoverride.fitw;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FITWGUI extends Gui {
	private Minecraft mc;

	public FITWGUI(Minecraft mc) {
		super();
 		this.mc = mc;
	}
	
	@SubscribeEvent
	(priority = EventPriority.NORMAL)
	public void onRenderHotBar(RenderGameOverlayEvent event) {
		FontRenderer fontRender = this.mc.fontRenderer;

		if (event.isCancelable() || event.type != ElementType.HOTBAR || mc.gameSettings.showDebugInfo) return;

		fontRender.drawStringWithShadow(Temperature.getTemp(mc.theWorld, mc.thePlayer), ConfigHandler.xPos_actual, ConfigHandler.yPos_actual, ConfigHandler.color_actual);
	}
}
