package electrolyte.unstable.datastorage.reversinghoe;

import electrolyte.unstable.UnstableEnums;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

public class TransmutationDataStorage {

    private final UnstableEnums.TRANSMUTATION_INPUT inputType;
    private ResourceLocation location;
    private Ingredient input;
    private Block output;

    private static final ArrayList<TransmutationDataStorage> MASTER_STORAGE = new ArrayList<>();

    public TransmutationDataStorage(Block input, Block output) {
        this.inputType = UnstableEnums.TRANSMUTATION_INPUT.BLOCK;
        this.location = input.builtInRegistryHolder().key().location();
        this.input = Ingredient.of(input);
        this.output = output;
    }

    public TransmutationDataStorage(ResourceLocation location, Block output) {
        this.inputType = UnstableEnums.TRANSMUTATION_INPUT.TAG;
        this.location = location;
        this.input = Ingredient.of();
        this.output = output;
    }

    public static ArrayList<TransmutationDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }

    public UnstableEnums.TRANSMUTATION_INPUT getInputType() {
        return inputType;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public Ingredient getInput() {
        return input;
    }

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public Block getOutput() {
        return output;
    }
}
