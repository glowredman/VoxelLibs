package com.thevoxelbox.voxelpacket.common.struct;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.thevoxelbox.voxelpacket.common.data.Marshal;

/**
 * Struct for sending arbitrary 3d object positions
 * 
 * @author Adam Mummery-Smith
 *
 */
public class Coord3D implements Serializable
{
	/**
	 * Serial version UID 
	 */
	private static final long serialVersionUID = 1331074709877612503L;
	
	public double X, Y, Z;
	
	/**
	 * Private ctor for Serializable interface 
	 */
	@SuppressWarnings("unused")
	private Coord3D()
	{
	}

	/**
	 * Create a new Pos3D with the specified components
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Coord3D(double x, double y, double z)
	{
		this.X = x;
		this.Y = y;
		this.Z = x;
	}
	
	/**
	 * Create a new Pos3D from the specified data
	 * 
	 * @param dataBuffer
	 */
	public Coord3D(ByteBuffer dataBuffer)
	{
		try
		{
			this.X = Double.longBitsToDouble(dataBuffer.getLong());
			this.Y = Double.longBitsToDouble(dataBuffer.getLong());
			this.Z = Double.longBitsToDouble(dataBuffer.getLong());
		} catch (Exception ex) {}
	}
	
	/**
	 * Get bytes representing this struct
	 * 
	 * @return
	 */
	public byte[] getBytes()
	{
		try
		{
			long x = Double.doubleToLongBits(this.X);
			long y = Double.doubleToLongBits(this.Y);
			long z = Double.doubleToLongBits(this.Z);
	
			byte[] xBytes = Marshal.unpackByte(x);
			byte[] yBytes = Marshal.unpackByte(y);
			byte[] zBytes = Marshal.unpackByte(z);
			
			byte[] result = new byte[] {
				xBytes[0], xBytes[1], xBytes[2], xBytes[3], xBytes[4], xBytes[5], xBytes[6], xBytes[7], 
				yBytes[0], yBytes[1], yBytes[2], yBytes[3], yBytes[4], yBytes[5], yBytes[6], yBytes[7], 
				zBytes[0], zBytes[1], zBytes[2], zBytes[3], zBytes[4], zBytes[5], zBytes[6], zBytes[7] 
			};
			
			return result;
		}
		catch (Exception ex) { return new byte[0]; }
	}
}
