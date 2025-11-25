package net.blockboys.under_construction.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blockboys.under_construction.structure.TowerStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.List;

public class TowerMaterialsOverlay {

    public static final IGuiOverlay HUD_MATERIALS_NEEDED = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        // texture configurations
        int textureWidth = 32, textureHeight = 32;
        int scaleWidth = 32, scaleHeight = 32;

        // gui configurations
        // reference point is item block texture!
        int x = ((screenWidth - scaleWidth) / 2) - 400;
        int y = screenHeight - 420;
        int textOffset = 20;

        if (!ClientTowerData.checkStructureMaxed()) {
            int structureLevel = ClientTowerData.getStructureLevel();
            List<Item> upgradeItems = TowerStructure.getItemsList(structureLevel);
            List<Integer> upgradeAmounts = ClientTowerData.getUpgradeAmounts(structureLevel);

            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    "Required Tower Materials:",
                    x, y - 15, 0xFFFF00
            );

            for (int i = 0; i < upgradeAmounts.size(); i++) {
                int required = upgradeAmounts.get(i);
                if (required > 0) {
                    ResourceLocation ASSET = TowerStructure.getMaterialAsset(upgradeItems.get(i).asItem());
                    RenderSystem.setShaderTexture(0, ASSET);

                    // draw texture and text
                    guiGraphics.blit(ASSET,
                            x, y,
                            0, 0,
                            scaleWidth, scaleHeight,
                            textureWidth, textureHeight
                    );
                    guiGraphics.drawString(
                            Minecraft.getInstance().font,
                            "x" + required,
                            x + textOffset, y + textOffset, 0xFFFFFF
                    );
                    y += 40;
                }
            }
        } else {
            RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    "You have completed the Tower!",
                    x, y - 15, 0xFFFF00
            );
        }
    });
}
