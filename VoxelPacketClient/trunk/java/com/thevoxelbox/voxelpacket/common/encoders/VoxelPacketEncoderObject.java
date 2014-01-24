package com.thevoxelbox.voxelpacket.common.encoders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;

/**
 * Fallback encoder which encodes using the serialisation API
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketEncoderObject implements IVoxelPacketEncoder<Serializable>
{
	private int dataTypeId;

	public VoxelPacketEncoderObject(int dataTypeId)
	{
		this.dataTypeId = dataTypeId;
	}

	@Override
	public int getDataTypeID()
	{
		return this.dataTypeId;
	}

	@Override
	public byte[] encode(Serializable object)
	{
		try
		{
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			new ObjectOutputStream(byteStream).writeObject(object);
			return byteStream.toByteArray();
		}
		catch (IOException e) {}
		
		return new byte[0];
	}

	@Override
	public Serializable decode(ByteBuffer dataBuffer)
	{
		if (dataBuffer != null)
		{		
			try
			{
				byte[] data = new byte[dataBuffer.remaining()];
				dataBuffer.get(data);
				ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
				Serializable object = (Serializable)inputStream.readObject();
				return object;
			}
			catch (IOException e) { }
			catch (ClassNotFoundException e) { }
		}
		
		return null;
	}
	
}
