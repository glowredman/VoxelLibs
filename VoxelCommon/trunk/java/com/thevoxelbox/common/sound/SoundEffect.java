package com.thevoxelbox.common.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

public class SoundEffect extends PositionedSound
{
    public SoundEffect(ResourceLocation soundLocation, float volume, float pitch)
    {
        super(soundLocation);
		this.volume = volume;
		this.field_147663_c = pitch;
		this.xPosF = 0.0F;
		this.yPosF = 0.0F;
		this.zPosF = 0.0F;
		this.repeat = false;
		this.field_147665_h = 0;
		this.field_147666_i = ISound.AttenuationType.NONE;
    }
}
