package com.shift.minestuckuniverse.inventory.modus;

import com.shift.minestuckuniverse.entity.MSUEntities;
import com.shift.minestuckuniverse.entity.entities.EntityCruxiteSlime;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SlimeModus extends ArrayModus {
    public SlimeModus(ModusType<? extends SlimeModus> type, LogicalSide side) {
        super(type, side);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SlimeModus.class);

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard)
    {
        if(id == CaptchaDeckHandler.EMPTY_CARD)
        {
            if(list.size() < size)
            {
                size--;
                markDirty();
                return new ItemStack(MSItems.CAPTCHA_CARD.get());
            } else return ItemStack.EMPTY;
        }

        if(list.isEmpty())
            return ItemStack.EMPTY;

        if(id == CaptchaDeckHandler.EMPTY_SYLLADEX)
        {
            for(ItemStack item : list)
                createSlime(player, item);
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if(id < 0 || id >= list.size())
            return ItemStack.EMPTY;

        ItemStack item = list.remove(id);
        markDirty();

        if(asCard)
        {
            size--;
            markDirty();
            item = CaptchaCardItem.createCardWithItem(item, player.server);
        }
        if(side == LogicalSide.SERVER) {
            createSlime(player, item);
        }
        return ItemStack.EMPTY;
    }

    public static void createSlime(ServerPlayer source, ItemStack stack)
    {
        if (source.level().isClientSide()) return;
        ServerLevel world = source.serverLevel();
        EntityCruxiteSlime slime = new EntityCruxiteSlime(MSUEntities.CRUXITE_SLIME.get(), world);
        slime.moveTo(source.getX(), source.getY() + 1.0, source.getZ(), source.getYRot(), source.getXRot());
        slime.finalizeSpawn(
                world,
                world.getCurrentDifficultyAt(slime.blockPosition()),
                MobSpawnType.COMMAND,
                null
        );
        slime.setSize(1, true);

        int color = ColorHandler.getColorForPlayer(source);
        slime.setSlimeColor(color == -1 ? 0x99D9EA : color);

        //Delta Movement
        Random random = new Random();
        double speed = 0.5;

        double angle = random.nextDouble() * 2 * Math.PI;

        double vx = Math.cos(angle) * speed;
        double vz = Math.sin(angle) * speed;
        double vy = 0.2;

        slime.setDeltaMovement(vx, vy, vz);

        LOGGER.debug("SlimeModus: Attempting to create Cruxite Slime with: {}", stack);
        slime.setStoredItem(stack);
        LOGGER.debug("SlimeModus: Slime created, checking storedItem: {}", slime.getStoredItem());
        world.addFreshEntity(slime);
    }
}
