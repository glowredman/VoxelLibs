package com.thevoxelbox.common;

import com.mumfrey.liteloader.transformers.PacketTransformer;


public class PlayerSpawnTransformer extends PacketTransformer
{
	private static final String LiteModVoxelCommon = "com.thevoxelbox.common.LiteModVoxelCommon";
	private static final String processSpawnPlayer = "processSpawnPlayer";

	// TODO Obfuscation 1.7.2
	private static final String packetSpawnPlayerObf = "fs";
	private static final String packetSpawnPlayer = "net.minecraft.network.play.server.S0CPacketSpawnPlayer";

	public PlayerSpawnTransformer()
	{
		super(packetSpawnPlayer, packetSpawnPlayerObf, LiteModVoxelCommon, processSpawnPlayer);
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
