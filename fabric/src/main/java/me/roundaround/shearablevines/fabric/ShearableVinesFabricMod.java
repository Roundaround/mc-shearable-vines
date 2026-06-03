package me.roundaround.shearablevines.fabric;

import me.roundaround.allay.api.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint(Entrypoint.MAIN)
public final class ShearableVinesFabricMod implements ModInitializer {
  @Override
  public void onInitialize() {
    // Mixin-only mod: the SHEARED blockstate property is wired entirely via mixins.
    // Trove's Fabric core self-bootstraps, so there is nothing to register here.
  }
}
