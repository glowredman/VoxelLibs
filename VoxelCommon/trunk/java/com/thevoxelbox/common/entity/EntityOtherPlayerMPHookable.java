package com.thevoxelbox.common.entity;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;
import com.thevoxelbox.common.interfaces.IEntityOtherPlayerMPHook;
import com.thevoxelbox.common.util.PrivateFields;

/**
 * Entity which VoxelCommon replaces other player entities with in order to hook setting up the sking
 *
 * @author Adam Mummery-Smith
 */
public class EntityOtherPlayerMPHookable extends EntityOtherPlayerMP
{
	/**
	 * Callbacks
	 */
	private static List<IEntityOtherPlayerMPHook> hooks = new LinkedList<IEntityOtherPlayerMPHook>();
	
	/**
	 * Add a new hook
	 */
	public static void addHook(IEntityOtherPlayerMPHook hook)
	{
		if (!hooks.contains(hook))
		{
			hooks.add(hook);
		}
	}

	/**
	 * @param world
	 * @param profile
	 */
	public EntityOtherPlayerMPHookable(World world, GameProfile profile)
	{
		super(world, profile);
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.client.entity.AbstractClientPlayer#setupCustomSkin()
	 */
	@Override
	protected void setupCustomSkin()
	{
		boolean hooked = false;
		
		for (IEntityOtherPlayerMPHook hook : hooks)
			hooked |= hook.setupSkin(this.getCommandSenderName(), this);
				
		if (!hooked)
			super.setupCustomSkin();
	}

	/**
	 * @param skinResource
	 */
	public void setSkinResource(ResourceLocation skinResource)
	{
		PrivateFields.skinResource.set(this, skinResource);
	}

	/**
	 * @param cloakResource
	 */
	public void setCloakResource(ResourceLocation cloakResource)
	{
		PrivateFields.cloakResource.set(this, cloakResource);
	}

	/**
	 * @param skinTexture
	 */
	public void setSkinTexture(ThreadDownloadImageData skinTexture)
	{
		PrivateFields.skinTexture.set(this, skinTexture);
	}

	/**
	 * @param cloakTexture
	 */
	public void setCloakTexture(ThreadDownloadImageData cloakTexture)
	{
		PrivateFields.cloakTexture.set(this, cloakTexture);
	}
}
