package com.thevoxelbox.voxelpacket.common.encoders;

import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;

/**
 * Encodes a flat array of integers
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketEncoderIntArray implements IVoxelPacketEncoder<int[]>
{
	private int dataTypeId;

	public VoxelPacketEncoderIntArray(int dataTypeId)
	{
		this.dataTypeId = dataTypeId;
	}

	@Override
	public int getDataTypeID()
	{
		return this.dataTypeId;
	}

	@Override
	public byte[] encode(int[] object)
	{
		if (object == null || object.length == 0)
		{
			return new byte[] { (byte)0 };
		}
		
		ByteBuffer encodeBuffer = ByteBuffer.allocate((object.length * 4) + 4);
		encodeBuffer.putInt(object.length);
		
		for (int index = 0; index < object.length; index++)
			encodeBuffer.putInt(object[index]);
		
		encodeBuffer.flip();
		
		byte[] encoded = new byte[encodeBuffer.limit()];
		encodeBuffer.get(encoded);
		
		return encoded;
	}

	@Override
	public int[] decode(ByteBuffer dataBuffer)
	{
		if (dataBuffer != null && dataBuffer.limit() >= 4)
		{
			int arrayLength = dataBuffer.getInt();
			int[] decoded = new int[arrayLength];
			
			for (int index = 0; index < arrayLength; index++)
				decoded[index] = dataBuffer.getInt();
			
			return decoded;
		}
		return new int[0];
	}
	
	
}
