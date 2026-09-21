package de.cxlledjay.bokarts.entity.custom;

import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.config.BoKartsConfig;
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

public class KartEntity extends BoatEntity implements RideableInventory{

    // -------------------- constants --------------------
    private static final float STEERING_SPEED = 20.0f;
    private static final float STEERING_CENTER = 0.0f;
    private static final float ENGINE_ACCELERATION = 0.1f;

    // -------------------- tracked data --------------------
    private static final TrackedData<String> PAINT_COLOR = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> HORN_SOUND = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Float> FUEL = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> ODO = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> ENGINE_REVS = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> STEERING_ANGLE = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.FLOAT);

    // -------------------- attributes --------------------

    // input tracking
    public boolean  pressingLeft, pressingRight, pressingForward, pressingBack, pressingSlow;
    private boolean lastSentLeft, lastSentRight, lastSentForward, lastSentBack, lastSentSlow;

    // physics tracking
    private int ticksInWater = 0;

    // sound tracking
    private boolean engineSoundStarted = false;
    private float soundScaling = 0.0f;

    // range tracking
    public float currentFuel = -1;
    public float currentOdometer = -1;

    // position tracking
    private boolean hasTrackedPosition = false;
    private double lastTickX;
    private double lastTickY;
    private double lastTickZ;

    // animation tracking
    public float frontAxleRotation = 0.0f;
    public float frontAxleRotationPrev = 0.0f;
    public float rearAxleRotation = 0.0f;
    public float rearAxleRotationPrev = 0.0f;
    public float steeringAngle = STEERING_CENTER;
    public float steeringAnglePrev = STEERING_CENTER;
    private float engineRevs = 0.0f;




    // ==================== the kart ====================

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


    // ==================== fixing BoatEntity behaviour ====================

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


    // ==================== kart driving behaviour ====================

    @Override
    public float getNearbySlipperiness() {

        // get block below kart
        BlockState groundBlock = this.getWorld().getBlockState(this.getSteppingPos());

        // calculated ice boat slipperiness
        float slipperiness = BoKartsConfig.slipperinessNoPassenger; //< default: no passenger

        // check if a player is controlling it
        if(hasControllingPassenger() && getControllingPassenger() instanceof PlayerEntity) {
            if(pressingSlow) {
                // player wants to go slow (maneuvering)
                slipperiness = BoKartsConfig.slipperinessSlowed;
            } else {
                // normal driving => check block
                if(isDrivableBlock(groundBlock)) {
                    // on a road
                    slipperiness = BoKartsConfig.slipperinessDriveable;
                } else {
                    // off-road
                    slipperiness = BoKartsConfig.slipperinessNonDriveable;
                }
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
    public void move(MovementType movementType, Vec3d movement) {

        super.move(movementType, movement);

        // physics logic on server!
        if(!this.getWorld().isClient()) {

            if(this.isTouchingWater() || this.isSubmergedInWater()) {
                // touching water!
                if(++this.ticksInWater >= BoKartsConfig.maxTicksInWater) {
                    // spend too long in water => kaboom

                    // spawn explosion
                    this.getWorld().createExplosion(
                            this,     // The entity causing the explosion
                            this.getX(),    // X coordinate
                            this.getY(),    // Y coordinate
                            this.getZ(),    // Z coordinate
                            1.5f,           // Power (TNT is 4.0f, Creeper is 3.0f)
                            World.ExplosionSourceType.NONE // NONE = damages players/entities, but DOES NOT break blocks!
                    );

                    // eject passengers
                    if (this.hasPassengers()) {
                        this.removeAllPassengers();
                    }

                    // kill kart and drop it
                    this.killAndDropItem(this.asItem());

                    // despawn it
                    this.discard();
                }
            } else {
                // not touching water (anymore!)
                this.ticksInWater = 0;
            }
        }
    }

    @Override
    public void fall(double heightDifference, boolean onGround, BlockState state, BlockPos pos) {
        // Reset the fall distance to 0 every tick it falls
        this.fallDistance = 0.0F;

        // Continue with standard entity falling (won't break because distance is 0)
        super.fall(heightDifference, onGround, state, pos);
    }



    // ==================== KartEntity <-> KartItem ====================

    @Override
    public Item asItem() {
        return switch (this.getPaintColor()) {
            case WHITE -> ModItems.KART_WHITE;
            case ORANGE -> ModItems.KART_ORANGE;
            case MAGENTA -> ModItems.KART_MAGENTA;
            case LIGHT_BLUE -> ModItems.KART_LIGHT_BLUE;
            case YELLOW -> ModItems.KART_YELLOW;
            case LIME -> ModItems.KART_LIME;
            case PINK -> ModItems.KART_PINK;
            case GRAY -> ModItems.KART_GRAY;
            case LIGHT_GRAY -> ModItems.KART_LIGHT_GRAY;
            case CYAN -> ModItems.KART_CYAN;
            case PURPLE -> ModItems.KART_PURPLE;
            case BLUE -> ModItems.KART_BLUE;
            case BROWN -> ModItems.KART_BROWN;
            case GREEN -> ModItems.KART_GREEN;
            case RED -> ModItems.KART_RED;
            case BLACK -> ModItems.KART_BLACK;
            case PURPLE_GOLD -> ModItems.KART_PURPLE_GOLD;
            case FADE -> ModItems.KART_FADE;
            default -> ModItems.KART_DEFAULT;
        };
    }

    // set data component types of ItemStack to tracked data
    public ItemStack getKartAsCustomStack() {

        // get correct variant
        ItemStack stack = new ItemStack(this.asItem());

        // sync values
        this.setFuelSynced(this.currentFuel);
        this.setOdometerSynced(this.currentOdometer);

        // set DataComponentTypes
        stack.set(ModDataComponentTypes.KART_ITEM_HORN_SOUND, this.getHornSound().asString());
        stack.set(ModDataComponentTypes.KART_ITEM_FUEL, this.getFuelSynced());
        stack.set(ModDataComponentTypes.KART_ITEM_ODOMETER, this.getOdometerSynced());

        return stack;
    }

    // override creative middle click
    @Override
    public ItemStack getPickBlockStack() {
        return this.getKartAsCustomStack();
    }

    // override dropped item on destroy
    @Override
    public ItemEntity dropStack(ItemStack stack, float yOffset) {
        // Check if the game is trying to drop our base Kart item
        if (stack.isOf(this.asItem())) {
            // Swap out the blank vanilla stack with our custom Component stack!
            stack = this.getKartAsCustomStack();
        }

        // Let the game spawn the item into the world
        return super.dropStack(stack, yOffset);
    }



    // ==================== input handling ====================

    @Override
    public void setInputs(boolean left, boolean right, boolean forward, boolean back) {

        // manipulate reverse physics
        boolean manipulatedLeft = back ? right : left;
        boolean manipulatedRight = back ? left : right;

        // handle inputs in BoatEntity
        super.setInputs(manipulatedLeft, manipulatedRight, forward, back);

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


    // ==================== tick() ====================

    @Override
    public void tick() {

        // no fuel = no drive
        boolean isOutOfFuel = this.currentFuel <= 0.0f;
        if (isOutOfFuel) {
            super.setInputs(false, false, false, false);
        }

        // tick the boat parent
        super.tick();


        // ---------------- position tracking (client + server) ----------------

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
        if (this.currentFuel == -1) this.currentFuel = this.getFuelSynced();
        if (this.currentOdometer == -1) this.currentOdometer = this.getOdometerSynced();

        // update server tracking variables
        if(this.pressingForward || this.pressingBack) {
            // we are accelerating: consume fuel!
            this.currentFuel = Math.max(0, this.currentFuel - BoKartsConfig.fuelConsumptionPerTick); // prevent going under 0
        }
        this.currentOdometer += (float) distanceThisTick;







        // ==================== [SERVER SIDED CODE] ====================
        if (!this.getWorld().isClient()) {

            // ---------------- fuel and odo sync ----------------

            // sync client data with exact data from server, executed every 10 ticks
            if(this.age % 10 == 0) {
                this.setFuelSynced(this.currentFuel);
                this.setOdometerSynced(this.currentOdometer);
            }

            // ---------------- calculate values for animation ----------------

            // engine revs
            calculateEngineRevs();

            // steering angle
            calculateSteeringAngle();

            // update client
            this.setEngineRevs(this.engineRevs);
            this.setSteeringAngle(this.steeringAngle);
        }

        // ==================== [CLIENT SIDED CODE] ====================
        else {

            // for lerping steering angle (still able steer when no fuel!)
            this.steeringAngle = this.getSteeringAngle();
            this.steeringAnglePrev = this.steeringAngle;


            // do no animations or sound when out of fuel!

            if(!isOutOfFuel) {
                // kart speed calculation
                Vec3d velocity = this.getVelocity();
                double yawRad = Math.toRadians(this.getYaw());
                double forwardX = -Math.sin(yawRad);
                double forwardZ = Math.cos(yawRad);
                double forwardSpeed = (velocity.x * forwardX) + (velocity.z * forwardZ);

                // resulting rotation
                float frontAxleRotationThisTick = (float) (forwardSpeed * 5.34);
                float backAxleRotationThisTick = frontAxleRotationThisTick + this.getEngineRevs();



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

                this.setSoundScaling(((float) forwardSpeed * 5) + (Math.abs(this.getEngineRevs()) * 10));

                if (this.getWorld().isClient) {
                    if (this.hasPassengers() && !this.engineSoundStarted) {
                        KartEngineSound.playEngineSound(this);
                        this.engineSoundStarted = true;
                    } else if (!this.hasPassengers() && this.engineSoundStarted) {
                        this.engineSoundStarted = false;
                    }
                }
            } else {
                // wheels not moving
                frontAxleRotation = 0.0f;
                frontAxleRotationPrev = 0.0f;
                rearAxleRotation = 0.0f;
                rearAxleRotationPrev = 0.0f;

                // reset engine sound
                this.engineSoundStarted = false;
            }

        }
    }



    // ==================== tick() helpers ====================

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
        if (pressingForward) {
            // positive acceleration
            this.engineRevs += ENGINE_ACCELERATION;
        } else if (pressingBack) {
            // negative acceleration
            this.engineRevs -= ENGINE_ACCELERATION;
        } else {
            // no button pressed => slow return to idle
            this.engineRevs = applyEngineFlywheel(this.engineRevs);
        }
        this.engineRevs = MathHelper.clamp(this.engineRevs, 0 - (ENGINE_ACCELERATION * 25), 0 + (ENGINE_ACCELERATION * 25));
    }

    private float applyEngineFlywheel(float engineRotationDelta) {
        if(engineRotationDelta < (0 + ENGINE_ACCELERATION * 2) && engineRotationDelta > (0 - ENGINE_ACCELERATION * 2)) {
            engineRotationDelta = 0;
        } else if(engineRotationDelta > 0) {
            engineRotationDelta -= ENGINE_ACCELERATION * 2;
        } else if(engineRotationDelta < 0) {
            engineRotationDelta += ENGINE_ACCELERATION * 2;
        }

        return engineRotationDelta;
    }


    // ==================== sounds ====================

    @Override
    @Nullable
    protected SoundEvent getPaddleSoundEvent() {
        return null;
    }


    public float getSoundScaling() {
        return soundScaling;
    }

    private void setSoundScaling(float soundScaling) {
        this.soundScaling = soundScaling;
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





    // ==================== fuel and odometer ====================

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
            int neededItemsUntilFull = (int) Math.ceil((BoKartsConfig.maxFuelCapacity - this.currentFuel) / fuelProItem);
            int consumedItems = Math.min(neededItemsUntilFull, fuel.getCount());

            // refuel the engine and remove items from inventory
            this.currentFuel = Math.min((this.currentFuel + fuelProItem * consumedItems), BoKartsConfig.maxFuelCapacity);
            fuel.decrement(consumedItems);

            // sync with client
            this.setFuelSynced(this.currentFuel);

            //play sound depending on fuel level and consumed items
            SoundEvent refillSound = SoundEvents.BLOCK_NOTE_BLOCK_BASS.value();
            float pitch = 0.9f;

            if(consumedItems > 0) {
                if(currentFuel >= BoKartsConfig.maxFuelCapacity) {
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
        return decimalFormat1.format(fuelCapacity / 1000.0f) + "L/" + decimalFormat1.format(BoKartsConfig.maxFuelCapacity / 1000.0f) + "L";
    }




    // ==================== data tracking ====================

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        // customization
        builder.add(PAINT_COLOR, "default");
        builder.add(HORN_SOUND, "horn_1");

        // fuel and distance
        builder.add(FUEL, 0.0f);
        builder.add(ODO, 0.0f);

        // animations
        builder.add(ENGINE_REVS, 0.0f);
        builder.add(STEERING_ANGLE, STEERING_CENTER);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("PaintColor", this.getPaintColor().toString());
        nbt.putString("HornSound", this.getHornSound().toString());

        nbt.putFloat("Fuel", this.getFuelSynced());
        nbt.putFloat("Odo", this.getOdometerSynced());

        nbt.putFloat("EngineRevs", this.getEngineRevs());
        nbt.putFloat("SteeringAngle", this.getSteeringAngle());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(PAINT_COLOR, nbt.getString("PaintColor"));
        this.dataTracker.set(HORN_SOUND, nbt.getString("HornSound"));

        this.dataTracker.set(FUEL, nbt.getFloat("Fuel"));
        this.dataTracker.set(ODO, nbt.getFloat("Odo"));

        this.dataTracker.set(ENGINE_REVS, nbt.getFloat("EngineRevs"));
        this.dataTracker.set(STEERING_ANGLE, nbt.getFloat("SteeringAngle"));
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);

        // sync client tracked values with server values
        if (this.getWorld().isClient()) {
            if (FUEL.equals(data)) {
                this.currentFuel = this.getFuelSynced();
            }
            if (ODO.equals(data)) {
                this.currentOdometer = this.getOdometerSynced();
            }
        }
    }



    // ==================== getter and setter related to data tracking ====================

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



    public Float getEngineRevs() {
        return this.dataTracker.get(ENGINE_REVS);
    }

    private void setEngineRevs(Float acceleration) {
        this.dataTracker.set(ENGINE_REVS, acceleration);
    }

    public Float getSteeringAngle() {
        return this.dataTracker.get(STEERING_ANGLE);
    }

    private void setSteeringAngle(Float speed) {
        this.dataTracker.set(STEERING_ANGLE, speed);
    }




    // ==================== customization ====================

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
        BLACK       ("black"),

        // custom variants
        PURPLE_GOLD("purple_gold"),
        FADE("fade");

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
