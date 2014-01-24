package com.thevoxelbox.voxelpacket.common.encoders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;

/**
 * NBT Encoder for world entities, allows entities to actually be sent to the client
 * 
 * @author Adam Mummery-Smith
 */
public class VoxelPacketEncoderEntityNBT implements IVoxelPacketEncoder<Entity>
{
	private int dataTypeId;

	public VoxelPacketEncoderEntityNBT(int dataTypeId)
	{
		this.dataTypeId = dataTypeId;
	}

	@Override
	public int getDataTypeID()
	{
		return this.dataTypeId;
	}

	@Override
	public byte[] encode(Entity object)
	{
		try
		{
			NBTTagCompound entityData = new NBTTagCompound();
			
			if (object.writeToNBTOptional(entityData))
			{
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				CompressedStreamTools.write(entityData, new DataOutputStream(byteStream));
				return byteStream.toByteArray();
			}
			
			return new byte[0];
		}
		catch (Exception ex)
		{
			return new byte[0];
		}
	}

	@Override
	public Entity decode(ByteBuffer dataBuffer)
	{
		World theWorld = Minecraft.getMinecraft().theWorld;
		
		if (dataBuffer != null && theWorld != null)
		{		
			try
			{
				byte[] data = new byte[dataBuffer.remaining()];
				dataBuffer.get(data);
				
				if (data.length > 0)
				{
					NBTTagCompound entityData = CompressedStreamTools.read(new DataInputStream(new ByteArrayInputStream(data)));
					Entity entity = EntityList.createEntityFromNBT(entityData, theWorld);
					
					if (entity != null && !theWorld.field_147482_g.contains(entity)) // loadedTileEntityList
					{
						theWorld.joinEntityInSurroundings(entity);
					}
					
					return entity;
				}
			}
			catch (Exception e) { }
		}
		
		return null;
	}
}
