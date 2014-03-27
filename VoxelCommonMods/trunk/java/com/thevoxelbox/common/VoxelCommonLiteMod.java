package com.thevoxelbox.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import net.minecraft.launchwrapper.Launch;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.core.LiteLoaderFriend;
import com.mumfrey.liteloader.launch.ClassPathUtilities;
import com.mumfrey.liteloader.modconfig.ConfigManager;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.util.log.LiteLoaderLogger;

/**
 * Base class for mods which want to "soft" depend on VoxelCommon. Basically checks if VoxelCommon is present
 * and if not it extracts a temporary jar and injects it onto the classpath dynamically. This allows VoxelCommon
 * mods to be packaged WITHOUT an explicit distribution of VoxelCommon and depend on it dynamically.
 * 
 * @author Adam Mummery-Smith
 */
public abstract class VoxelCommonLiteMod implements LiteMod, Configurable
{
	private static String tempEnvVarName = "TEMP";

	/**
	 * Name of the bundled jar
	 */
	private String bundledJarName = "voxelcommon-2.2.4.jar";

	private final String voxelCommonClassName = "com.thevoxelbox.common.LiteModVoxelCommon"; 
	
	private final String modClassName;
	
	private LiteMod mod;
	
	public VoxelCommonLiteMod(String modClassName)
	{
		this.bundledJarName = LiteLoader.getInstance().getModMetaData(this, "voxelCommonJarName", this.bundledJarName);
		this.modClassName = modClassName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(File configPath)
	{
		try
		{
			Class.forName(this.voxelCommonClassName, false, Launch.class.getClassLoader());
		}
		catch (Throwable th)
		{
			if (!this.extractAndInjectMod("VoxelLib", this.voxelCommonClassName, this.bundledJarName, new File(System.getenv(VoxelCommonLiteMod.tempEnvVarName))))
			{
				// le cry
				return;
			}
		}
		
		try
		{
			// Instance the inner mod via reflection
			Class<LiteMod> modClass = (Class<LiteMod>)Class.forName(this.modClassName);
			
			this.mod = modClass.newInstance();
			this.mod.init(configPath);
			
			if (this.mod instanceof Configurable && ((Configurable)this.mod).getConfigPanelClass() != null)
			{
				this.registerConfigurable();
			}
			
			// Register mod with the events system
			LiteLoader.getEvents().addListener(this.mod);
		}
		catch (Throwable th)
		{
			th.printStackTrace();
		}
	}

	/**
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void registerConfigurable() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Field configManagerField = LiteLoader.class.getDeclaredField("configManager");
		configManagerField.setAccessible(true);
		ConfigManager mgr = (ConfigManager)configManagerField.get(LiteLoader.getInstance());
		mgr.registerMod(this);
	}
	
	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass()
	{
		return this.mod != null && this.mod instanceof Configurable ? ((Configurable)this.mod).getConfigPanelClass() : null;
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath)
	{
		if (this.mod != null)
		{
			this.mod.upgradeSettings(version, configPath, oldConfigPath);
		}
	}
	
	/**
	 * If you can't work out what this method does then you need help
	 * @param jarPath
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean extractAndInjectMod(String libraryName, String className, String resourceName, File libPath)
	{
		final File jarFile = new File(libPath, resourceName);
		if (!jarFile.exists())
		{
			LiteLoaderLogger.info("%s jar does not exist, attempting to extract to %s", libraryName, libPath.getAbsolutePath());
			
			if (!VoxelCommonLiteMod.extractFile("/" + resourceName, jarFile))
			{
				LiteLoaderLogger.warning("%s jar could not be extracted, %s may not function correctly (or at all)", libraryName, this.getName());
				return false;
			}
		}
		
		if (jarFile.exists())
		{
			LiteLoaderLogger.info("%s jar exists, attempting to inject into classpath", libraryName);
			
			try
			{
				ClassPathUtilities.injectIntoClassPath(Launch.classLoader, jarFile.toURI().toURL());
				LiteLoaderFriend.loadMod(libraryName, (Class<? extends LiteMod>)Class.forName(className), jarFile);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return false;
			}
			
			LiteLoaderLogger.info("%s jar was successfully extracted", libraryName);
			return true;
		}

		LiteLoaderLogger.warning("%s jar was not detected, %s may not function correctly (or at all)", libraryName, this.getName());
		
		return false;
	}
	
	/**
	 * Extract a file contained within the litemod to the specified path
	 * @param resourceName
	 * @param outputFile
	 * 
	 * @return
	 */
	private static boolean extractFile(String resourceName, File outputFile)
	{
		try
		{
			final InputStream inputStream = VoxelCommonLiteMod.class.getResourceAsStream(resourceName);
			final OutputSupplier<FileOutputStream> outputSupplier = Files.newOutputStreamSupplier(outputFile);
			ByteStreams.copy(inputStream, outputSupplier);
		}
		catch (NullPointerException ex)
		{
			return false;
		}
		catch (IOException ex)
		{
			return false;
		}
		
		return true;
	}
}
