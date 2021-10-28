package electrodynamics.common.recipe.categories.o2o.specificmachines;

import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.categories.o2o.O2ORecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MineralCrusherRecipe extends O2ORecipe/* <MineralCrusherRecipe> */ {

    public static final String RECIPE_GROUP = "mineral_crusher_recipe";
    public static final String MOD_ID = electrodynamics.api.References.ID;
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(MOD_ID, RECIPE_GROUP);

    public MineralCrusherRecipe(ResourceLocation id, CountableIngredient input, ItemStack output) {
	super(id, input, output);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
	return ElectrodynamicsRecipeInit.MINERAL_CRUSHER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
	return Registry.RECIPE_TYPE.get(RECIPE_ID);
    }

}
