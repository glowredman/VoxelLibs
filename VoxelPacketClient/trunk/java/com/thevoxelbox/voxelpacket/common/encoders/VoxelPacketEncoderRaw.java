package com.thevoxelbox.voxelpacket.common.encoders;

import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;

/**
 * Raw encoder which does nothing
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketEncoderRaw implements IVoxelPacketEncoder<byte[]>
{
	private int dataTypeId;
	
	public VoxelPacketEncoderRaw(int dataTypeId)
	{
		this.dataTypeId = dataTypeId;
	}

	@Override
	public int getDataTypeID()
	{
		return this.dataTypeId;
	}

	@Override
	public byte[] encode(byte[] object)
	{
		if (object != null)
		{
			return object;
		}
	
		return new byte[0];
	}

	@Override
	public byte[] decode(ByteBuffer dataBuffer)
	{
		if (dataBuffer == null)
			return new byte[0];
		
		byte[] data = new byte[dataBuffer.remaining()];
		dataBuffer.get(data);
		return data;
	}
	
}
