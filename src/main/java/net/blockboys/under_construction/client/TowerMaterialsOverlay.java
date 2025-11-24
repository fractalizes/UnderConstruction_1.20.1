package net.blockboys.under_construction.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class TowerMaterialsOverlay {
    // TODO asset not loading(?), showing on screen only as black square
    private static final ResourceLocation COBBLESTONE_ASSET = ResourceLocation.fromNamespaceAndPath(
            UnderConstruction.MOD_ID, "cobblestone.png");

    public static final IGuiOverlay HUD_MATERIALS_NEEDED = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;

        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, COBBLESTONE_ASSET);
        guiGraphics.blit(COBBLESTONE_ASSET,
                x - 94, screenHeight - 54,
                0, 0,
                12, 12);
    });

}
