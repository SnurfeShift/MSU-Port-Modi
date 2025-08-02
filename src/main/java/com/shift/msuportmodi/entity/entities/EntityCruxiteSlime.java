package com.shift.msuportmodi.entity.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public class EntityCruxiteSlime extends Slime {
    private static final EntityDataAccessor<Integer> SLIME_COLOR = SynchedEntityData.defineId(EntityCruxiteSlime.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> STORED_ITEM = SynchedEntityData.defineId(EntityCruxiteSlime.class, EntityDataSerializers.ITEM_STACK);
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityCruxiteSlime.class);

    public EntityCruxiteSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new EntityCruxiteSlime.SlimeMoveControl(this);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLIME_COLOR, 0);
        builder.define(STORED_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (!getStoredItem().is(Items.AIR)) tag.put("storedItem", getStoredItem().save(level().registryAccess()));
        tag.putInt("slimeColor", getColor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if(tag.contains("storedItem", 10)) {
            CompoundTag item = tag.getCompound("storedItem");
            ItemStack stack = ItemStack.parseOptional(level().registryAccess(), item);
            entityData.set(STORED_ITEM, stack);
        }

        entityData.set(SLIME_COLOR, tag.getInt("slimeColor"));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityCruxiteSlime.SlimeFloatGoal(this));
        this.goalSelector.addGoal(3, new EntityCruxiteSlime.SlimeRandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new EntityCruxiteSlime.SlimeKeepOnJumpingGoal(this));
    }

    public ItemStack getStoredItem() {
        return entityData.get(STORED_ITEM);
    }

    public void setStoredItem(ItemStack v) {
        entityData.set(STORED_ITEM, v);
    }

    public int getColor() {
        //int color = entityData.get(SLIME_COLOR);
        //LOGGER.debug("EntityCruxiteSlime: getColor() called, returning 0x{}", Integer.toHexString(color).toUpperCase());
        return entityData.get(SLIME_COLOR);
    }

    public void setSlimeColor(int v) {
        //LOGGER.debug("EntityCruxiteSlime: setColor() called, returning 0x{}", Integer.toHexString(v).toUpperCase());
        entityData.set(SLIME_COLOR, v);
        //int color = entityData.get(SLIME_COLOR);
        //LOGGER.debug("EntityCruxiteSlime: checking SLIME_COLOR, returning 0x{}", Integer.toHexString(color).toUpperCase());
    }
    @Override
    protected boolean isDealsDamage() {
        return false;
    }

    public void dropItem() {
        ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), getStoredItem());
        this.level().addFreshEntity(itemEntity);
    }

    @Override
    public void remove(Entity.RemovalReason pReason) {
        super.remove(pReason);
        this.setRemoved(pReason);
        if (pReason == Entity.RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_DIE);
            this.dropItem();
        }
    }

    //Replace this with a proper function for health later on, currently this is for testing.
    @Override
    public void setSize(int size, boolean resetHealth) {
        super.setSize(size, resetHealth);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(10));
        this.setHealth(this.getMaxHealth());
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }



    float getSoundPitch() {
        float f = this.isTiny() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }

    static class SlimeFloatGoal extends Goal {
        private final EntityCruxiteSlime slime;

        public SlimeFloatGoal(EntityCruxiteSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            slime.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof EntityCruxiteSlime.SlimeMoveControl;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }

            ((EntityCruxiteSlime.SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.2);
        }
    }

    static class SlimeKeepOnJumpingGoal extends Goal {
        private final EntityCruxiteSlime slime;

        public SlimeKeepOnJumpingGoal(EntityCruxiteSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            return !this.slime.isPassenger();
        }

        public void tick() {
            ((EntityCruxiteSlime.SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.0D);
        }
    }

    static class SlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final EntityCruxiteSlime slime;
        private boolean isAggressive;

        public SlimeMoveControl(EntityCruxiteSlime slime) {
            super(slime);
            this.slime = slime;
            this.yRot = 180.0F * slime.getYRot() / (float)Math.PI;
        }

        public void setDirection(float yRot, boolean aggressive) {
            this.yRot = yRot;
            this.isAggressive = aggressive;
        }

        public void setWantedMovement(double speed) {
            this.speedModifier = speed;
            this.operation = Operation.MOVE_TO;
        }

        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = Operation.WAIT;
                if (this.mob.onGround()) {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.slime.getJumpControl().jump();
                        if (this.slime.doPlayJumpSound()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
                        }
                    } else {
                        this.slime.xxa = 0.0F;
                        this.slime.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }

        }
    }

    static class SlimeRandomDirectionGoal extends Goal {
        private final EntityCruxiteSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public SlimeRandomDirectionGoal(EntityCruxiteSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            return this.slime.getTarget() == null && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }

        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
                this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
            }

            ((EntityCruxiteSlime.SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
        }
    }
}
