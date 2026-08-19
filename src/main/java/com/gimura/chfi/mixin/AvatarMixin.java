package com.gimura.chfi.mixin;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.config.Configs;
import org.figuramc.figura.mixin.gui.GuiGraphicsAccessor;
import org.figuramc.figura.model.rendering.AvatarRenderer;
import org.joml.Matrix3x2fStack;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.gimura.chfi.ChatHeadsFiguraIntegrationAvatar;
import com.gimura.chfi.FiguraPortraitRenderStateWithColor;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@Mixin(Avatar.class)
public final class AvatarMixin implements ChatHeadsFiguraIntegrationAvatar {
    @Shadow
    public AvatarRenderer renderer;

    @Shadow
    public boolean loaded;

    @Unique
    private Avatar avatar = (Avatar) (Object) this;

    @Unique
    @Override
    public boolean chatHeadsFiguraIntegration$submitPortraitDrawWithColor(
        GuiGraphics gui,
        ResourceLocation fallback,
        int x,
        int y,
        int size,
        float modelScale,
        boolean upsideDown,
        int color
    ) {
        if (!Configs.AVATAR_PORTRAIT.value || renderer == null || !loaded)
            return false;

        // matrices
        Matrix3x2fStack pose = gui.pose();
        pose.pushMatrix();
        pose.translate(x, y);
        //pose.scale(modelScale, modelScale * (upsideDown ? 1 : -1));
        pose.rotate(180f * (float) (Math.PI / 180.0));

        // scissors
        Vector2f pos = pose.transformPosition(new Vector2f());

        int x1 = (int) pos.x;
        int y1 = (int) pos.y;
        int x2 = (int) pos.x + size;
        int y2 = (int) pos.y + size;

        gui.pose().pushMatrix();
        gui.pose().identity();
        gui.enableScissor(x1, y1, x2, y2);
        gui.pose().popMatrix();

        // setup render
        pose.translate((float)(4d / 16d), (float) (upsideDown ? 0 : (8d / 16d)));

        FiguraPortraitRenderStateWithColor state = new FiguraPortraitRenderStateWithColor(
            avatar,
            fallback,
            modelScale,
            upsideDown,
            x1, y1,
            x2, y2,
            size,
            color,
            ((GuiGraphicsAccessor) gui).figura$getScissorStack().peek()
        );
        gui.fill(x1, y1, x2, y2, -1);
        ((GuiGraphicsAccessor) gui).figura$getRenderState().submitPicturesInPictureState(state);
        gui.pose().popMatrix();

        gui.disableScissor();

        // return
        return true;
    }
}
