package net.blockboys.under_construction.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class TowerMaterialsOverlay {

//    private static final ResourceLocation COBBLESTONE_ASSET = ResourceLocation.withDefaultNamespace(
//            "textures/blocks/cobblestone.png");
    private static final ResourceLocation COBBLESTONE_ASSET = ResourceLocation.fromNamespaceAndPath(
            UnderConstruction.MOD_ID, "textures/icons/cobblestone.png");

    public static final IGuiOverlay HUD_MATERIALS_NEEDED = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        int textureWidth = 64, textureHeight = 64;
        int scaleWidth = 64, scaleHeight = 64;

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, COBBLESTONE_ASSET);
        guiGraphics.blit(COBBLESTONE_ASSET,
                (screenWidth - scaleWidth) / 2, screenHeight - 100,
                0, 0,
                scaleWidth, scaleHeight,
                textureWidth, textureHeight
        );
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                "hello world!",
                (screenWidth - scaleWidth) / 2,
                screenHeight - 100,
                0xFFFFFF
        );
    });
}
