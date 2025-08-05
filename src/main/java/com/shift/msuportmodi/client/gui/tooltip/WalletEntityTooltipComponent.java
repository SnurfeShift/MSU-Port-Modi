package com.shift.msuportmodi.client.gui.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class WalletEntityTooltipComponent implements ClientTooltipComponent {
    private final CompoundTag tag;

    public WalletEntityTooltipComponent(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public int getWidth(Font font) {
        return 50;
    }

    @Override
    public void renderImage(Font font, int pX, int pY, GuiGraphics guiGraphics) {
        // Data
        String id = this.tag.getString("id");
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(id));
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null)
            return;
        // Entity
        LivingEntity entity = (LivingEntity) type.create(level);
        if (entity == null)
            return;

        entity.setOnGround(true);
        entity.load(this.tag);

        // Quaternionf
        double rotation = System.currentTimeMillis() / 25.0D % 360.0D;
        Quaternionf pose = new Quaternionf().rotateZ((float)Math.PI).rotateY((float)Math.toRadians(rotation));
        Vector3f cameraOffset = new Vector3f(0, 0, 0);

        // Scale
        float height = entity.getBbHeight();
        float scale;
        if (height > 2.5F) {
            scale = 40.0F / height;
        } else if (height < 1.0F) {
            scale = 20.0F * (1.0F / height);
        } else {
            scale = 20.0F;
        }

        // Scissors and locations.
        int centerX = pX + getWidth(font) / 2;
        int posY = pY + 43;

        guiGraphics.enableScissor(pX, pY - 33, pX + getWidth(font), pY + getHeight());
        InventoryScreen.renderEntityInInventory(guiGraphics, centerX, posY, scale, cameraOffset, pose, null, entity);
        guiGraphics.disableScissor();
    }
}
