package de.cxlledjay.bokarts.entity.custom.kart;

import com.google.common.collect.Lists;
import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.config.BoKartsConfig;
import de.cxlledjay.bokarts.config.ClientSyncedConfig;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.entity.custom.ModTrackedDataHandlers;
import de.cxlledjay.bokarts.entity.custom.kart.property.*;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.KartItemv2;
import de.cxlledjay.bokarts.networking.packet.KartEntityInputPayloadC2S;
import de.cxlledjay.bokarts.screen.custom.KartInventoryScreenHandler;
import de.cxlledjay.bokarts.sound.KartEngineSound;
import de.cxlledjay.bokarts.util.KartFuelItems;
import de.cxlledjay.bokarts.util.ModTags;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class KartEntity extends Entity implements RideableInventory{

    // ==================== vanilla entity stuff ====================
    protected static final TrackedData<Integer> DAMAGE_WOBBLE_TICKS = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> DAMAGE_WOBBLE_SIDE = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Float> DAMAGE_WOBBLE_STRENGTH = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private float ticksUnderwater;
    private float yawVelocity;
    private int lerpTicks;
    private double x;
    private double y;
    private double z;
    private double boatYaw;
    private double boatPitch;
    private double waterLevel;
    private KartEntity.Location location;
    public enum Location {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR;
    }

    // kart specific variables

    // -------------------- constants --------------------
    private static final float STEERING_SPEED = 20.0f;
    private static final float STEERING_CENTER = 0.0f;
    private static final float ENGINE_REVS_ACCEL = 4.2f;
    public static final float ENGINE_REVS_MAX = 100.0f;
    public static final float ENGINE_REVS_MIN = -42.0f;
    private static final int HORN_COOLDOWN = 10;
    private static final int KEY_FOB_DURATION = 100;

    // -------------------- tracked data --------------------
    // stats
    private static final TrackedData<HornType> HORN_TYPE = DataTracker.registerData(KartEntity.class, ModTrackedDataHandlers.HORN_TYPE_TRACKED_DATA_HANDLER);
    private static final TrackedData<Float> FUEL = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> ODO = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);

    // animation
    private static final TrackedData<Byte> PLAYER_INPUT = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.BYTE);

    // modification
    private static final TrackedData<WheelType> WHEEL_TYPE = DataTracker.registerData(KartEntity.class, ModTrackedDataHandlers.WHEEL_TYPE_TRACKED_DATA_HANDLER);
    private static final TrackedData<EngineType> ENGINE_TYPE = DataTracker.registerData(KartEntity.class, ModTrackedDataHandlers.ENGINE_TYPE_TRACKED_DATA_HANDLER);
    private static final TrackedData<BodyType> BODY_TYPE = DataTracker.registerData(KartEntity.class, ModTrackedDataHandlers.BODY_TYPE_TRACKED_DATA_HANDLER);
    private static final TrackedData<SpoilerType> SPOILER_TYPE = DataTracker.registerData(KartEntity.class, ModTrackedDataHandlers.SPOILER_TYPE_TRACKED_DATA_HANDLER);
    private static final TrackedData<AeroType> AERO_TYPE = DataTracker.registerData(KartEntity.class, ModTrackedDataHandlers.AERO_TYPE_TRACKED_DATA_HANDLER);

    private static final TrackedData<Integer> WHEEL_COLOR = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> BODY_COLOR = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> RACE_NUMBER = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.INTEGER);

    // key
    private static final TrackedData<Boolean> HAS_KEY = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Optional<UUID>> KEY_UUID = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    // -------------------- attributes --------------------

    // input tracking
    public boolean  pressingLeft, pressingRight, pressingForward, pressingBack, pressingSlow;
    private boolean lastSentLeft, lastSentRight, lastSentForward, lastSentBack, lastSentSlow;
    private static final byte FLAG_FORWARD  = 1;
    private static final byte FLAG_BACKWARD = 1 << 1;
    private static final byte FLAG_LEFT     = 1 << 2;
    private static final byte FLAG_RIGHT    = 1 << 3;
    private static final byte FLAG_SLOW     = 1 << 4;

    // custom physics
    private double lastTickX;
    private double lastTickY;
    private double lastTickZ;
    private boolean hasTrackedPos = false;
    public float acceleration = 0.04f;
    public float vMax = 2.0f;
    public float wheelFriction = 0.02f;
    public float frictionPenalty = 0.08f;
    public float handBrakeProgress = 0.0f;
    public float fuelConsumptionBase = 0.5f;
    public float stepHeight = 0.75f;

    // sound tracking
    private boolean engineSoundStarted = false;
    private float soundScaling = 0.0f;
    private int currentHornTicks = 0;

    // range tracking
    public float currentFuel = -1;
    public float currentOdometer = -1;

    // animation tracking
    public float frontAxleRotation = 0.0f;
    public float frontAxleRotationPrev = 0.0f;
    public float rearAxleRotation = 0.0f;
    public float rearAxleRotationPrev = 0.0f;
    public float steeringAngle = STEERING_CENTER;
    public float steeringAnglePrev = STEERING_CENTER;
    public float engineRevsPrev = 0.0f;
    public float engineRevs = 0.0f;
    public double distanceThisTick = 0.0f;

    // key fob tracking
    private int keyFobTimer = 0;




    // ==================== the kart ====================

    public KartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    public KartEntity(World world, double x, double y, double z) {
        this(ModEntities.KART_ENTITY_TYPE, world);
        this.setPosition(x, y, z);
        this.lastTickX = x;
        this.lastTickY = y;
        this.lastTickZ = z;
    }



    // ==================== vanilla methods / entity overrides ====================

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.EVENTS;
    }

    @Override
    protected double getGravity() {
        return 0.04;
    }

    @Override
    public Vec3d positionInPortal(Direction.Axis portalAxis, BlockLocating.Rectangle portalRect) {
        return LivingEntity.positionInPortal(super.positionInPortal(portalAxis, portalRect));
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.boatYaw = yaw;
        this.boatPitch = pitch;
        this.lerpTicks = 10;
    }

    private void updatePositionAndRotation() {
        if (this.isLogicalSideForUpdatingMovement()) {
            this.lerpTicks = 0;
            this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpTicks > 0) {
            this.lerpPosAndRotation(this.lerpTicks, this.x, this.y, this.z, this.boatYaw, this.boatPitch);
            this.lerpTicks--;
        }
    }

    @Override
    public double getLerpTargetX() {
        return this.lerpTicks > 0 ? this.x : this.getX();
    }

    @Override
    public double getLerpTargetY() {
        return this.lerpTicks > 0 ? this.y : this.getY();
    }

    @Override
    public double getLerpTargetZ() {
        return this.lerpTicks > 0 ? this.z : this.getZ();
    }

    @Override
    public float getLerpTargetPitch() {
        return this.lerpTicks > 0 ? (float)this.boatPitch : this.getPitch();
    }

    @Override
    public float getLerpTargetYaw() {
        return this.lerpTicks > 0 ? (float)this.boatYaw : this.getYaw();
    }

    @Override
    public Direction getMovementDirection() {
        return this.getHorizontalFacing().rotateYClockwise();
    }

    @Override
    protected Text getDefaultName() {
        return Text.translatable(this.asItem().getTranslationKey());
    }




    // ==================== damage handling ====================

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getWorld().isClient || this.isRemoved()) {
            return true;
        }

        if (this.isInvulnerableTo(source)) {
            return false;
        }

        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.scheduleVelocityUpdate();
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() + amount * 10.0F);
        this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
        boolean bl = source.getAttacker() instanceof PlayerEntity && ((PlayerEntity)source.getAttacker()).getAbilities().creativeMode;
        if ((bl || !(this.getDamageWobbleStrength() > 40.0F)) && !this.shouldAlwaysKill(source)) {
            if (bl) {
                this.discard();
            }
        } else {
            this.killAndDropSelf(source);
        }

        return true;
    }

    boolean shouldAlwaysKill(DamageSource source) {
        return false;
    }

    @Override
    public void animateDamage(float yaw) {
        this.setDamageWobbleSide(-this.getDamageWobbleSide());
        this.setDamageWobbleTicks(10);
        this.setDamageWobbleStrength(this.getDamageWobbleStrength() * 11.0F);
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }



    // ==================== water stuff from boat code ==================== TODO: check it is needed...

    private boolean checkBoatInWater() {
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.minY + 0.001);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl = false;
        this.waterLevel = -Double.MAX_VALUE;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int o = i; o < j; o++) {
            for (int p = k; p < l; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER)) {
                        float f = p + fluidState.getHeight(this.getWorld(), mutable);
                        this.waterLevel = Math.max(f, this.waterLevel);
                        bl |= box.minY < f;
                    }
                }
            }
        }

        return bl;
    }

    @Nullable
    private KartEntity.Location getUnderWaterLocation() {
        Box box = this.getBoundingBox();
        double d = box.maxY + 0.001;
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.maxY);
        int l = MathHelper.ceil(d);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl = false;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int o = i; o < j; o++) {
            for (int p = k; p < l; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER) && d < mutable.getY() + fluidState.getHeight(this.getWorld(), mutable)) {
                        if (!fluidState.isStill()) {
                            return KartEntity.Location.UNDER_FLOWING_WATER;
                        }

                        bl = true;
                    }
                }
            }
        }

        return bl ? KartEntity.Location.UNDER_WATER : null;
    }

    @Override
    public boolean isSubmergedInWater() {
        return this.location == KartEntity.Location.UNDER_WATER || this.location == KartEntity.Location.UNDER_FLOWING_WATER;
    }





    // ==================== passengers ====================

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ActionResult actionResult = super.interact(player, hand);
        if (actionResult != ActionResult.PASS) {
            return actionResult;
        }

        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        }

        if (this.ticksUnderwater < 60.0F) {
            if (!this.getWorld().isClient) {
                return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
            } else {
                return ActionResult.SUCCESS;
            }
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        super.updatePassengerPosition(passenger, positionUpdater);
        if (passenger.equals(this.getControllingPassenger())) {
            passenger.setYaw(passenger.getYaw() + this.yawVelocity);
            passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
            this.clampPassengerYaw(passenger);
        } else {
            float deltaYaw = this.getYaw() - this.prevYaw;
            passenger.setYaw(passenger.getYaw() + deltaYaw);
            passenger.setHeadYaw(passenger.getHeadYaw() + deltaYaw);
            passenger.setBodyYaw(this.getYaw());
        }
    }

    protected int getMaxPassengers() {
        return (this.getSpoilerType() == SpoilerType.RACE_PLUS || this.getSpoilerType() == SpoilerType.RACE_PLUS_CARBON) ? 3 : 1;
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {

        float x = 0.0f;
        float y = 0.05f;
        float z = -0.4f;

        if (this.getPassengerList().size() > 1) {
            int i = this.getPassengerList().indexOf(passenger);

            if(i == 1) {
                x = -0.75f;
                y = -0.6225f;
                z = -1.4f;
            }
            else if(i == 2) {
                x = 0.75f;
                y = -0.6225f;
                z = -1.4f;
            }
        }

        return new Vec3d(x, dimensions.height() / 3.0F - y, z).rotateY(-this.getYaw() * (float) (Math.PI / 180.0));
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        passenger.setYaw(this.getYaw());
        passenger.setHeadYaw(this.getYaw());
        passenger.setBodyYaw(this.getYaw());
    }

    @Override
    protected void removePassenger(Entity passenger) {
        // stop animations etc
        if(passenger instanceof PlayerEntity && passenger == this.getControllingPassenger()) {
            if(this.getWorld().isClient) {
                this.engineRevs = 0.0f;
                this.engineRevsPrev = 0.0f;
            } else {
                this.setInputsByServer(false,false,false,false,false);
            }
        }
        super.removePassenger(passenger);
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().size() < this.getMaxPassengers() && !this.isSubmergedIn(FluidTags.WATER) && passenger instanceof PlayerEntity;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof LivingEntity livingEntity ? livingEntity : super.getControllingPassenger();
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset(this.getWidth() * MathHelper.SQUARE_ROOT_OF_TWO, passenger.getWidth(), passenger.getYaw());
        double d = this.getX() + vec3d.x;
        double e = this.getZ() + vec3d.z;
        BlockPos blockPos = BlockPos.ofFloored(d, this.getBoundingBox().maxY, e);
        BlockPos blockPos2 = blockPos.down();
        if (!this.getWorld().isWater(blockPos2)) {
            List<Vec3d> list = Lists.newArrayList();
            double f = this.getWorld().getDismountHeight(blockPos);
            if (Dismounting.canDismountInBlock(f)) {
                list.add(new Vec3d(d, blockPos.getY() + f, e));
            }

            double g = this.getWorld().getDismountHeight(blockPos2);
            if (Dismounting.canDismountInBlock(g)) {
                list.add(new Vec3d(d, blockPos2.getY() + g, e));
            }

            for (EntityPose entityPose : passenger.getPoses()) {
                for (Vec3d vec3d2 : list) {
                    if (Dismounting.canPlaceEntityAt(this.getWorld(), vec3d2, passenger, entityPose)) {
                        passenger.setPose(entityPose);
                        return vec3d2;
                    }
                }
            }
        }

        return super.updatePassengerForDismount(passenger);
    }

    protected void clampPassengerYaw(Entity passenger) {
        passenger.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(passenger.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        passenger.prevYaw += g - f;
        passenger.setYaw(passenger.getYaw() + g - f);
        passenger.setHeadYaw(passenger.getYaw());
    }

    @Override
    public void onPassengerLookAround(Entity passenger) {
        this.clampPassengerYaw(passenger);
    }





    // ==================== collisions ====================
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        if (entity instanceof KartEntity) {
            if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.pushAwayFrom(entity);
            }
        } else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.pushAwayFrom(entity);
        }
    }

    @Override
    public boolean collidesWith(Entity other) {
        return false;
    }

    private void handleKartCollisions(Vec3d velocityDirection, double distanceThisTick) {

        // get nearby entities
        List<Entity> possibleHitEntities = this.getWorld().getOtherEntities(
                this,
                this.getBoundingBox().expand(0.2F, -0.01F, 0.2F),
                entity -> !this.hasPassenger(entity) && entity instanceof LivingEntity && !entity.isSpectator());

        // optimize
        if(possibleHitEntities.isEmpty()) return;

        // iterate trough them
        for(Entity entity : possibleHitEntities) {

            // hit while driving
            if(!this.getWorld().isClient() && distanceThisTick > 0.1) {

                // yeet only living entities
                if(entity instanceof LivingEntity livingEntity) {

                    // no knockback if entity is riding something
                    if(livingEntity.hasVehicle()) continue;

                    // no knockback if entity is player in spectator or creative
                    if(livingEntity instanceof ServerPlayerEntity player) {
                        if(player.isSpectator() || player.isCreative()) continue;
                    }

                    // deal damage
                    float inflictedDamage = (float) (distanceThisTick * 3.5);
                    livingEntity.damage(this.getDamageSources().generic(), inflictedDamage);

                    // yeet them
                    Vec3d knockback = velocityDirection.multiply(distanceThisTick * 4.2).add(0, 0.5 + (distanceThisTick/ 4), 0);
                    livingEntity.addVelocity(knockback.x, knockback.y, knockback.z);
                    livingEntity.velocityModified = true; // send update to client!

                    // play sound
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                            net.minecraft.sound.SoundEvents.ENTITY_GENERIC_EXPLODE,
                            SoundCategory.PLAYERS,
                            0.6f,
                            2.0f
                            //1.0f + ((float) distanceThisTick * 0.2f)
                    );
                }
            }
            // hit while standing still => push away
            else if (EntityPredicates.canBePushedBy(this).test(entity) && !entity.hasPassenger(this)) {
                this.pushAwayFrom(entity);
            }
        }
    }




    // ==================== edge cases in movement ====================

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        if (!this.hasVehicle()) {
            if (onGround) {
                this.onLanding();
            } else if (!this.getWorld().getFluidState(this.getBlockPos().down()).isIn(FluidTags.WATER) && heightDifference < 0.0) {
                this.fallDistance -= (float)heightDifference;
            }
        }
    }




    // ==================== item management ====================

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    protected Item asItem() {
        return ModItems.KART_ITEM;
    }

    // set data component types of ItemStack to tracked data
    private ItemStack syncAndGetKartAsCustomStack() {
        this.setFuelSynced(this.currentFuel);
        this.setOdometerSynced(this.currentOdometer);

        return getKartAsCustomStack();
    }

    public ItemStack getKartAsCustomStack() {

        // get item
        ItemStack stack = new ItemStack(this.asItem());

        // set DataComponentTypes
        stack.set(ModDataComponentTypes.KART_ITEM_HORN_TYPE, this.getHornType());
        stack.set(ModDataComponentTypes.KART_ITEM_FUEL, this.getFuelSynced());
        stack.set(ModDataComponentTypes.KART_ITEM_ODOMETER, this.getOdometerSynced());

        // set v2 data component types
        stack.set(ModDataComponentTypes.KART_ITEM_BODY_TYPE, this.getBodyType());
        stack.set(ModDataComponentTypes.KART_ITEM_WHEEL_TYPE, this.getWheelType());
        stack.set(ModDataComponentTypes.KART_ITEM_ENGINE_TYPE, this.getEngineType());
        stack.set(ModDataComponentTypes.KART_ITEM_SPOILER_TYPE, this.getSpoilerType());
        stack.set(ModDataComponentTypes.KART_ITEM_AERO_TYPE, this.getAeroType());
        stack.set(ModDataComponentTypes.KART_ITEM_BODY_COLOR, this.getBodyColor());
        stack.set(ModDataComponentTypes.KART_ITEM_WHEEL_COLOR, this.getWheelColor());
        stack.set(ModDataComponentTypes.KART_ITEM_RACE_NUMBER, this.getRaceNumber());

        // set name with livery
        Text kartName = this.getCustomName();
        if(kartName == null || kartName.equals(Text.translatable("item.bokarts.kart_item"))) {
             MutableText name = Text.literal("Kart [").formatted(Formatting.WHITE)
                    .append(KartItemv2.getFormattedLiveryName(this.getBodyType(), this.getBodyColor())
                            .append(Text.literal("]")))
                    .styled(style -> style.withItalic(false));
            stack.set(DataComponentTypes.CUSTOM_NAME, name);
        } else {
            stack.set(DataComponentTypes.CUSTOM_NAME, kartName);
        }



        // save key data
        stack.set(ModDataComponentTypes.KART_ITEM_HAS_KEY, this.isKeyInIgnition());
        stack.set(ModDataComponentTypes.KART_KEY_PAIRED_UUID, this.getKeyUuid());

        return stack;
    }

    // override creative middle click
    @Override
    public ItemStack getPickBlockStack() {
        return this.syncAndGetKartAsCustomStack();
    }

    // override dropped item on destroy
    @Override
    public ItemEntity dropStack(ItemStack stack, float yOffset) {
        // Check if the game is trying to drop our base Kart item
        if (stack.isOf(this.asItem())) {
            // Swap out the blank vanilla stack with our custom Component stack!
            stack = this.syncAndGetKartAsCustomStack();
        }

        // Let the game spawn the item into the world
        return super.dropStack(stack, yOffset);
    }

    public void killAndDropItem(Item selfAsItem) {
        this.kill();
        if (this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            ItemStack itemStack = new ItemStack(selfAsItem);
            itemStack.set(DataComponentTypes.CUSTOM_NAME, this.getCustomName());
            this.dropStack(itemStack);
        }
    }

    protected void killAndDropSelf(DamageSource source) {
        this.killAndDropItem(this.asItem());
    }

    // interface to check if kart can be driven / spawned (has wheels and engine)
    public boolean isDrivable() {
        return ((this.getWheelType() != WheelType.NONE) && (this.getEngineType() != EngineType.ENGINE_NONE));
    }







    // ==================== input handling ====================

    //@Override
    public void setInputs(boolean left, boolean right, boolean forward, boolean back) {
        this.pressingLeft = left;
        this.pressingRight = right;
        this.pressingForward = forward;
        this.pressingBack = back;

        if (this.getWorld().isClient()) {

            // steal space key
            Entity driver = this.getControllingPassenger();
            if (driver instanceof ClientPlayerEntity clientPlayer) {
                this.pressingSlow = clientPlayer.input.jumping;
            }

            // check if inputs changed
            if (this.pressingForward != lastSentForward || this.pressingBack != lastSentBack ||
                    this.pressingLeft != lastSentLeft || this.pressingRight != lastSentRight || this.pressingSlow != lastSentSlow) {


                // send new input to server
                ClientPlayNetworking.send(new KartEntityInputPayloadC2S(
                        this.getId(), this.pressingLeft, this.pressingRight, this.pressingForward, this.pressingBack, this.pressingSlow
                ));

                // update trackers
                this.lastSentForward = this.pressingForward;
                this.lastSentBack = this.pressingBack;
                this.lastSentLeft = this.pressingLeft;
                this.lastSentRight = this.pressingRight;
                this.lastSentSlow = this.pressingSlow;
            }
        }
    }

    public void setInputsByServer(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack, boolean pressingSlow) {
        // update controls on server side
        this.pressingLeft = pressingLeft;
        this.pressingRight = pressingRight;
        this.pressingForward = pressingForward;
        this.pressingBack = pressingBack;
        this.pressingSlow = pressingSlow;

        if (!this.getWorld().isClient()) {
            byte input = 0;
            if (pressingForward) input |= FLAG_FORWARD;
            if (pressingBack) input |= FLAG_BACKWARD;
            if (pressingLeft) input |= FLAG_LEFT;
            if (pressingRight) input |= FLAG_RIGHT;
            if (pressingSlow) input |= FLAG_SLOW;

            this.setPlayerInput(input);
        }
    }







    // ==================== kart driving behaviour ====================

    @Override
    public float getStepHeight() {
        return this.stepHeight;
    }

    private boolean isDrivableBlock(BlockState blockState) {
        return blockState.isIn(ModTags.Blocks.DRIVABLE_BLOCKS);
    }

    private KartEntity.Location checkLocation() {
        KartEntity.Location location = this.getUnderWaterLocation();
        if (location != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return location;
        } else if (this.checkBoatInWater()) {
            return KartEntity.Location.IN_WATER;
        } else {
            return KartEntity.Location.ON_LAND;
        }
    }




    private void updateMovement() {
        double gravity = -this.getFinalGravity();
        float appliedFriction = 0.3f; //< default

        if(this.isOnGround()) {
            // when somebody is driving on ground, apply normal friction
            BlockState groundBlock = this.getWorld().getBlockState(this.getSteppingPos());
            if(getControllingPassenger() instanceof PlayerEntity) {
                appliedFriction = (isDrivableBlock(groundBlock)) ? (this.wheelFriction) : (this.wheelFriction + this.frictionPenalty);
            }
        } else if(this.isTouchingWater()) {
            // in water => higher friction + gravity
            gravity = -7.0E-4;
            appliedFriction = Math.min(0.8f, this.wheelFriction + this.frictionPenalty * 2);
        } else {
            // in air => a bit more friction
            appliedFriction = this.wheelFriction * 1.25f;
        }


        // apply handbrake
        appliedFriction += this.handBrakeProgress * (1.0f - appliedFriction);



        float decay = Math.max(0.0f, 1.0f - appliedFriction);

        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x * decay, vec3d.y + gravity, vec3d.z * decay);
        this.yawVelocity = this.yawVelocity * decay;

    }

    private void updateAcceleration() {
        if (this.hasPassengers()) {

            if(!this.isDrivable()) return; // no update when undrivable

            // result
            float appliedAcceleration = 0.0f;

            // current speed
            Vec3d currentVel = this.getVelocity();
            float yawRad = this.getYaw() * (float) (Math.PI / 180.0);
            float v = (float) Math.sqrt(currentVel.x * currentVel.x + currentVel.z * currentVel.z);

            // physics
            float maintenanceForce = this.vMax * this.wheelFriction;
            float effectiveVMax = this.vMax * (1.0f - this.wheelFriction);
            float ratio = Math.min((v/effectiveVMax), 1.0f);
            float a = this.acceleration - (this.acceleration - maintenanceForce) * ratio;

            // get input types
            if (this.pressingLeft) {
                this.yawVelocity--;
            }

            if (this.pressingRight) {
                this.yawVelocity++;
            }

            if (this.pressingRight != this.pressingLeft && !this.pressingForward && !this.pressingBack) {
                appliedAcceleration += a / 8.0f;
            }

            this.setYaw(this.getYaw() + this.yawVelocity);
            if (this.pressingForward) {
                appliedAcceleration += a;
            }

            if (this.pressingBack) {
                appliedAcceleration -= a / 4.0f;
            }

            this.setVelocity(
                    this.getVelocity().add(
                            MathHelper.sin(-yawRad) * appliedAcceleration,
                            0.0,
                            MathHelper.cos(yawRad) * appliedAcceleration
            ));
        }
    }


    // call when kart is created / loaded / updated
    public void recalculateKartPhysics() {
        // get tuning parts
        EngineType engine = this.getEngineType();
        WheelType wheel = this.getWheelType();
        SpoilerType spoiler = this.getSpoilerType();
        AeroType aero = this.getAeroType();

        // calculate physics
        this.acceleration           = engine.getbaseA() * ( 1 + aero.getMultiA() + spoiler.getMultiA() + wheel.getMultiA() );
        this.vMax                   = engine.getbaseV() * ( 1 + aero.getMultiV() + spoiler.getMultiV() + wheel.getMultiV() );
        this.wheelFriction          = wheel.getBaseF()  * ( 1 + aero.getMultiF() + spoiler.getMultiF() );

        this.frictionPenalty        = wheel.getPenalityF();
        this.stepHeight             = wheel.getStepHeight();
        this.fuelConsumptionBase    = 0.5f * engine.getFuelMulti();
    }







    // ==================== main function tick() ====================


    @Override
    public void tick() {

        // underwater check
        this.location = this.checkLocation();

        if (this.location != Location.UNDER_WATER && this.location != Location.UNDER_FLOWING_WATER && this.location != Location.IN_WATER) {
            this.ticksUnderwater = 0.0F;
        } else {
            this.ticksUnderwater++;
        }


        // -------------------- vanilla stuff --------------------

        // damage wobble
        if (this.getDamageWobbleTicks() > 0) {
            this.setDamageWobbleTicks(this.getDamageWobbleTicks() - 1);
        }
        if (this.getDamageWobbleStrength() > 0.0F) {
            this.setDamageWobbleStrength(this.getDamageWobbleStrength() - 1.0F);
        }
        super.tick();
        this.updatePositionAndRotation();


        // ==================== movement physics ====================
        if (this.isLogicalSideForUpdatingMovement()) {

            // set inputs
            if (this.getControllingPassenger() instanceof PlayerEntity player) {
                boolean forward = player.forwardSpeed > 0.0F;
                boolean backward = player.forwardSpeed < 0.0F;
                boolean left = player.sidewaysSpeed > 0.0F;
                boolean right = player.sidewaysSpeed < 0.0F;

                this.setInputs(left, right, forward, backward);
            }

            this.updateMovement();
            if (this.getWorld().isClient && !this.canNotDrive()) {
                this.updateAcceleration();
            }

            // --- interpolate steps for fast vehicles ---
            Vec3d totalVel = this.getVelocity();
            double horizontalSpeed = Math.sqrt(totalVel.x * totalVel.x + totalVel.z * totalVel.z);
            // moving faster than 2 bps => steps > 1
            int steps = (int) Math.ceil(horizontalSpeed);
            if (steps > 1) {
                // get equal sized chunks
                Vec3d chunkVel = totalVel.multiply(1.0 / steps);
                // call move function multiple times with chunked velocity
                for (int i = 0; i < steps; i++) {
                    this.move(MovementType.SELF, chunkVel);
                    if (this.horizontalCollision) {
                        break;
                    }
                }
            } else {
                // as normal
                this.move(MovementType.SELF, totalVel);
            }
        } else {
            this.setVelocity(Vec3d.ZERO);

            // track input from all other clients spectating the kart
            if (this.getWorld().isClient()) {
                byte flags = this.getPlayerInput();
                this.pressingForward = (flags & FLAG_FORWARD) != 0;
                this.pressingBack    = (flags & FLAG_BACKWARD) != 0;
                this.pressingLeft    = (flags & FLAG_LEFT) != 0;
                this.pressingRight   = (flags & FLAG_RIGHT) != 0;
                this.pressingSlow    = (flags & FLAG_SLOW) != 0;
            }
        }

        // ==================== post movement calculations ====================
        if(!hasTrackedPos) {
            this.lastTickX = this.getX();
            this.lastTickY = this.getY();
            this.lastTickZ = this.getZ();
            this.hasTrackedPos = true;
        }

        Vec3d movementThisTick = new Vec3d(
                this.getX() - this.lastTickX,
                this.getY() - this.lastTickY,
                this.getZ() - this.lastTickZ);
        this.distanceThisTick = movementThisTick.length();

        // -------------------- collision check --------------------
        this.checkBlockCollision();
        this.handleKartCollisions(movementThisTick.normalize(), this.distanceThisTick);

        // -------------------- server synced animations --------------------
        this.steeringAnglePrev = this.steeringAngle;
        this.engineRevsPrev = this.engineRevs;
        this.calculateSteeringAngle();
        this.calculateEngineRevs();
        this.calculateHandBrake();

        // -------------------- kart stats --------------------
        // init
        if (this.currentFuel == -1) this.currentFuel = this.getFuelSynced();
        if (this.currentOdometer == -1) this.currentOdometer = this.getOdometerSynced();

        // fuel consumption
        if(this.pressingForward || this.pressingBack) {
            // we are accelerating: consume fuel!
            float revFactor = 0.5f + (Math.abs(this.engineRevs) / ENGINE_REVS_MAX);
            float fuelDrain = this.fuelConsumptionBase * revFactor;
            this.currentFuel = Math.max(0, this.currentFuel - fuelDrain); // prevent going under 0
        }
        this.currentOdometer += (float) this.distanceThisTick;

        //==================== server sided updates ====================
        if (!this.getWorld().isClient()) {

            // spend too long in water => kaboom
            if (this.ticksUnderwater >= ClientSyncedConfig.getMaxTicksInWater()) {

                // eject passengers
                this.removeAllPassengers();

                // spawn explosion
                this.getWorld().createExplosion(
                        this,     // The entity causing the explosion
                        this.getX(),    // X coordinate
                        this.getY(),    // Y coordinate
                        this.getZ(),    // Z coordinate
                        1.5f,           // Power (TNT is 4.0f, Creeper is 3.0f)
                        World.ExplosionSourceType.NONE // NONE = damages players/entities, but DOES NOT break blocks!
                );

                // kill kart and drop it
                this.killAndDropItem(this.asItem());

                // despawn it
                this.discard();
            }

            // sync client data with exact data from server, executed every second
            if(this.age % 20 == 0) {
                this.setFuelSynced(this.currentFuel);
                this.setOdometerSynced(this.currentOdometer);
            }

            // horn
            if(this.currentHornTicks > 0) this.currentHornTicks--;
            this.tickKeyFob();
        }

        //==================== client sided animations ====================
        else {

            // animations when kart is able to drive
            if(!this.canNotDrive()) {

                // kart speed calculation
                double yawRad = Math.toRadians(this.getYaw());
                double forwardX = -Math.sin(yawRad);
                double forwardZ = Math.cos(yawRad);
                double forwardSpeed = (movementThisTick.x * forwardX) + (movementThisTick.z * forwardZ);

                // resulting rotation
                float frontAxleRotationThisTick = (float) (forwardSpeed * 5.34);
                float backAxleRotationThisTick = frontAxleRotationThisTick + this.engineRevs / 33;

                // ---------------- wheel spin front ----------------
                this.frontAxleRotationPrev = this.frontAxleRotation;
                this.frontAxleRotation = this.frontAxleRotation + frontAxleRotationThisTick;

                // ---------------- wheel spin back ----------------
                this.rearAxleRotationPrev = this.rearAxleRotation;
                if(pressingSlow) {
                    // no wheelspin => rear = front
                    this.rearAxleRotation = this.frontAxleRotation;
                } else {
                    // added wheelspin
                    this.rearAxleRotation = this.rearAxleRotation + backAxleRotationThisTick;
                }

                // ---------------- sounds ----------------
                this.setSoundScaling(Math.abs(this.engineRevs));

                if (this.getWorld().isClient) {
                    if (this.hasPassengers() && !this.engineSoundStarted) {
                        KartEngineSound.playEngineSound(this);
                        this.engineSoundStarted = true;
                    } else if (!this.hasPassengers() && this.engineSoundStarted) {
                        this.engineSoundStarted = false;
                    }
                }

            // no driving
            } else {
                frontAxleRotation = 0.0f;
                frontAxleRotationPrev = 0.0f;
                rearAxleRotation = 0.0f;
                rearAxleRotationPrev = 0.0f;
                this.engineSoundStarted = false;
            }

        }

        // update position tracking
        this.lastTickX = this.getX();
        this.lastTickY = this.getY();
        this.lastTickZ = this.getZ();
    }







    // ==================== tick() helpers ====================

    public boolean canNotDrive() {
        return this.currentFuel <= 0.0f || (!this.isKeyInIgnition() && this.getKeyUuid() != null);
    }

    private void calculateSteeringAngle() {

        if (!(pressingLeft && pressingRight)) {
            if (pressingLeft) {
                // steering to the left
                this.steeringAngle += STEERING_SPEED;
            } else if (pressingRight) {
                // steering to the right
                this.steeringAngle -= STEERING_SPEED;
            } else {
                // no button pressed => center spring
                this.steeringAngle = applySteeringWheelCenterSpring(this.steeringAngle);
            }
        } else {
            // pressing left & right => center spring
            this.steeringAngle = applySteeringWheelCenterSpring(this.steeringAngle);
        }
        // clamp steering to 0 - 180 degs
        this.steeringAngle = MathHelper.clamp(this.steeringAngle, STEERING_CENTER - (STEERING_SPEED * 5), STEERING_CENTER + (STEERING_SPEED * 5));

    }

    private float applySteeringWheelCenterSpring(float angle) {
        if(angle < (STEERING_CENTER + STEERING_SPEED) && angle > (STEERING_CENTER - STEERING_SPEED)) {
            angle = STEERING_CENTER;
        } else if(angle > STEERING_CENTER) {
            angle -= STEERING_SPEED;
        } else if(angle < STEERING_CENTER) {
            angle += STEERING_SPEED;
        }

        return angle;
    }

    private void calculateEngineRevs() {

        if(pressingForward) {
            float dMax = 1.0f - (this.engineRevs / ENGINE_REVS_MAX);
            float dynamicAccel = ENGINE_REVS_ACCEL * Math.max(0.2f, dMax);
            this.engineRevs += dynamicAccel;
        } else if(pressingBack) {
            float distanceToMin = 1.0f - (this.engineRevs / ENGINE_REVS_MIN);
            float dynamicAccel = ENGINE_REVS_ACCEL * Math.max(0.2f, distanceToMin);
            this.engineRevs -= dynamicAccel;
        } else {
            this.engineRevs = applyEngineFlywheel(this.engineRevs);
        }
        this.engineRevs = MathHelper.clamp(this.engineRevs, ENGINE_REVS_MIN, ENGINE_REVS_MAX);
    }

    private float applyEngineFlywheel(float currentRevs) {
        currentRevs *= 0.90f;
        if (Math.abs(currentRevs) < (ENGINE_REVS_ACCEL * 0.1f)) {
            return 0.0f;
        }
        return currentRevs;
    }

    private void calculateHandBrake() {
        if(!pressingSlow) this.handBrakeProgress = 0.0f; //< instantly snap back to normal when released
        else {
            float dynamicBrake = 0.02f + (this.handBrakeProgress * 0.125f);
            this.handBrakeProgress = Math.min(this.handBrakeProgress + dynamicBrake, 1.0f);
        }
    }


    private void tickKeyFob() {
        // only do something when active
        if(this.keyFobTimer > 0) {

            if(this.keyFobTimer == KEY_FOB_DURATION) {
                // play first horn
                this.playHornSoundNoCooldown();
            } else if (this.keyFobTimer == KEY_FOB_DURATION-5) {
                // play second horn
                this.playHornSoundNoCooldown();
            } else if (this.keyFobTimer == 1) {
                // reset
                this.setGlowing(false);
            }

            this.keyFobTimer--;
        }
    }

    public void triggerKeyFob() {
        if(this.keyFobTimer > 0) return; //< no spamming!
        this.keyFobTimer = KEY_FOB_DURATION;
        this.setGlowing(true);
    }




    // ==================== sounds ====================

    public float getSoundScaling() {
        return soundScaling;
    }

    private void setSoundScaling(float soundScaling) {
        this.soundScaling = soundScaling;
    }

    // horn
    public void playHornSound() {
        if(this.currentHornTicks != 0) return; // on cooldown
        this.currentHornTicks = HORN_COOLDOWN; // set cooldown

        this.playHornSoundNoCooldown();
    }

    private void playHornSoundNoCooldown() {
        this.getWorld().playSound(
                null, // Excluded player (null = everyone hears it)
                this.getX(), this.getY(), this.getZ(),
                this.getHornType().getSoundEvent(),
                SoundCategory.RECORDS,
                0.75f,
                1.0f
        );
    }

    public void cycleHornSound(ServerPlayerEntity player, int direction) {

        // calculate HornSound
        HornType newHorn;
        if(direction > 0) {
            // next
            newHorn = this.getHornType().next();
        } else {
            // previous
            newHorn = this.getHornType().previous();
        }

        // preview HornSound
        Identifier oldSoundId = this.getHornType().getSoundEvent().getId();
        player.networkHandler.sendPacket(new StopSoundS2CPacket(oldSoundId, SoundCategory.PLAYERS));
        player.playSoundToPlayer(newHorn.getSoundEvent(), SoundCategory.PLAYERS, BoKartsConfig.hornPreviewVolume, 1.0f);
        String msg = getFormattedHornName(newHorn);
        player.sendMessage(Text.literal(msg).formatted(Formatting.YELLOW), true);

        // set HornSound for Kart
        this.setHornType(newHorn);
    }






    // ==================== fuel and odometer ====================

    @Override
    public void openInventory(PlayerEntity player) {
        if(!this.getWorld().isClient) {

            Text kartName = this.getCustomName();
            if(kartName == null || kartName.equals(Text.translatable("item.bokarts.kart_item"))) {
                kartName = Text.literal("Kart [").formatted(Formatting.WHITE)
                        .append(KartItemv2.getFormattedLiveryName(this.getBodyType(), this.getBodyColor())
                                .append(Text.literal("]")))
                        .styled(style -> style.withItalic(false));
            }

            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, playerInventory, player1) ->
                            new KartInventoryScreenHandler(syncId, playerInventory), kartName)
            );
        }
    }

    public void addFuelFromItem(ItemStack fuel) {

        if(!this.getWorld().isClient() && !fuel.isEmpty()) {

            // edge case: fuel not yet initialized
            if(this.currentFuel == -1) this.currentFuel = this.getFuelSynced();

            // get fuel consumption per item
            float fuelProItem = KartFuelItems.getFuelAmountFromItemStack(fuel);

            // if somehow the screen handler did give us a non fuel block
            if (fuelProItem <= 0.0f) {
                return;
            }

            // calculate needed items for refueling
            int neededItemsUntilFull = (int) Math.ceil((ClientSyncedConfig.getMaxFuelCapacity() - this.currentFuel) / fuelProItem);
            int consumedItems = Math.min(neededItemsUntilFull, fuel.getCount());

            // refuel the engine and remove items from inventory
            this.currentFuel = Math.min((this.currentFuel + fuelProItem * consumedItems), ClientSyncedConfig.getMaxFuelCapacity());
            fuel.decrement(consumedItems);

            // sync with client
            this.setFuelSynced(this.currentFuel);

            //play sound depending on fuel level and consumed items
            SoundEvent refillSound = SoundEvents.BLOCK_NOTE_BLOCK_BASS.value();
            float pitch = 0.9f;

            if(consumedItems > 0) {
                if(currentFuel >= ClientSyncedConfig.getMaxFuelCapacity()) {
                    // refilled to max capacity
                    refillSound = SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
                    pitch = 1.1f;
                } else {
                    // refilled but not yet max capacity
                    refillSound = SoundEvents.ITEM_BOTTLE_FILL;
                    pitch = 0.6f;
                }
            }

            // broadcast sound
            this.getWorld().playSound(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    refillSound,
                    SoundCategory.PLAYERS,
                    1.0f,
                    pitch
            );
        }
    }






    // ==================== display methods ====================

    public static String getFormattedDistanceString(Float distance) {
        String  rangeString;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        if(distance < 1000) {
            // less than 1km
            rangeString = decimalFormat.format(distance) + " m";
        } else {
            // 1km or more
            rangeString = decimalFormat.format(distance / 1000.0f) + " km";
        }

        return rangeString;
    }

    public static String getFormattedFuelCapacityString(Float fuelCapacity) {
        DecimalFormat decimalFormat1 = new DecimalFormat("0.0");
        return decimalFormat1.format(fuelCapacity / 1000.0f) + "L/" + decimalFormat1.format(ClientSyncedConfig.getMaxFuelCapacity() / 1000.0f) + "L";
    }

    public static String getFormattedHornName(HornType hornType) {
        return "[" + (hornType.getId() + 1) + "/" + HornType.values().length + "] " + hornType.getDisplayName();
    }

    public static boolean isValidRaceNumber(int raceNumber) {
        return (raceNumber >= 1 && raceNumber <= 99);
    }






    // ==================== data tracking ====================

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        // vanilla stuff
        builder.add(DAMAGE_WOBBLE_TICKS, 0);
        builder.add(DAMAGE_WOBBLE_SIDE, 1);
        builder.add(DAMAGE_WOBBLE_STRENGTH, 0.0F);

        // fuel and distance
        builder.add(FUEL, 0.0f);
        builder.add(ODO, 0.0f);

        // animations
        builder.add(PLAYER_INPUT, (byte) 0);

        // v2 model
        builder.add(HORN_TYPE, HornType.HORN1);
        builder.add(WHEEL_TYPE, WheelType.NONE);
        builder.add(ENGINE_TYPE, EngineType.ENGINE_NONE);
        builder.add(BODY_TYPE, BodyType.NONE);
        builder.add(SPOILER_TYPE, SpoilerType.NONE);
        builder.add(AERO_TYPE, AeroType.NONE);
        builder.add(BODY_COLOR, 0xFFFFFF);
        builder.add(WHEEL_COLOR, 0xFFFFFF);
        builder.add(RACE_NUMBER, 0);

        // key
        builder.add(HAS_KEY, false);
        builder.add(KEY_UUID, Optional.empty());
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("Fuel", this.getFuelSynced());
        nbt.putFloat("Odo", this.getOdometerSynced());

        // v2 model
        nbt.putInt("HornType", this.getHornType().getId());
        nbt.putInt("WheelType", this.getWheelType().getId());
        nbt.putInt("EngineType", this.getEngineType().getId());
        nbt.putInt("BodyType", this.getBodyType().getId());
        nbt.putInt("SpoilerType", this.getSpoilerType().getId());
        nbt.putInt("AeroType", this.getAeroType().getId());
        nbt.putInt("BodyColor", this.getBodyColor());
        nbt.putInt("WheelColor", this.getWheelColor());
        nbt.putInt("RaceNumber", this.getRaceNumber());

        nbt.putBoolean("HasKey", this.isKeyInIgnition());
        // key
        if (this.getKeyUuid() != null) {
            nbt.putUuid("KeyUuid", this.getKeyUuid());
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        // convert old paints
        if (nbt.contains("PaintColor")) {

            String oldPaintColorAttribute =  nbt.getString("PaintColor");

            BodyType bodyType = switch (oldPaintColorAttribute) {
                case "fade" -> BodyType.LIVERY_FADE;
                default -> BodyType.SOLID_COLOR_OVERLAY;
            };

            int bodyColor = switch (oldPaintColorAttribute) {
                case "orange" -> 0xF9801D;
                case "magenta" -> 0xC74EBD;
                case "light_blue" -> 0x3AB3DA;
                case "yellow" -> 0xFED83D;
                case "lime" -> 0x80C71F;
                case "pink" -> 0xF38BAA;
                case "gray" -> 0x474F52;
                case "light_gray" -> 0x9D9D97;
                case "cyan" -> 0x169C9C;
                case "purple" -> 0x8932B8;
                case "blue" -> 0x3C44AA;
                case "brown" -> 0x835432;
                case "green" -> 0x5E7C16;
                case "red" -> 0xB02E26;
                case "black" -> 0x1D1D21;
                case "purple_gold" -> 0x70569C;
                case "default" -> 0xA086D3;
                default -> 0xFFFFFF;
            };

            int wheelColor = switch (oldPaintColorAttribute) {
                case "purple_gold" -> 0xC0996F;
                case "fade" -> 0xA43148;
                default -> 0xFFFFFF;
            };

            WheelType wheelType = WheelType.DRIFT;
            EngineType engineType = EngineType.ENGINE_GOLD;

            this.setBodyType(bodyType);
            this.setBodyColor(bodyColor);
            this.setWheelColor(wheelColor);
            this.setWheelType(wheelType);
            this.setEngineType(engineType);
        }

        if (nbt.contains("Fuel")) this.setFuelSynced(nbt.getFloat("Fuel"));
        if (nbt.contains("Odo")) this.setOdometerSynced(nbt.getFloat("Odo"));

        if (nbt.contains("HornType")) this.setHornType(HornType.fromId(nbt.getInt("HornType")));
        else if (nbt.contains("HornSound")) this.setHornType(HornType.HORN1); //< update from old horn system
        if (nbt.contains("WheelType")) this.setWheelType(WheelType.fromId(nbt.getInt("WheelType")));
        if (nbt.contains("EngineType")) this.setEngineType(EngineType.fromId(nbt.getInt("EngineType")));
        if (nbt.contains("BodyType")) this.setBodyType(BodyType.fromId(nbt.getInt("BodyType")));
        if (nbt.contains("SpoilerType")) this.setSpoilerType(SpoilerType.fromId(nbt.getInt("SpoilerType")));
        if (nbt.contains("AeroType")) this.setAeroType(AeroType.fromId(nbt.getInt("AeroType")));
        if (nbt.contains("BodyColor")) this.setBodyColor(nbt.getInt("BodyColor"));
        if (nbt.contains("WheelColor")) this.setWheelColor(nbt.getInt("WheelColor"));
        if (nbt.contains("RaceNumber")) this.setRaceNumber(nbt.getInt("RaceNumber"));


        // key
        if (nbt.contains("HasKey")) this.setKeyInIgnition(nbt.getBoolean("HasKey"));
        UUID keyUuid;
        if (nbt.containsUuid("KeyUuid")) {
            keyUuid = nbt.getUuid("KeyUuid");
        } else {
            keyUuid = null;
        }

        if (keyUuid != null) {
            this.setKeyUuid(keyUuid);
        }

        // update position to prevent loading with undefined lastTickX/Y/Z
        this.lastTickX = this.getX();
        this.lastTickY = this.getY();
        this.lastTickZ = this.getZ();

        // update physics
        this.recalculateKartPhysics();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);

        // sync client tracked values with server values
        if(this.getWorld().isClient()) {
            if(FUEL.equals(data)) {
                this.currentFuel = this.getFuelSynced();
            }
            if(ODO.equals(data)) {
                this.currentOdometer = this.getOdometerSynced();
            }

            if(ENGINE_TYPE.equals(data) || WHEEL_TYPE.equals(data) || SPOILER_TYPE.equals(data) || AERO_TYPE.equals(data)) {
                this.recalculateKartPhysics();
            }
        }
    }







    // ==================== getter and setter related to data tracking ====================

    // vanilla
    public void setDamageWobbleTicks(int damageWobbleTicks) {
        this.dataTracker.set(DAMAGE_WOBBLE_TICKS, damageWobbleTicks);
    }
    public void setDamageWobbleSide(int damageWobbleSide) {
        this.dataTracker.set(DAMAGE_WOBBLE_SIDE, damageWobbleSide);
    }
    public void setDamageWobbleStrength(float damageWobbleStrength) {
        this.dataTracker.set(DAMAGE_WOBBLE_STRENGTH, damageWobbleStrength);
    }
    public float getDamageWobbleStrength() {
        return this.dataTracker.get(DAMAGE_WOBBLE_STRENGTH);
    }
    public int getDamageWobbleTicks() {
        return this.dataTracker.get(DAMAGE_WOBBLE_TICKS);
    }
    public int getDamageWobbleSide() {
        return this.dataTracker.get(DAMAGE_WOBBLE_SIDE);
    }

    // horn
    public HornType getHornType() {
        return this.dataTracker.get(HORN_TYPE);
    }
    public void setHornType(HornType type) {
        this.dataTracker.set(HORN_TYPE, type);
    }

    // driving stats
    public Float getFuelSynced() {
        return this.dataTracker.get(FUEL);
    }
    public void setFuelSynced(Float range) {
        if(range < 0.0f) range = 0.0f;
        this.dataTracker.set(FUEL, range);
    }
    public Float getOdometerSynced() {
        return this.dataTracker.get(ODO);
    }
    public void setOdometerSynced(Float odometer) {
        if(odometer < 0.0f) odometer = 0.0f;
        this.dataTracker.set(ODO, odometer);
    }

    // animations
    public byte getPlayerInput() {
        return this.dataTracker.get(PLAYER_INPUT);
    }
    private void setPlayerInput(byte input) {
        this.dataTracker.set(PLAYER_INPUT, input);
    }

    // kartv2 modifications
    public WheelType getWheelType() {
        return this.dataTracker.get(WHEEL_TYPE);
    }
    public void setWheelType(WheelType type) {
        this.dataTracker.set(WHEEL_TYPE, type);
    }
    public EngineType getEngineType() {
        return this.dataTracker.get(ENGINE_TYPE);
    }
    public void setEngineType(EngineType type) {
        this.dataTracker.set(ENGINE_TYPE, type);
    }
    public BodyType getBodyType() {
        return this.dataTracker.get(BODY_TYPE);
    }
    public void setBodyType(BodyType type) {
        this.dataTracker.set(BODY_TYPE, type);
    }
    public SpoilerType getSpoilerType() {
        return this.dataTracker.get(SPOILER_TYPE);
    }
    public void setSpoilerType(SpoilerType type) {
        this.dataTracker.set(SPOILER_TYPE, type);
    }
    public AeroType getAeroType() {
        return this.dataTracker.get(AERO_TYPE);
    }
    public void setAeroType(AeroType type) {
        this.dataTracker.set(AERO_TYPE, type);
    }

    public int getBodyColor() {
        return this.dataTracker.get(BODY_COLOR);
    }
    public void setBodyColor(int type) {
        this.dataTracker.set(BODY_COLOR, type);
    }
    public int getWheelColor() {
        return this.dataTracker.get(WHEEL_COLOR);
    }
    public void setWheelColor(int type) {
        this.dataTracker.set(WHEEL_COLOR, type);
    }
    public int getRaceNumber() {
        return this.dataTracker.get(RACE_NUMBER);
    }
    public void setRaceNumber(int type) {
        this.dataTracker.set(RACE_NUMBER, type);
    }

    // key stuff
    public boolean isKeyInIgnition () { return this.dataTracker.get(HAS_KEY); }
    public void setKeyInIgnition (boolean value) { this.dataTracker.set(HAS_KEY, value); }
    public UUID getKeyUuid() { return this.dataTracker.get(KEY_UUID).orElse(null); }
    public void setKeyUuid(@Nullable UUID uuid) { this.dataTracker.set(KEY_UUID, Optional.ofNullable(uuid)); }
}
