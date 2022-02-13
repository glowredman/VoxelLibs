package com.thevoxelbox.voxelpacket.common.encoders;

import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.common.interfaces.IVoxelPacketEncoder;
import com.thevoxelbox.voxelpacket.common.struct.Coord3D;

/**
 * Encoder for Coord3D struct, just a wrapper on the struct's own serialisation functions
 * 
 * @author Adam Mummery-Smith
 *
 */
public class VoxelPacketEncoderCoord3D implements IVoxelPacketEncoder<Coord3D>
{
	private int dataTypeId;
	
	public VoxelPacketEncoderCoord3D(int dataTypeId)
	{
		this.dataTypeId = dataTypeId;
	}

	@Override
	public int getDataTypeID()
	{
		return this.dataTypeId;
	}

	@Override
	public byte[] encode(Coord3D object)
	{
		return object.getBytes();
	}

	@Override
	public Coord3D decode(ByteBuffer dataBuffer)
	{
		return new Coord3D(dataBuffer);
	}
}
