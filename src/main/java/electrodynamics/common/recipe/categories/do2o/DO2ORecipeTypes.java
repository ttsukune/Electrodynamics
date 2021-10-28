package electrodynamics.common.recipe.categories.do2o;

import electrodynamics.common.recipe.categories.do2o.specificmachines.EnergizedAlloyerRecipe;
import electrodynamics.common.recipe.categories.do2o.specificmachines.OxidationFurnaceRecipe;
import electrodynamics.common.recipe.categories.do2o.specificmachines.ReinforcedAlloyerRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class DO2ORecipeTypes {

    public static final RecipeSerializer<OxidationFurnaceRecipe> OXIDATION_FURNACE_JSON_SERIALIZER = new DO2ORecipeSerializer<>(
	    OxidationFurnaceRecipe.class);

    public static final RecipeSerializer<EnergizedAlloyerRecipe> ENERGIZED_ALLOYER_JSON_SERIALIZER = new DO2ORecipeSerializer<>(
	    EnergizedAlloyerRecipe.class);

    public static final RecipeSerializer<ReinforcedAlloyerRecipe> REINFORCED_ALLOYER_JSON_SERIALIZER = new DO2ORecipeSerializer<>(
	    ReinforcedAlloyerRecipe.class);
}
