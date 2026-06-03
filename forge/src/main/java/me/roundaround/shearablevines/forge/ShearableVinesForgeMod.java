package me.roundaround.shearablevines.forge;

import me.roundaround.trove.forge.TroveForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("shearablevines")
public final class ShearableVinesForgeMod {
  public ShearableVinesForgeMod(FMLJavaModLoadingContext context) {
    TroveForge.bootstrap(context);
    // Mixin-only mod: nothing else to register.
  }
}
