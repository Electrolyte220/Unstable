package electrolyte.unstable.patchouli;

import com.mojang.blaze3d.systems.RenderSystem;
import electrolyte.unstable.datastorage.endsiege.ChestDataStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public class ChestComponent implements ICustomComponent {

    private transient String chestLocation;

    @Override
    public void build(int componentX, int componentY, int pageNum) {}

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        AtomicInteger xPos = new AtomicInteger();
        AtomicInteger yPos = new AtomicInteger();
        AtomicInteger count = new AtomicInteger();
        xPos.set(2);
        yPos.set(14);
        count.getAndIncrement();
        ChestDataStorage.getMasterStorage().forEach(dataStorage -> {
            if(dataStorage.chestLocation().toString().equals(chestLocation.toUpperCase())) {
                dataStorage.chestContents().forEach(ingredient -> {
                    RenderSystem.enableBlend();
                    RenderSystem.setShaderColor(1F, 1F,1F, 1F);
                    RenderSystem.setShaderTexture(0, new ResourceLocation(PatchouliAPI.MOD_ID, "textures/gui/crafting.png"));
                    graphics.blit(new ResourceLocation(PatchouliAPI.MOD_ID, "textures/gui/crafting.png"), xPos.get() - 5, yPos.get() - 5, 20, 102, 26, 26, 128, 256);
                    context.renderIngredient(graphics, xPos.get(), yPos.get(), mouseX, mouseY, ingredient);
                    xPos.addAndGet(24);
                    if (count.get() % 5 == 0) {
                        xPos.set(2);
                        yPos.addAndGet(24);
                    }
                    count.getAndIncrement();
                });
            }
        });
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        chestLocation = lookup.apply(IVariable.wrap("#chest_location#")).asString();
    }
}
