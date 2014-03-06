package com.thevoxelbox.voxelpacket.common.encoders;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;

/**
 * String encoder, don't use native serialization for strings because it's not as efficient
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketEncoderString implements IVoxelPacketEncoder<String>
{
	private int dataTypeId;

	public VoxelPacketEncoderString(int dataTypeId)
	{
		this.dataTypeId = dataTypeId;
	}
	
	@Override
	public int getDataTypeID()
	{
		return this.dataTypeId;
	}

	@Override
	public byte[] encode(String object)
	{
		try
		{
			return object.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return new byte[0];
		}
	}

	@Override
	public String decode(ByteBuffer dataBuffer)
	{
		if (dataBuffer == null)
			return "";
		
		try
		{
			byte[] data = new byte[dataBuffer.remaining()];
			dataBuffer.get(data);
			return new String(data, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return "";
		}
	}
	
}
