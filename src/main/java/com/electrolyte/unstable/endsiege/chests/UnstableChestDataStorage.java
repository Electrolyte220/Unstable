package com.electrolyte.unstable.endsiege.chests;

import com.electrolyte.unstable.UnstableEnums;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public record UnstableChestDataStorage (
        UnstableEnums.CHEST_LOCATION chestLocation,
        List<Map<UnstableEnums.NBT_TYPE, Ingredient>> chestContents) {}