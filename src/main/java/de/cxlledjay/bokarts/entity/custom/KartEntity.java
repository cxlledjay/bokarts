package de.cxlledjay.bokarts.entity.custom;

import de.cxlledjay.bokarts.BoKarts;
import de.cxlledjay.bokarts.entity.ModEntities;
import de.cxlledjay.bokarts.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class KartEntity extends BoatEntity {


    public boolean pressingLeft, pressingRight, pressingForward, pressingBack, pressingSpace;

    private static final float steeringSpeed = 20.0f;
    private static final float steeringCenter = 0.0f;
    private static final float engineAcceleration = 0.1f;

    private static final TrackedData<String> PAINT_COLOR = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.STRING);
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
        return 0.98F;
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
            case DEBUG:
                dropItem = Items.FURNACE;
                break;
        }

        return dropItem;
    }



    // animations

    @Override
    public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack) {
        super.setInputs(pressingLeft, pressingRight, pressingForward, pressingBack);
        // steal vanilla inputs
        this.pressingLeft = pressingLeft;
        this.pressingRight = pressingRight;
        this.pressingForward = pressingForward;
        this.pressingBack = pressingBack;
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
        super.tick();

        // handle animations

        // ---------------- steering ----------------
        float newAngle = this.getSteeringAngle();
        if(!(pressingLeft && pressingRight)) {
            if(pressingLeft) {
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
        newAngle = MathHelper.clamp(newAngle, steeringCenter-(steeringSpeed*5), steeringCenter+(steeringSpeed*5));
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
        if(pressingForward) {
            // positive acceleration
            currentRevs += engineAcceleration;
        } else if (pressingBack) {
            // negative acceleration
            currentRevs -= engineAcceleration;
        } else {
            // no button pressed => slow return to idle
            currentRevs = applyEngineFlywheel(currentRevs);
        }
        currentRevs = MathHelper.clamp(currentRevs, 0-(engineAcceleration*25), 0+(engineAcceleration*25));
        this.setEngineRevs(currentRevs);

        // ---------------- wheel spin back ----------------
        this.setEngineRotationPrev(this.getEngineRotation());
        float backAxleRotationThisTick = radiansThisTick + currentRevs;
        this.setEngineRotation(this.getEngineRotation() + backAxleRotationThisTick);
    }

    // attributes
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PAINT_COLOR, "default");
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
        this.dataTracker.set(ENGINE_REVS, nbt.getFloat("EngineRevs"));
        this.dataTracker.set(WHEEL_ROTATION, nbt.getFloat("WheelRotation"));
        this.dataTracker.set(WHEEL_ROTATION_PREV, nbt.getFloat("WheelRotationPrev"));
        this.dataTracker.set(ENGINE_ROTATION, nbt.getFloat("EngineRotation"));
        this.dataTracker.set(ENGINE_ROTATION_PREV, nbt.getFloat("EngineRotationPrev"));
        this.dataTracker.set(STEERING_ANGLE, nbt.getFloat("SteeringAngle"));
    }

    // getter and setter for attributes

    public PaintColor getPaintColor() {
        return PaintColor.getColor(this.dataTracker.get(PAINT_COLOR));
    }

    public void setPaintColor(PaintColor paintColor) {
        this.dataTracker.set(PAINT_COLOR, paintColor.toString());
    }


    public Float getEngineRevs() {
        return this.dataTracker.get(ENGINE_REVS);
    }

    public void setEngineRevs(Float acceleration) {
        this.dataTracker.set(ENGINE_REVS, acceleration);
    }


    public Float getWheelRotation() {
        return this.dataTracker.get(WHEEL_ROTATION);
    }

    public void setWheelRotation(Float rotation) {
        this.dataTracker.set(WHEEL_ROTATION, rotation);
    }

    public Float getWheelRotationPrev() {
        return this.dataTracker.get(WHEEL_ROTATION_PREV);
    }

    public void setWheelRotationPrev(Float rotation) {
        this.dataTracker.set(WHEEL_ROTATION_PREV, rotation);
    }


    public Float getEngineRotation() {
        return this.dataTracker.get(ENGINE_ROTATION);
    }

    public void setEngineRotation(Float rotation) {
        this.dataTracker.set(ENGINE_ROTATION, rotation);
    }

    public Float getEngineRotationPrev() {
        return this.dataTracker.get(ENGINE_ROTATION_PREV);
    }

    public void setEngineRotationPrev(Float rotation) {
        this.dataTracker.set(ENGINE_ROTATION_PREV, rotation);
    }


    public Float getSteeringAngle() {
        return this.dataTracker.get(STEERING_ANGLE);
    }

    public void setSteeringAngle(Float speed) {
        this.dataTracker.set(STEERING_ANGLE, speed);
    }








    // variants
    public enum PaintColor implements StringIdentifiable {
        DEFAULT     ("default"),
        DEBUG       ("debug"),

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

        public String getName() {
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

}
