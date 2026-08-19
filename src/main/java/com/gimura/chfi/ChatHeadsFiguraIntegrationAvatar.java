package com.gimura.chfi;

import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public interface ChatHeadsFiguraIntegrationAvatar {
    @Unique
    boolean chatHeadsFiguraIntegration$submitPortraitDrawWithColor(
        GuiGraphics gui,
        ResourceLocation fallback,
        int x,
        int y,
        int size,
        float modelScale,
        boolean upsideDown,
        int color
    );
}
