package com.thevoxelbox.voxelpacket.common.encoders;

import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;

public class VoxelPacketEncoderNull implements IVoxelPacketEncoder<Object>
{
	private int dataTypeId;
	
	public VoxelPacketEncoderNull(int dataTypeId)
	{
		this.dataTypeId = dataTypeId;
	}

	@Override
	public int getDataTypeID()
	{
		return this.dataTypeId;
	}

	@Override
	public byte[] encode(Object object)
	{
		return new byte[0];
	}

	@Override
	public Object decode(ByteBuffer dataBuffer)
	{
		return null;
	}

}
