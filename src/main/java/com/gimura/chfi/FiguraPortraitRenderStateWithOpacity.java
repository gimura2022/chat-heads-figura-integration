package com.gimura.chfi;

import org.figuramc.figura.avatar.Avatar;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.pip.PictureInPictureRenderState;
import net.minecraft.resources.ResourceLocation;

public final record FiguraPortraitRenderStateWithOpacity(
    @Nullable Avatar avatar,
    @Nullable ResourceLocation fallbackSkin,
    float modelScale,
    boolean upsideDown,
    int x0,
    int y0,
    int x1,
    int y1,
    float scale,
    float opacity,
    @Nullable ScreenRectangle scissorArea,
    @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {
    public FiguraPortraitRenderStateWithOpacity(
        @Nullable Avatar avatar,
        @Nullable ResourceLocation fallbackSkin,
        float modelScale,
        boolean upsideDown,
        int x0,
        int y0,
        int x1,
        int y1,
        float scale,
        float opacity,
        @Nullable ScreenRectangle screenRectangle
    ) {
        this(
            avatar,
            fallbackSkin,
            modelScale,
            upsideDown,
            x0, y0,
            x1, y1,
            scale,
            opacity,
            screenRectangle,
            PictureInPictureRenderState.getBounds(x0, y0, x1, y1, screenRectangle)
        );
    }
}
