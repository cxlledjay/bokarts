package de.cxlledjay.bokarts.entity.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.networking.packet.KartInputPayloadC2S;
import de.cxlledjay.bokarts.screen.custom.KartInventoryScreenHandler;
import de.cxlledjay.bokarts.sound.KartEngineSound;
import de.cxlledjay.bokarts.sound.ModSounds;
import de.cxlledjay.bokarts.util.KartFuelItems;
import de.cxlledjay.bokarts.util.ModTags;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Objects;

public class KartEntity extends BoatEntity implements RideableInventory{

    // input tracking
    public boolean  pressingLeft, pressingRight, pressingForward, pressingBack, pressingSlow;
    private boolean lastSentLeft, lastSentRight, lastSentForward, lastSentBack, lastSentSlow;

    // animation stuff
    private static final float steeringSpeed = 20.0f;
    private static final float steeringCenter = 0.0f;
    private static final float engineAcceleration = 0.1f;

    // sound stuff
    private boolean engineSoundStarted = false;
    private float soundScaling = 0.0f;

    // range stuff
    public static final float fuelTankMaxCapacity = 10000.0f; // in ml
    private static final float fuelConsumptionPerTick = 10.5625f; // in ml
    private float serverTrackedFuel = -1;
    private float serverTrackedOdometer = -1;
    public float clientTrackedFuel = -1;
    public float clientTrackedOdometer = -1;

    // position tracking helper
    private boolean hasTrackedPosition = false;
    private double lastTickX;
    private double lastTickY;
    private double lastTickZ;

    // attributes
    private static final TrackedData<String> PAINT_COLOR = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> HORN_SOUND = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.STRING);

    private static final TrackedData<Float> FUEL = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> ODO = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> PRESSING_FORWARDS = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // private static final TrackedData<Boolean> PRESSING_BACKWARDS = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    // animation tracking TODO: revert data tracker and do client only + steering via BoatEntity tracked data
    private static final float clientTrackingEngineRevs = 0.0f;
    private static final float clientTrackingWheelRotation = 0.0f;
    private static final float clientTrackingWheelRotationPrev = 0.0f;
    private static final float clientTrackingEngineRotation = 0.0f;
    private static final float clientTrackingEngineRotationPrev = 0.0f;
    private static final float clientTrackingSteeringAngle = 0.0f;
    private static final TrackedData<Float> ENGINE_REVS = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> WHEEL_ROTATION = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> WHEEL_ROTATION_PREV = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> ENGINE_ROTATION = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> ENGINE_ROTATION_PREV = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> STEERING_ANGLE = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);


    public KartEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public KartEntity(World world, double x, double y, double z) {
        this(ModEntities.KART_ENTITY_TYPE, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    // fix driver position and number
    @Override
    protected void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater) {
        if (this.hasPassenger(passenger)) {
            // 1. Define your local offset: X (left/right), Y (height), Z (forward/backward)
            // Adjust these values to align exactly with your seat
            Vec3d localOffset = new Vec3d(0.0, -0.42, -0.3);

            // 2. Convert the kart's yaw to radians and rotate the offset
            // Minecraft yaw is inverted, so we use negative yaw
            float yaw = this.getYaw();
            Vec3d rotatedOffset = localOffset.rotateY(-yaw * ((float)Math.PI / 180F));

            // 3. Apply the rotated offset to the kart's true world position
            positionUpdater.accept(
                    passenger,
                    this.getX() + rotatedOffset.x,
                    this.getY() + localOffset.y,
                    this.getZ() + rotatedOffset.z
            );

            // 1. Calculate how much the kart rotated since the last tick
            float deltaYaw = this.getYaw() - this.prevYaw;

            // 2. Add that rotation to the passenger's current camera angles
            passenger.setYaw(passenger.getYaw() + deltaYaw);
            passenger.setHeadYaw(passenger.getHeadYaw() + deltaYaw);

            // 3. Keep the torso facing strictly forward
            passenger.setBodyYaw(this.getYaw());
        } else {
            super.updatePassengerPosition(passenger, positionUpdater);
        }
    }

    @Override
    protected int getMaxPassengers() {
        return 1;
    }



    // kart physics

    @Override
    public float getNearbySlipperiness() {

        float slipperiness = 0.6f; //< default boat
        BlockState groundBlock = this.getWorld().getBlockState(this.getSteppingPos());

        if(hasControllingPassenger() && getControllingPassenger() instanceof PlayerEntity) {
            if(isDrivableBlock(groundBlock)) {
                // is on road => go brr
                slipperiness = 0.98f;
            } else {
                slipperiness = 0.9f;
            }
        }

        return slipperiness;
    }

    private boolean isDrivableBlock(BlockState blockState) {
        return blockState.isIn(ModTags.Blocks.DRIVABLE_BLOCKS);
    }

    @Override
    public float getStepHeight() {
        return 0.75F;
    }

    @Override
    public boolean isPushable() {
        return false;
    }


    @Override
    public void fall(double heightDifference, boolean onGround, BlockState state, BlockPos pos) {
        // Reset the fall distance to 0 every tick it falls
        this.fallDistance = 0.0F;

        // Continue with standard entity falling (won't break because distance is 0)
        super.fall(heightDifference, onGround, state, pos);
    }



    // item stuff

    @Override
    public Item asItem() {
        Item dropItem = ModItems.KART_DEFAULT;

        switch(this.getPaintColor()) {
            case GREEN:
                dropItem = Items.FURNACE;
                break;
        }

        return dropItem;
    }



    // animations

    @Override
    public void setInputs(boolean left, boolean right, boolean forward, boolean back) {
        // handle inputs in super class
        super.setInputs(left, right, forward, back);

        if (this.getWorld().isClient()) {

            // steal inputs
            this.pressingLeft = left;
            this.pressingRight = right;
            this.pressingForward = forward;
            this.pressingBack = back;

            // steal space key
            Entity driver = this.getControllingPassenger();
            if (driver instanceof net.minecraft.client.network.ClientPlayerEntity clientPlayer) {
                this.pressingSlow = clientPlayer.input.jumping;
            }

            // check if inputs changed
            if (this.pressingForward != lastSentForward || this.pressingBack != lastSentBack ||
                    this.pressingLeft != lastSentLeft || this.pressingRight != lastSentRight || this.pressingSlow != lastSentSlow) {


                // send new input to server
                ClientPlayNetworking.send(new KartInputPayloadC2S(
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
    }



    private float applySteeringWheelCenterSpring(float angle) {
        if(angle < (steeringCenter + steeringSpeed) && angle > (steeringCenter - steeringSpeed)) {
            angle = steeringCenter;
        } else if(angle > steeringCenter) {
            angle -= steeringSpeed;
        } else if(angle < steeringCenter) {
            angle += steeringSpeed;
        }

        return angle;
    }

    private float applyEngineFlywheel(float engineRotationDelta) {
        if(engineRotationDelta < (0 + engineAcceleration * 2) && engineRotationDelta > (0 - engineAcceleration * 2)) {
            engineRotationDelta = 0;
        } else if(engineRotationDelta > 0) {
            engineRotationDelta -= engineAcceleration * 2;
        } else if(engineRotationDelta < 0) {
            engineRotationDelta += engineAcceleration * 2;
        }

        return engineRotationDelta;
    }

    @Override
    public void tick() {

        // no fuel = no drive
        boolean isOutOfFuel = this.getWorld().isClient()
                ? (this.clientTrackedFuel <= 0)
                : (this.serverTrackedFuel <= 0);
        if (isOutOfFuel) {
            super.setInputs(false, false, false, false);
        }

        // tick the boat parent
        super.tick();

        // positon tracking on both client and server
        if (!this.hasTrackedPosition) {
            this.lastTickX = this.getX();
            this.lastTickY = this.getY();
            this.lastTickZ = this.getZ();
            this.hasTrackedPosition = true;
        }
        double dx = this.getX() - this.lastTickX;
        double dy = this.getY() - this.lastTickY;
        double dz = this.getZ() - this.lastTickZ;
        double distanceThisTick = Math.sqrt(dx * dx + dy * dy + dz * dz);

        // update position tracking
        this.lastTickX = this.getX();
        this.lastTickY = this.getY();
        this.lastTickZ = this.getZ();

        // position tracking code
        if(!this.getWorld().isClient()) {
            // on server...

            // init server tracking
            if (this.serverTrackedFuel == -1) this.serverTrackedFuel = this.getFuelSynced();
            if (this.serverTrackedOdometer == -1) this.serverTrackedOdometer = this.getOdometerSynced();

            // update server tracking variables
            if(getPressingForwardsSynced()) {
                // we are accelerating: consume fuel!
                this.serverTrackedFuel = Math.max(0, this.serverTrackedFuel - fuelConsumptionPerTick); // prevent going under 0
            }
            this.serverTrackedOdometer += (float) distanceThisTick;
        } else {
            // on client...

            // init client tracking
            if (this.clientTrackedFuel == -1) this.clientTrackedFuel = this.getFuelSynced();
            if (this.clientTrackedOdometer == -1) this.clientTrackedOdometer = this.getOdometerSynced();

            // update client tracking variables
            if(getPressingForwardsSynced()) {
                // we are accelerating: consume fuel!
                this.clientTrackedFuel = Math.max(0, this.clientTrackedFuel - fuelConsumptionPerTick); // prevent going under 0
            }
            this.clientTrackedOdometer += (float) distanceThisTick;
        }


        // ==================== [SERVER SIDED CODE] ====================
        if (!this.getWorld().isClient()) {
            // sync client data with exact data from server, executed every 10 ticks
            if(this.age % 10 == 0) {
                this.setFuelSynced(this.serverTrackedFuel);
                this.setOdometerSynced(this.serverTrackedOdometer);
            }

            // input handling
            if(this.hasControllingPassenger()) {
                setPressingForwardSynced(pressingForward);
                // set backwards movement too!
            }
        }

        // ==================== [CLIENT SIDED CODE] ====================
        else {
            // handle animations

            // ---------------- steering ----------------
            float newAngle = this.getSteeringAngle();
            if (!(pressingLeft && pressingRight)) {
                if (pressingLeft) {
                    // steering to the left
                    newAngle += steeringSpeed;
                } else if (pressingRight) {
                    // steering to the right
                    newAngle -= steeringSpeed;
                } else {
                    // no button pressed => center spring
                    newAngle = applySteeringWheelCenterSpring(newAngle);
                }
            } else {
                // pressing left & right => center spring
                newAngle = applySteeringWheelCenterSpring(newAngle);
            }
            // clamp steering to 0 - 180 degs
            newAngle = MathHelper.clamp(newAngle, steeringCenter - (steeringSpeed * 5), steeringCenter + (steeringSpeed * 5));
            // write to data tracker
            this.setSteeringAngle(newAngle);

            // ---------------- wheel spin front ----------------
            this.setWheelRotationPrev(this.getWheelRotation());

            Vec3d velocity = this.getVelocity();
            double yawRad = Math.toRadians(this.getYaw());
            double forwardX = -Math.sin(yawRad);
            double forwardZ = Math.cos(yawRad);
            double forwardSpeed = (velocity.x * forwardX) + (velocity.z * forwardZ);
            float radiansThisTick = (float) (forwardSpeed * 5.34);
            this.setWheelRotation(this.getWheelRotation() + radiansThisTick);

            // ---------------- engine revs ----------------
            float currentRevs = this.getEngineRevs();
            if (pressingForward) {
                // positive acceleration
                currentRevs += engineAcceleration;
            } else if (pressingBack) {
                // negative acceleration
                currentRevs -= engineAcceleration;
            } else {
                // no button pressed => slow return to idle
                currentRevs = applyEngineFlywheel(currentRevs);
            }
            currentRevs = MathHelper.clamp(currentRevs, 0 - (engineAcceleration * 25), 0 + (engineAcceleration * 25));
            this.setEngineRevs(currentRevs);

            // ---------------- wheel spin back ----------------
            this.setEngineRotationPrev(this.getEngineRotation());
            float backAxleRotationThisTick = radiansThisTick + currentRevs;
            this.setEngineRotation(this.getEngineRotation() + backAxleRotationThisTick);


            // ---------------- sounds ----------------
            this.setSoundScaling(((float) forwardSpeed * 5) + (Math.abs(currentRevs) * 10));

            if (this.getWorld().isClient) {
                if (this.hasPassengers() && !this.engineSoundStarted) {
                    KartEngineSound.playEngineSound(this);
                    this.engineSoundStarted = true;
                } else if (!this.hasPassengers() && this.engineSoundStarted) {
                    this.engineSoundStarted = false;
                }
            }

        }

    }



    // ---------------- sounds ----------------

    @Override
    @Nullable
    protected SoundEvent getPaddleSoundEvent() {
        return null;
    }


    // horn
    public void playHornSound() {
        this.getWorld().playSound(
                null, // Excluded player (null = everyone hears it)
                this.getX(), this.getY(), this.getZ(),
                this.getHornSound().getSoundEvent(),
                SoundCategory.PLAYERS,
                0.8f, // Volume
                1.0f  // Pitch
        );
    }

    public void cycleHornSound(ServerPlayerEntity player, int direction) {

        // calculate HornSound
        KartEntity.HornSounds newSound;
        if(direction > 0) {
            // next
            newSound = this.getHornSound().next();
        } else {
            // previous
            newSound = this.getHornSound().previous();
        }

        // preview HornSound
        Identifier oldSoundId = this.getHornSound().getSoundEvent().getId();
        player.networkHandler.sendPacket(new StopSoundS2CPacket(oldSoundId, SoundCategory.PLAYERS));
        player.playSoundToPlayer(newSound.getSoundEvent(), SoundCategory.PLAYERS, 1.0f, 1.0f);

        // set HornSound for Kart
        this.setHornSound(newSound);
    }





    // ---------------- fuel ----------------

    @Override
    public void openInventory(PlayerEntity player) {
        if(!this.getWorld().isClient) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, playerInventory, player1) ->
                            new KartInventoryScreenHandler(syncId, playerInventory), Text.translatable(this.asItem().getTranslationKey())
            ));
        }
    }

    public void addFuelFromItem(ItemStack fuel) {

        // we are on the server here. the if-clause is just for making sure...
        if(!this.getWorld().isClient()) {

            if(!fuel.isEmpty()) {

                // calculate fuel consumption
                float fuelProItem = KartFuelItems.getFuelAmountFromItemStack(fuel);
                int neededItemsUntilFull = (int) Math.floor((fuelTankMaxCapacity - this.serverTrackedFuel) / fuelProItem);
                int consumedItems = Math.min(neededItemsUntilFull, fuel.getCount());

                // refuel the engine and remove items from inventory
                this.serverTrackedFuel += fuelProItem * consumedItems;
                fuel.decrement(consumedItems);

                // sync with client
                this.setFuelSynced(this.serverTrackedFuel);
            }
        }
    }

    public String getFormattedDistanceString(Float distance) {
        String  rangeString = "";
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

    public String getFormattedFuelCapacityString(Float fuelCapacity) {
        DecimalFormat decimalFormat1 = new DecimalFormat("0.0");
        return decimalFormat1.format(fuelCapacity / 1000.0f) + "L/" + decimalFormat1.format(fuelTankMaxCapacity / 1000.0f) + "L";
    }




    // attributes
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        // customization
        builder.add(PAINT_COLOR, "default");
        builder.add(HORN_SOUND, "horn_1");

        // fuel and distance
        builder.add(FUEL, 0.0f);
        builder.add(ODO, 0.0f);
        builder.add(PRESSING_FORWARDS, false);

        // animations
        builder.add(ENGINE_REVS, 0.0f);
        builder.add(WHEEL_ROTATION, 0.0f);
        builder.add(WHEEL_ROTATION_PREV, 0.0f);
        builder.add(ENGINE_ROTATION, 0.0f);
        builder.add(ENGINE_ROTATION_PREV, 0.0f);
        builder.add(STEERING_ANGLE, steeringCenter);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putString("PaintColor", this.getPaintColor().toString());
        nbt.putString("HornSound", this.getHornSound().toString());

        nbt.putFloat("Fuel", this.getFuelSynced());
        nbt.putFloat("Odo", this.getOdometerSynced());
        nbt.putBoolean("PressingForwards", this.getPressingForwardsSynced());

        nbt.putFloat("EngineRevs", this.getEngineRevs());
        nbt.putFloat("WheelRotation", this.getWheelRotation());
        nbt.putFloat("WheelRotationPrev", this.getWheelRotationPrev());
        nbt.putFloat("EngineRotation", this.getEngineRotation());
        nbt.putFloat("EngineRotationPrev", this.getEngineRotationPrev());
        nbt.putFloat("SteeringAngle", this.getSteeringAngle());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(PAINT_COLOR, nbt.getString("PaintColor"));
        this.dataTracker.set(HORN_SOUND, nbt.getString("HornSound"));

        this.dataTracker.set(FUEL, nbt.getFloat("Fuel"));
        this.dataTracker.set(ODO, nbt.getFloat("Odo"));
        this.dataTracker.set(PRESSING_FORWARDS, nbt.getBoolean("PressingForwards"));

        this.dataTracker.set(ENGINE_REVS, nbt.getFloat("EngineRevs"));
        this.dataTracker.set(WHEEL_ROTATION, nbt.getFloat("WheelRotation"));
        this.dataTracker.set(WHEEL_ROTATION_PREV, nbt.getFloat("WheelRotationPrev"));
        this.dataTracker.set(ENGINE_ROTATION, nbt.getFloat("EngineRotation"));
        this.dataTracker.set(ENGINE_ROTATION_PREV, nbt.getFloat("EngineRotationPrev"));
        this.dataTracker.set(STEERING_ANGLE, nbt.getFloat("SteeringAngle"));
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);

        // sync client tracked values with server values
        if (this.getWorld().isClient()) {
            if (FUEL.equals(data)) {
                this.clientTrackedFuel = this.getFuelSynced();
            }
            if (ODO.equals(data)) {
                this.clientTrackedOdometer = this.getOdometerSynced();
            }
        }
    }

    // getter and setter for attributes

    public PaintColor getPaintColor() {
        return PaintColor.getColor(this.dataTracker.get(PAINT_COLOR));
    }

    public void setPaintColor(PaintColor paintColor) {
        this.dataTracker.set(PAINT_COLOR, paintColor.toString());
    }

    public HornSounds getHornSound() {
        return HornSounds.getHornSound(this.dataTracker.get(HORN_SOUND));
    }

    public void setHornSound(HornSounds hornSounds) {
        this.dataTracker.set(HORN_SOUND, hornSounds.toString());
    }





    public Float getFuelSynced() {
        return this.dataTracker.get(FUEL);
    }

    private void setFuelSynced(Float range) {
        if(range < 0.0f) range = 0.0f;
        this.dataTracker.set(FUEL, range);
    }

    public Float getOdometerSynced() {
        return this.dataTracker.get(ODO);
    }

    private void setOdometerSynced(Float odometer) {
        if(odometer < 0.0f) odometer = 0.0f;
        this.dataTracker.set(ODO, odometer);
    }

    public boolean getPressingForwardsSynced() {
        return this.dataTracker.get(PRESSING_FORWARDS);
    }

    private void setPressingForwardSynced(boolean pressingForwards) {
        this.dataTracker.set(PRESSING_FORWARDS, pressingForwards);
    }






    public Float getEngineRevs() {
        return this.dataTracker.get(ENGINE_REVS);
    }

    private void setEngineRevs(Float acceleration) {
        this.dataTracker.set(ENGINE_REVS, acceleration);
    }


    public Float getWheelRotation() {
        return this.dataTracker.get(WHEEL_ROTATION);
    }

    private void setWheelRotation(Float rotation) {
        this.dataTracker.set(WHEEL_ROTATION, rotation);
    }

    public Float getWheelRotationPrev() {
        return this.dataTracker.get(WHEEL_ROTATION_PREV);
    }

    private void setWheelRotationPrev(Float rotation) {
        this.dataTracker.set(WHEEL_ROTATION_PREV, rotation);
    }


    public Float getEngineRotation() {
        return this.dataTracker.get(ENGINE_ROTATION);
    }

    private void setEngineRotation(Float rotation) {
        this.dataTracker.set(ENGINE_ROTATION, rotation);
    }

    public Float getEngineRotationPrev() {
        return this.dataTracker.get(ENGINE_ROTATION_PREV);
    }

    private void setEngineRotationPrev(Float rotation) {
        this.dataTracker.set(ENGINE_ROTATION_PREV, rotation);
    }


    public Float getSteeringAngle() {
        return this.dataTracker.get(STEERING_ANGLE);
    }

    private void setSteeringAngle(Float speed) {
        this.dataTracker.set(STEERING_ANGLE, speed);
    }

    public float getSoundScaling() {
        return soundScaling;
    }

    private void setSoundScaling(float soundScaling) {
        this.soundScaling = soundScaling;
    }



    // variants
    public enum PaintColor implements StringIdentifiable {
        DEFAULT     ("default"),

        // vanilla dye colors
        WHITE       ("white"),
        ORANGE      ("orange"),
        MAGENTA     ("magenta"),
        LIGHT_BLUE  ("light_blue"),
        YELLOW      ("yellow"),
        LIME        ("lime"),
        PINK        ("pink"),
        GRAY        ("gray"),
        LIGHT_GRAY  ("light_gray"),
        CYAN        ("cyan"),
        PURPLE      ("purple"),
        BLUE        ("blue"),
        BROWN       ("brown"),
        GREEN       ("green"),
        RED         ("red"),
        BLACK       ("black");

        private final String name;

        public static final StringIdentifiable.EnumCodec<KartEntity.PaintColor> CODEC = StringIdentifiable.createCodec(KartEntity.PaintColor::values);

        PaintColor(final String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static KartEntity.PaintColor getColor(String name) {
            return CODEC.byId(name, DEFAULT);
        }
    }


    // horn sound effects
    public enum HornSounds implements StringIdentifiable {
        HORN1("horn_1", ModSounds.HORN_CIVIC),
        HORN2("horn_2", ModSounds.HORN_MINI),
        HORN3("horn_3", ModSounds.HORN_BIKE),
        VILLAGER("villager", SoundEvents.ENTITY_VILLAGER_HURT),
        METAL_PIPE("metal_pipe", ModSounds.HORN_METAL_PIPE),
        DISCORD_JOIN("discord_join", ModSounds.HORN_DISCORD_JOIN),
        DISCORD_LEAVE("discord_leave", ModSounds.HORN_DISCORD_LEAVE),
        AUGHH("aughh", ModSounds.HORN_AUGHH),
        RIZZ("rizz", ModSounds.HORN_RIZZ),
        YODA("yoda", ModSounds.HORN_YODA);


        private final String name;
        private final SoundEvent soundEvent;
        public static final StringIdentifiable.EnumCodec<KartEntity.HornSounds> CODEC = StringIdentifiable.createCodec(KartEntity.HornSounds::values);

        HornSounds(final String name, SoundEvent soundEvent) {
            this.name = name;
            this.soundEvent = soundEvent;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static KartEntity.HornSounds getHornSound(String name) {
            return CODEC.byId(name, HORN1);
        }

        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }

        // next and previous select logic
        private static final HornSounds[] vals = values();

        public HornSounds next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }

        public HornSounds previous() {
            return vals[((this.ordinal() + vals.length) - 1) % vals.length];
        }
    }
}
