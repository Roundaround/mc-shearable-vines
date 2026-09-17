package me.roundaround.shearablevines.gametest;

import me.roundaround.allay.api.gametest.ServerGameTest;
import me.roundaround.shearablevines.ShearableVinesMod;
import me.roundaround.trove.gametest.GameTestAssertionException;
import me.roundaround.trove.gametest.ServerTest;
import me.roundaround.trove.gametest.ServerTestContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * A sheared vine never grows. Rather than idle waiting on the random-tick lottery, this delivers
 * the random ticks itself, thousands in one server hop, and checks nothing around the vine
 * changed. An un-sheared vine in the same spot must grow under the same barrage, which proves the
 * setup can grow at all.
 */
@ServerGameTest
public class ShearedVineGrowthTest implements ServerTest {
  private static final BlockPos VINE = new BlockPos(8, 200, 8);
  private static final int RANDOM_TICKS = 5000;

  @Override
  public void runTest(ServerTestContext context) {
    context.onCleanup(() -> context.runOnServer((server) -> clear(server.overworld())));

    int controlTicks = context.computeOnServer((server) -> ticksUntilGrowth(server.overworld(), false));
    if (controlTicks < 0) {
      throw new GameTestAssertionException(
          "the un-sheared control vine never grew in " + RANDOM_TICKS + " random ticks, so the test proves nothing");
    }

    int shearedTicks = context.computeOnServer((server) -> ticksUntilGrowth(server.overworld(), true));
    if (shearedTicks >= 0) {
      throw new GameTestAssertionException("the sheared vine grew after " + shearedTicks + " random ticks");
    }
  }

  /** Random-tick a fresh vine until anything nearby changes; the tick count, or -1 if nothing ever did. */
  private static int ticksUntilGrowth(ServerLevel level, boolean sheared) {
    clear(level);
    level.setBlockAndUpdate(VINE.east(), Blocks.STONE.defaultBlockState());
    level.setBlockAndUpdate(
        VINE,
        Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true).setValue(ShearableVinesMod.SHEARED, sheared)
    );

    List<BlockState> before = snapshot(level);
    for (int i = 1; i <= RANDOM_TICKS; i++) {
      level.getBlockState(VINE).randomTick(level, VINE, level.getRandom());
      if (!snapshot(level).equals(before)) {
        return i;
      }
    }
    return -1;
  }

  private static List<BlockState> snapshot(ServerLevel level) {
    List<BlockState> states = new ArrayList<>();
    for (BlockPos pos : BlockPos.betweenClosed(VINE.offset(-2, -2, -2), VINE.offset(2, 2, 2))) {
      states.add(level.getBlockState(pos));
    }
    return states;
  }

  private static void clear(ServerLevel level) {
    for (BlockPos pos : BlockPos.betweenClosed(VINE.offset(-2, -2, -2), VINE.offset(2, 2, 2))) {
      level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }
  }
}
