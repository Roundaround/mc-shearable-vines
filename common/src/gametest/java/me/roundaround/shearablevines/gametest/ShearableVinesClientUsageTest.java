package me.roundaround.shearablevines.gametest;

import me.roundaround.allay.api.gametest.ClientGameTest;
import me.roundaround.shearablevines.ShearableVinesMod;
import me.roundaround.trove.gametest.ClientTest;
import me.roundaround.trove.gametest.ClientTestContext;
import me.roundaround.trove.gametest.ClientWorld;
import me.roundaround.trove.gametest.GameTestAssertionException;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Boot a world, hang a vine on a solid block, and shear it — the mod's core feature.
 * Asserts the mod-added {@code sheared} block-state property flips from false to true,
 * which is what stops the vine from random-tick growing.
 */
@ClientGameTest
public class ShearableVinesClientUsageTest implements ClientTest {
  @Override
  public void runTest(ClientTestContext context) {
    BlockPos anchor = new BlockPos(2, 65, 0);
    BlockPos vine = new BlockPos(1, 65, 0);

    try (ClientWorld world = context.worldBuilder().creative().stopTime(true).create()) {
      world.setBlock(anchor, "minecraft:stone");
      world.setBlock(vine, "minecraft:vine[east=true]");
      world.teleport(1.5, 65.0, -3.5);
      context.waitTicks(2);

      BlockState before = world.getBlockState(vine);
      if (before.getValue(ShearableVinesMod.SHEARED)) {
        throw new GameTestAssertionException("fresh vine should start un-sheared");
      }

      world.setMainHandItem("minecraft:shears");
      world.useItemOn(vine);
      context.waitTicks(2);

      BlockState after = world.getBlockState(vine);
      if (!after.getValue(ShearableVinesMod.SHEARED)) {
        throw new GameTestAssertionException("shearing the vine did not set sheared=true");
      }
    }
  }
}
