package net.blockboys.underconstruction.block;

import net.blockboys.underconstruction.block.entity.UpgradeChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class UpgradeChestBlock extends Block {

    public UpgradeChestBlock(Properties properties) {
        super(properties);
    }

    // This method is called when the player right-clicks the block
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) { // server side only
            var entity = level.getBlockEntity(pos);
            if (entity instanceof UpgradeChestBlockEntity upgradeChest) {
                // Example action: update the custom string
                upgradeChest.setCustomData("Used by " + player.getName().getString());
            }
        }
        return InteractionResult.SUCCESS;
    }
}
