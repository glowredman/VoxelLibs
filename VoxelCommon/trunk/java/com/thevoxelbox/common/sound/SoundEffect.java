package com.thevoxelbox.common.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

public class SoundEffect extends PositionedSound
{
    public SoundEffect(ResourceLocation soundLocation, float volume, float pitch)
    {
        super(soundLocation);
		this.field_147662_b = volume;
		this.field_147663_c = pitch;
		this.field_147660_d = 0.0F;
		this.field_147661_e = 0.0F;
		this.field_147658_f = 0.0F;
		this.field_147659_g = false;
		this.field_147665_h = 0;
		this.field_147666_i = ISound.AttenuationType.NONE;
    }
}
