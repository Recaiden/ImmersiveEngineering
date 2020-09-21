/*
 * BluSunrize
 * Copyright (c) 2020
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.util.compat.jei.sawmill;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.crafting.SawmillRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlocks;
import blusunrize.immersiveengineering.common.util.ListUtils;
import blusunrize.immersiveengineering.common.util.compat.jei.IERecipeCategory;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIHelper;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIIngredientStackListBuilder;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class SawmillRecipeCategory extends IERecipeCategory<SawmillRecipe>
{
	public static final ResourceLocation UID = new ResourceLocation(Lib.MODID, "sawmill");
	private final IDrawableAnimated arrow;
	private final IDrawableAnimated arrow_split;

	public SawmillRecipeCategory(IGuiHelper helper)
	{
		super(SawmillRecipe.class, helper, UID, "block.immersiveengineering.sawmill");
		setBackground(helper.drawableBuilder(
				JEIHelper.JEI_GUI, 0, 0, 104, 26).setTextureSize(128, 128).addPadding(2, 36, 3, 13).build()
		);
		IDrawableStatic arrowStatic = helper.drawableBuilder(JEIHelper.JEI_GUI, 0, 26, 46, 16).setTextureSize(128, 128).build();
		this.arrow = helper.createAnimatedDrawable(arrowStatic, 80, IDrawableAnimated.StartDirection.LEFT, false);
		arrowStatic = helper.drawableBuilder(JEIHelper.JEI_GUI, 46, 26, 46, 16).setTextureSize(128, 128).build();
		this.arrow_split = helper.createAnimatedDrawable(arrowStatic, 80, IDrawableAnimated.StartDirection.LEFT, false);
		setIcon(new ItemStack(IEBlocks.Multiblocks.sawmill));
	}

	@Override
	public void setIngredients(SawmillRecipe recipe, IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM, JEIIngredientStackListBuilder.make(recipe.input).build());
		NonNullList<ItemStack> l = ListUtils.fromItems(recipe.output);
		if(!recipe.stripped.isEmpty())
			l.add(recipe.stripped);
		l.addAll(recipe.secondaryStripping);
		l.addAll(recipe.secondaryOutputs);
		ingredients.setOutputs(VanillaTypes.ITEM, l);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, SawmillRecipe recipe, IIngredients ingredients)
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 3, 6);
		guiItemStacks.set(0, Arrays.asList(recipe.input.getMatchingStacks()));

		int slot = 1;
		if(!recipe.stripped.isEmpty())
		{
			guiItemStacks.init(slot, false, 40, 6);
			guiItemStacks.set(slot, recipe.stripped);
			guiItemStacks.setBackground(slot++, JEIHelper.slotDrawable);
		}

		guiItemStacks.init(slot, false, 85, 6);
		guiItemStacks.set(slot++, recipe.output);

		int i = 0;
		for(ItemStack out : recipe.secondaryStripping)
		{
			guiItemStacks.init(slot, false, 40+i%2*18, 28+i/2*18);
			guiItemStacks.set(slot, out);
			guiItemStacks.setBackground(slot++, JEIHelper.slotDrawable);
			i++;
		}

		i = 0;
		for(ItemStack out : recipe.secondaryOutputs)
		{
			guiItemStacks.init(slot, false, 81+i%2*18, 28+i/2*18);
			guiItemStacks.set(slot, out);
			guiItemStacks.setBackground(slot++, JEIHelper.slotDrawable);
			i++;
		}
	}

	@Override
	public void draw(SawmillRecipe recipe, double mouseX, double mouseY)
	{
		if(recipe.stripped.isEmpty())
			this.arrow.draw(28, 6);
		else
			this.arrow_split.draw(28, 6);
	}

}