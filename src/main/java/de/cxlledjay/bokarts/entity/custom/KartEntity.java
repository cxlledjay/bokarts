package de.cxlledjay.bokarts.entity.custom;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class KartEntity extends BoatEntity {


    public boolean pressingLeft, pressingRight, pressingForward, pressingBack, pressingSpace;
    private static final TrackedData<String> PAINT_COLOR = DataTracker.registerData(KartEntity.class, TrackedDataHandlerRegistry.STRING);


    public KartEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
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
        // If you have created a custom BoKart item, return it here.
        // If you haven't made an item for it yet, return Items.AIR so it drops nothing.
        return Items.FURNACE;
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






    // attributes
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PAINT_COLOR, "default");
    }

    public PaintColor getPaintColor() {
        return PaintColor.getColor(this.dataTracker.get(PAINT_COLOR));
    }

    public void setPaintColor(PaintColor paintColor) {
        this.dataTracker.set(PAINT_COLOR, paintColor.toString());
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("PaintColor", this.getPaintColor().toString());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(PAINT_COLOR, nbt.getString("PaintColor"));
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
