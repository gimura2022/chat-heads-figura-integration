package com.gimura.chfi.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.gimura.chfi.FiguraPortraitRendererWithColor;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

@Mixin(GameRenderer.class)
public final class GameRendererMixin {
    @ModifyArg(
        method = "<init>",
        index = 2,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/render/GuiRenderer;<init>(Lnet/minecraft/client/gui/render/state/GuiRenderState;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Ljava/util/List;)V"
        )
    )
    private List<PictureInPictureRenderer<?>> addPortraitRendererWithColor(List<PictureInPictureRenderer<?>> list, @Local MultiBufferSource.BufferSource source) {
        List<PictureInPictureRenderer<?>> newList = new ArrayList<>(list);

        newList.add(new FiguraPortraitRendererWithColor(source));

        return newList;
    }
}
