package com.mumfrey.liteloader.core;

import java.io.File;

import com.mumfrey.liteloader.LiteMod;

public abstract class LiteLoaderFriend
{
	public static void loadMod(String identifier, Class<? extends LiteMod> mod, File jarFile) throws InstantiationException, IllegalAccessException
	{
		LiteLoader.getInstance().loadMod(identifier, mod, new LoadableModFile(jarFile, null));
	}
}
