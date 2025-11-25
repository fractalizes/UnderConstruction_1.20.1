package net.blockboys.under_construction.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blockboys.under_construction.UnderConstruction;
import net.blockboys.under_construction.structure.TowerStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class TowerMaterialsOverlay {
    private static final ResourceLocation COBBLESTONE_ASSET = ResourceLocation.fromNamespaceAndPath(
            UnderConstruction.MOD_ID, "textures/icons/cobblestone_32.png");

    public static final IGuiOverlay HUD_MATERIALS_NEEDED = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        // texture configurations
        int textureWidth = 32, textureHeight = 32;
        int scaleWidth = 32, scaleHeight = 32;

        // gui configurations
        // reference point is item block texture!
        int x = ((screenWidth - scaleWidth) / 2) - 190;
        int y = screenHeight - 215;

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                "Required Tower Materials:",
                x, y - 15, 0xFFFF00
        );

        RenderSystem.setShaderTexture(0, COBBLESTONE_ASSET);
        guiGraphics.blit(COBBLESTONE_ASSET,
                x, y,
                0, 0,
                scaleWidth, scaleHeight,
                textureWidth, textureHeight
        );
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                "x32",
                x + 20, y + 20, 0xFFFFFF
        );
    });
}
