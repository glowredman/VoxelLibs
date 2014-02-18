package com.thevoxelbox.common;

import com.mumfrey.liteloader.transformers.PacketTransformer;

public class TimeUpdateTransformer extends PacketTransformer
{
	private static final String LiteModVoxelCommon = "com.thevoxelbox.common.LiteModVoxelCommon";
	private static final String handleTimeUpdate = "handleTimeUpdate";

	// TODO Obfuscation 1.7.2
	private static final String packetTimeUpdateObf = "hu";
	private static final String packetTimeUpdate = "net.minecraft.network.play.server.S03PacketTimeUpdate";

	public TimeUpdateTransformer()
	{
		super(packetTimeUpdate, packetTimeUpdateObf, LiteModVoxelCommon, handleTimeUpdate);
	}

	@Override
	protected void notifyInjectionFailed()
	{
	}
	
	@Override
	protected void notifyInjected()
	{
	}
	
	public static boolean isInjected()
	{
		return true;
	}
}
