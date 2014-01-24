package com.thevoxelbox.voxelpacket.common.encoders;

import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.common.data.Marshal;
import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;

/**
 * Encodes ints
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketEncoderInt implements IVoxelPacketEncoder<Integer>
{
	private int dataTypeId;

	public VoxelPacketEncoderInt(int dataTypeId)
	{
		this.dataTypeId = dataTypeId;
	}

	@Override
	public int getDataTypeID()
	{
		return this.dataTypeId;
	}

	@Override
	public byte[] encode(Integer object)
	{
		if (object != null)
			return Marshal.unpackByte(object);
		
		return new byte[0];
	}

	@Override
	public Integer decode(ByteBuffer dataBuffer)
	{
		try
		{
			int value = dataBuffer.getInt();
			return Integer.valueOf(value);
		}
		catch (Exception ex) {}
		
		return 0;
	}
}
