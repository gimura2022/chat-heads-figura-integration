package com.gimura.chfi.mixin;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.config.Configs;
import org.figuramc.figura.mixin.gui.GuiGraphicsAccessor;
import org.figuramc.figura.model.rendering.AvatarRenderer;
import org.figuramc.figura.model.rendering.PartFilterScheme;
import org.figuramc.figura.utils.ui.UIHelper;
import org.joml.Matrix3x2fStack;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.gimura.chfi.ChatHeadsFiguraIntegrationAvatar;
import com.gimura.chfi.FiguraPortraitRenderStateWithOpacity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
    public boolean chatHeadsFiguraIntegration$renderHeadForPortraitWithOpacity(
        MultiBufferSource.BufferSource buffer,
        PoseStack stack,
        int light,
        float modelScale,
        boolean upsideDown,
        float opacity
    ) {
        stack.pushPose();
        stack.scale(2, 2, 2); // i have no clue why it's exactly 2x smaller than it should be
        renderer.allowPivotParts = false;

        UIHelper.paperdoll = true;
        UIHelper.dollScale = 16f;

        renderer.setupRenderer(
                PartFilterScheme.PORTRAIT, buffer, stack,
                1f, light, opacity, OverlayTexture.NO_OVERLAY,
                false, false
        );

        int comp = renderer.renderSpecialParts();
        boolean ret = comp > 0 || avatar.headRender(stack, buffer, light, false);

        // after render
        stack.popPose();
        buffer.endBatch();
        UIHelper.paperdoll = false;

        renderer.allowPivotParts = true;

        // return
        return ret;
    }

    @Unique
    @Override
    public boolean chatHeadsFiguraIntegration$submitPortraitDrawWithOpacity(
        GuiGraphics gui,
        ResourceLocation fallback,
        int x,
        int y,
        int size,
        float modelScale,
        boolean upsideDown,
        float opacity
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

        FiguraPortraitRenderStateWithOpacity state = new FiguraPortraitRenderStateWithOpacity(
            avatar,
            fallback,
            modelScale,
            upsideDown,
            x1, y1,
            x2, y2,
            size,
            opacity,
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
