package com.thevoxelbox.common.util;

import static org.lwjgl.opengl.GL11.*;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

public abstract class TextureHelper
{
	public static void releaseTexture(ResourceLocation resource)
	{
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		ITextureObject textureObject = textureManager.getTexture(resource);
		
		if (textureObject != null)
		{
			int textureName = textureObject.getGlTextureId();
			glDeleteTextures(textureName);
			
			Map<ResourceLocation, ? extends ITextureObject> resourceToTextureMap = PrivateFields.resourceToTextureMap.get(textureManager);
			resourceToTextureMap.remove(resource);
		}
	}
}
