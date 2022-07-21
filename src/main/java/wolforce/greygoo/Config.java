package wolforce.greygoo;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Config {

	private static Config instance;

	public final DoubleValue spreadChance;
	public final DoubleValue playerSpreadChance;
	public final IntValue spreadDistance;

	public final BooleanValue needsSky;
	public final BooleanValue makesPillars;
	public final IntValue pillarDistance;

	public static void init() {
		Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
		instance = specPair.getLeft();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight(), "greygoo.toml");
	}

	Config(ForgeConfigSpec.Builder builder) {
		spreadChance = builder.comment("Chance that a goo block spreads on random tick.").defineInRange("spreadChance", 1.0, 0, 1);
		playerSpreadChance = builder.comment("Chance that a player creates goo underneath him.").defineInRange("playerSpreadChance", 0.01, 0, 1);
		spreadDistance = builder.comment("Number of blocks that a spread changes.").defineInRange("spreadDistance", 10, 1, Integer.MAX_VALUE);

		needsSky = builder.comment("If goo needs to see the sky to disapear.").define("needsSky", true);
		makesPillars = builder.comment("If goo makes pillars.").define("makesPillars", true);
		pillarDistance = builder.comment("How far appart are the pillars.").defineInRange("pillarDistance", 17, 17, Integer.MAX_VALUE);
	}

	//
	//
	//

	public static float getSpreadChance() {
		return instance.playerSpreadChance.get().floatValue();
	}

	public static float getPlayerSpreadChance() {
		return instance.playerSpreadChance.get().floatValue();
	}

	public static boolean needsSky() {
		return instance.needsSky.get();
	}

	public static boolean makesPillars() {
		return instance.makesPillars.get();
	}

	public static int pillarDistance() {
		return instance.pillarDistance.get();
	}
}
