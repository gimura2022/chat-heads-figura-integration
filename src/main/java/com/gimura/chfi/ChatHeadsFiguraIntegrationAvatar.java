package com.gimura.chfi;

import org.spongepowered.asm.mixin.Unique;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public interface ChatHeadsFiguraIntegrationAvatar {
    @Unique
    boolean chatHeadsFiguraIntegration$renderHeadForPortraitWithOpacity(
        MultiBufferSource.BufferSource buffer,
        PoseStack stack,
        int light,
        float modelScale,
        boolean upsideDown,
        float opacity
    );

    @Unique
    boolean chatHeadsFiguraIntegration$submitPortraitDrawWithOpacity(
        GuiGraphics gui,
        ResourceLocation fallback,
        int x,
        int y,
        int size,
        float modelScale,
        boolean upsideDown,
        float opacity
    );
}
