package electrodynamics.common.recipe.categories.item2item.specificmachines;

import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.categories.item2item.Item2ItemRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class MineralGrinderRecipe extends Item2ItemRecipe {

	public static final String RECIPE_GROUP = "mineral_grinder_recipe";
	public static final String MOD_ID = electrodynamics.api.References.ID;
	public static final ResourceLocation RECIPE_ID = new ResourceLocation(MOD_ID, RECIPE_GROUP);

	public MineralGrinderRecipe(ResourceLocation id, CountableIngredient[] input, ItemStack output, double experience) {
		super(id, input, output, experience);
	}

	public MineralGrinderRecipe(ResourceLocation id, CountableIngredient[] input, ItemStack output, ProbableItem[] itemBiproducts, double experience) {
		super(id, input, output, itemBiproducts, experience);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ElectrodynamicsRecipeInit.MINERAL_GRINDER_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return Registry.RECIPE_TYPE.get(RECIPE_ID);
	}

}
