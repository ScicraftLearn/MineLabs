package be.minelabs.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class MinelabsGameRules {

    public static final GameRules.Key<GameRules.BooleanRule> BOHR_PROJECTILES = GameRuleRegistry.register(
            "bohrProjectiles", GameRules.Category.SPAWNING, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_CHEMICAL_PROJECTILES = GameRuleRegistry.register(
            "allowChemicalProjectiles", GameRules.Category.SPAWNING, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_DECAY = GameRuleRegistry.register(
            "allowDecay", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.IntRule> E_RADIUS = GameRuleRegistry.register("eRadius",
            GameRules.Category.UPDATES, GameRuleFactory.createIntRule(12));

    public static final GameRules.Key<GameRules.BooleanRule> CHARGED_DROPS_ON_BLOCKHIT = GameRuleRegistry.register(
            "chargedDropsOnBlockhit", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static void onInitialize() {
    }
}
