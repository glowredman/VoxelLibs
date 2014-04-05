package com.thevoxelbox.voxelpacket.common.encoders;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R2.NBTTagCompound;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;
import com.thevoxelbox.voxelpacket.exceptions.InvalidReplicationException;

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
			
			if (object.c(entityData))
			{
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				NBTCompressedStreamTools.a(entityData, (DataOutput) new DataOutputStream(byteStream));
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
	public Entity decode(ByteBuffer dataBuffer) throws InvalidReplicationException
	{
		throw new InvalidReplicationException("Cannot replicate entities from client to server!");
		
//		World theWorld = Util.GetMinecraft().theWorld;
//		
//		if (dataBuffer != null && theWorld != null)
//		{		
//			try
//			{
//				byte[] data = new byte[dataBuffer.remaining()];
//				dataBuffer.get(data);
//				
//				if (data.length > 0)
//				{
//					NBTTagList tagList = (NBTTagList) NBTBase.b(new DataInputStream(new ByteArrayInputStream(data)));
//					
//					if (tagList.tagCount() > 0)
//					{
//						NBTTagCompound entityData = (NBTTagCompound) tagList.tagAt(0);
//						Entity entity = EntityList.createEntityFromNBT(entityData, theWorld);
//						
//						if (entity != null && !theWorld.loadedTileEntityList.contains(entity))
//						{
//							theWorld.joinEntityInSurroundings(entity);
//						}
//						
//						return entity;
//					}
//				}
//			}
//			catch (Exception e) { }
//		}
//		
//		return null;
	}
}
