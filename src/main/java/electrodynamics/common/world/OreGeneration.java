package electrodynamics.common.world;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import electrodynamics.DeferredRegisters;
import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.settings.OreConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = References.ID, bus = Bus.FORGE)
public class OreGeneration {
	private static final HashSet<PlacedFeature> FEATURES = new HashSet<>();

	public static void registerOres() { // called in onCommonSetup(FMLCommonSetupEvent event)
		for (SubtypeOre ore : SubtypeOre.values()) {
			if (OreConfig.oresToSpawn.contains(ore.name())) {
				BlockState oreDefault = DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(ore).defaultBlockState();
				BlockState oreDeepslateDefault = DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeOreDeepslate.values()[ore.ordinal()]).defaultBlockState();
				List<TargetBlockState> targetBlockStates = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, oreDefault), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, oreDeepslateDefault));
				ConfiguredFeature<?, ?> feature = FeatureUtils.register(ore.tag(), Feature.ORE.configured(new OreConfiguration(targetBlockStates, ore.veinSize)));
				PlacedFeature placed = PlacementUtils.register(ore.tag(), feature.placed(List.of(CountPlacement.of((int) (ore.veinsPerChunk * OreConfig.OREGENERATIONMULTIPLIER)), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(ore.minY), VerticalAnchor.absolute(ore.maxY)), BiomeFilter.biome())));
				FEATURES.add(placed);
			}
		}
	}

	@SubscribeEvent
	public static void gen(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder gen = event.getGeneration();
		if (event.getCategory() != BiomeCategory.NETHER && event.getCategory() != BiomeCategory.THEEND) {
			for (PlacedFeature feature : FEATURES) {
				gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);
			}
		}
	}

	public static void generateSulfurAround(Random rand, BlockPos pos, WorldGenLevel level) {
		for (Direction direction : Direction.values()) {
			BlockPos offset = pos.offset(direction.getNormal());
			if (rand.nextFloat() < 0.3) {
				if (level.getBlockState(offset).getBlock() == Blocks.STONE) {
					level.setBlock(offset, DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeOre.sulfur).defaultBlockState(), 3);
				}
				if (level.getBlockState(offset).getBlock() == Blocks.DEEPSLATE) {
					level.setBlock(offset, DeferredRegisters.SUBTYPEBLOCK_MAPPINGS.get(SubtypeOreDeepslate.sulfur).defaultBlockState(), 3);
				}
			}
		}
	}
}
