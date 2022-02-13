package com.thevoxelbox.common.util;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;

public class RenderItemEx extends RenderItem
{
	public RenderItemEx()
	{
	}
	
	/**
	 * Render the item's icon or block into the GUI, including the glint effect.
	 */
	@Override
	public void renderItemAndEffectIntoGUI(FontRenderer fontRenderer, TextureManager texturemanager, ItemStack stack, int xPos, int yPos)
	{
		if (stack != null)
		{
			this.drawRect(Tessellator.instance, xPos, yPos, 16, 16, 1, 0, 0, 1);
			this.drawRect(Tessellator.instance, xPos + 1, yPos + 1, 14, 14, 0.6F, 0, 0, 1);
			super.renderItemAndEffectIntoGUI(fontRenderer, texturemanager, stack, xPos, yPos);
		}
	}
	
	@SuppressWarnings("cast")
	private void drawRect(Tessellator tessellator, int x, int y, int width, int height, float r, float g, float b, float a)
	{
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		glDisable(GL_DEPTH_TEST);
		glDepthFunc(GL_GREATER);
		glDisable(GL_LIGHTING);
		glDepthMask(false);
		glEnable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4f(r, g, b, a);
		tessellator.startDrawingQuads();
		tessellator.addVertex((double)(x + 0), (double)(y + 0), 0.0D);
		tessellator.addVertex((double)(x + 0), (double)(y + height), 0.0D);
		tessellator.addVertex((double)(x + width), (double)(y + height), 0.0D);
		tessellator.addVertex((double)(x + width), (double)(y + 0), 0.0D);
		tessellator.draw();
		glDepthMask(true);
		glPopAttrib();
	}
}
