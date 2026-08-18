package com.gimura.chfi.mixin;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.gimura.chfi.ChatHeadsFiguraIntegrationAvatar;

import dzwdz.chat_heads.ChatHeads;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;

@Mixin(ChatHeads.class)
public final class ChatHeadsMixin {
    private static final int PORTRAIT_SIZE = 8;

    @Inject(
        method = "renderChatHead(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/client/multiplayer/PlayerInfo;FZ)V",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private static void renderChatHead(
        GuiGraphics graphics,
        int x,
        int y,
        PlayerInfo owner,
        float opacity,
        boolean shadow,
        CallbackInfo callbackInfo
    ) {
        Avatar avatar = AvatarManager.getAvatarForPlayer(owner.getProfile().getId());

        if (avatar == null)
            return;

        Player player = Minecraft.getInstance().level.getPlayerByUUID(owner.getProfile().getId());

        if (player == null)
            return;

        ((ChatHeadsFiguraIntegrationAvatar) avatar).chatHeadsFiguraIntegration$submitPortraitDrawWithOpacity(
            graphics,
            null,
            x, y,
            PORTRAIT_SIZE,
            16f,
            LivingEntityRenderer.isEntityUpsideDown(player),
            opacity
        );

        callbackInfo.cancel();
    }
}
