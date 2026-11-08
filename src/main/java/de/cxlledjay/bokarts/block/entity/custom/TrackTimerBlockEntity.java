package de.cxlledjay.bokarts.block.entity.custom;

import de.cxlledjay.bokarts.block.ModBlocks;
import de.cxlledjay.bokarts.block.custom.TrackTimer;
import de.cxlledjay.bokarts.block.entity.ModBlockEntities;
import de.cxlledjay.bokarts.block.entity.custom.race.LeaderboardEntry;
import de.cxlledjay.bokarts.block.entity.custom.race.ParticipantInfo;
import de.cxlledjay.bokarts.component.ModDataComponentTypes;
import de.cxlledjay.bokarts.entity.custom.kart.KartEntity;
import de.cxlledjay.bokarts.item.ModItems;
import de.cxlledjay.bokarts.item.custom.TrackReport;
import de.cxlledjay.bokarts.networking.packet.LeaderboardResetS2C;
import de.cxlledjay.bokarts.networking.packet.LeaderboardUpdateEntriesS2C;
import de.cxlledjay.bokarts.networking.packet.LeaderboardUpdatePersonalS2C;
import de.cxlledjay.bokarts.screen.custom.TrackTimerScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TrackTimerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, SidedInventory {



    // mode
    public enum TrackTimerMode {
        TIME_ATTACK, GRAND_PRIX
    }

    // ---------- constants ----------
    private static final int MAX_LAPS = 1000;
    private static final int BOUNDING_BOX_RADIUS = 8;
    private static final int MAX_TRACK_WIDTH = 32;

    // hopper slot mappings
    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final int[] SIDE_SLOTS = new int[]{};




    // ---------- local dynamic values ----------

    // modify by only one player
    public PlayerEntity currentUser = null;

    // bounding box (used to search each tick for karts inside of it)
    private Box boundingBox = null;
    private Direction drivingDirection = Direction.NORTH;
    private final Map<UUID, Vec3d> previousKartPosition = new HashMap<>();

    // dynamic leaderboard calculation
    private List<Map.Entry<UUID, ParticipantInfo>> dynamic_leaderboard = new ArrayList<>();




    // ---------- persisted data ----------

    // the inventory
    public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);

    // settings by user
    private TrackTimerMode settingCurrentMode = TrackTimerMode.TIME_ATTACK;
    private int settingLapCount = 1;
    // where is the reflector => calculate bounding box from it each time it loads.
    private int trackWidth = 0;

    // --- race stats
    private boolean hasStarted = false;
    long trackRecordLapTime = 0;
    long raceStartTimestamp = 0;

    // race participant stats
    Map<UUID, ParticipantInfo> participantInfo = new HashMap<>();



    public TrackTimerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TRACK_TIMER_BE, pos, state);
    }



    // -------------------- racing business logic --------------------


    private void initiateParticipation(long timestamp, PlayerEntity player, KartEntity kart) {

        // init stat tracking data
        ParticipantInfo newStats = new ParticipantInfo(timestamp, 0, 0, false);
        this.participantInfo.put(player.getUuid(), newStats);

        // duration tracking
        if(this.raceStartTimestamp == 0) this.raceStartTimestamp = timestamp;

        // send message to player
        Text msg = null;
        if(this.settingCurrentMode == TrackTimerMode.TIME_ATTACK) {
            msg = getPrefix().append(Text.literal("Time tracking started!").formatted(Formatting.WHITE));
        } else if(this.settingCurrentMode == TrackTimerMode.GRAND_PRIX) {
            msg = getPrefix().append(Text.literal("Race tracking started! Remaining Laps: " + this.settingLapCount).formatted(Formatting.WHITE));

            if (player instanceof ServerPlayerEntity serverPlayer) {

                // reset leaderboard
                LeaderboardResetS2C leaderboardResetS2C = new LeaderboardResetS2C();
                ServerPlayNetworking.send(serverPlayer, leaderboardResetS2C);

                // init personal data
                LeaderboardUpdatePersonalS2C leaderboardUpdatePersonalS2C = new LeaderboardUpdatePersonalS2C(
                        player.getNameForScoreboard() + " (" + String.format("%02d", kart.getRaceNumber()) + ")",
                        -1,
                        -1,
                        false,
                        0,
                        this.settingLapCount
                );
                ServerPlayNetworking.send(serverPlayer, leaderboardUpdatePersonalS2C);

                // init leaderboard data
                this.calculateLeaderboard();
                this.broadcastLeaderboardUpdate();
            }
        }
        if(msg != null) player.sendMessage(msg);

        // and sync (notify writing to nbt)
        this.markDirty();
    }


    // ----- leaderboard calculation -----
    private void calculateLeaderboard() {

        // create new list
        this.dynamic_leaderboard = new ArrayList<>(this.participantInfo.entrySet());

        // sort the leaderboard
        this.dynamic_leaderboard.sort((entry1, entry2) -> {
            ParticipantInfo stats1 = entry1.getValue();
            ParticipantInfo stats2 = entry2.getValue();

            // 1) compare hasFinished (descending)
            int finishedCompare = Boolean.compare(stats2.finished(), stats1.finished());
            if (finishedCompare != 0) {
                return finishedCompare;
            }

            // 2) compare laps (descending)
            int lapsCompare = Integer.compare(stats2.laps(), stats1.laps());
            if (lapsCompare != 0) {
                return lapsCompare;
            }

            // 3) compare timestamps (ascending)
            return Long.compare(stats1.lastSeenTimestamp(), stats2.lastSeenTimestamp());
        });
    }

    private void broadcastLeaderboardUpdate() {

        // create leaderboard entries
        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        ParticipantInfo firstPlace = this.dynamic_leaderboard.getFirst().getValue();
        for(Map.Entry<UUID, ParticipantInfo> entry : this.dynamic_leaderboard) {

            // get data
            PlayerEntity p = this.world.getPlayerByUuid(entry.getKey());
            ParticipantInfo me =  entry.getValue();
            KartEntity k = null;
            if(p != null && p.hasVehicle() && p.getVehicle() instanceof KartEntity) {
                k =  (KartEntity) p.getVehicle();
            }

            // display name in format: ABC(12). ABC from the player name (first 3 letters) plus kart nr
            String name = "N/A(--)";
            if(p != null) name = p.getNameForScoreboard().toUpperCase(Locale.ROOT)
                    .substring(0, Math.min(p.getNameForScoreboard().toUpperCase(Locale.ROOT).length(), 3));
            if(k != null) name += "(" + String.format("%02d", k.getRaceNumber()) + ")";

            // distance to first
            long delta = me.lastSeenTimestamp() - firstPlace.lastSeenTimestamp();
            int lapDiff = firstPlace.laps() - me.laps();
            boolean lapped = lapDiff > 0;
            if(lapped) {
                delta = lapDiff;
            }

            // tires
            String tires = "/";
            if(k != null) tires = k.getWheelType().getShortForm();

            // create entry
            leaderboardEntries.add(new LeaderboardEntry(
                    name,
                    delta,
                    lapped,
                    tires,
                    me.finished()
            ));
        }


        // update each participant
        for(UUID uuid : this.participantInfo.keySet()) {
            PlayerEntity p = this.world.getPlayerByUuid(uuid);
            if(p == null) continue;

            // update leaderboard
            if (p instanceof ServerPlayerEntity serverPlayer) {
                LeaderboardUpdateEntriesS2C leaderboardUpdateEntriesS2C = new LeaderboardUpdateEntriesS2C(leaderboardEntries);
                ServerPlayNetworking.send(serverPlayer, leaderboardUpdateEntriesS2C);
            }
        }

    }




    private void handleLineCrossing(KartEntity kart, long timestamp) {
        if(this.world == null) return;
        if(kart.getControllingPassenger() == null) return;
        if(kart.getControllingPassenger() instanceof PlayerEntity player) {

            // retrieve info
            UUID playerUuid = player.getUuid();
            ParticipantInfo stats = this.participantInfo.get(playerUuid);

            // first time seeing kart? => init
            if(stats == null) {
                initiateParticipation(timestamp, player, kart);
                return;
            }

            // player has finished => no more tracking
            if(stats.finished()) return;


            // ----- lap time comparison -----

            // retrieve stats
            long currentLapTime = timestamp - stats.lastSeenTimestamp();
            int currentLap = stats.laps() + 1;
            long bestLapTime = stats.best();

            // --- lap time records
            // 1) personal best
            long deltaToBest = currentLapTime - bestLapTime;
            boolean isNewPB = deltaToBest < 0;
            if(isNewPB || bestLapTime == 0) bestLapTime = currentLapTime;
            // 2) track best
            long deltaToRecord = currentLapTime - this.trackRecordLapTime;
            boolean isNewRecord = deltaToRecord < 0;
            if(isNewRecord || this.trackRecordLapTime == 0) this.trackRecordLapTime = currentLapTime;

            // check finished
            boolean hasFinished = ((this.settingCurrentMode == TrackTimerMode.GRAND_PRIX) && currentLap >= this.settingLapCount);



            // --- update entry
            ParticipantInfo updatedStats = new ParticipantInfo(timestamp, bestLapTime, currentLap, hasFinished);
            this.participantInfo.put(playerUuid, updatedStats);


            // leaderboard recalculation
            this.calculateLeaderboard();




            // ----- handling of information -----

            byte flagsPB = 0;
            if(isNewRecord) flagsPB = 1;
            else if(isNewPB) flagsPB = 2;

            // a) time attack
            if(this.settingCurrentMode == TrackTimerMode.TIME_ATTACK) {

                // create message
                Text msg = this.createTimeAttackMessage(player.getNameForScoreboard(), currentLap, currentLapTime, deltaToBest, flagsPB);

                // send messages
                for(UUID uuid : this.participantInfo.keySet()) {
                    PlayerEntity p = this.world.getPlayerByUuid(uuid);
                    if(p != null) p.sendMessage(msg);
                }
            }

            // b) grand prix
            else if(this.settingCurrentMode == TrackTimerMode.GRAND_PRIX) {

                // only update if the player has not yet finished -- important: hasFinished will be true here if it is the last lap!
                if(currentLap <= this.settingLapCount) {

                    // create chat messages
                    Text msg = this.createGrandPrixLapMessage(player.getNameForScoreboard(), kart.getRaceNumber(), currentLap, currentLapTime, flagsPB);
                    Text msgFinished = null;


                    // check for race completion
                    if(currentLap == this.settingLapCount) {

                        // calculate placement
                        int placement = -1;
                        for (int i = 0; i < this.dynamic_leaderboard.size(); i++) {
                            if (this.dynamic_leaderboard.get(i).getKey().equals(playerUuid)) {
                                placement = i + 1;
                                break;
                            }
                        }

                        // total race duration
                        long duration = timestamp - this.raceStartTimestamp;

                        // completed the race
                        msgFinished = this.createGrandPrixPlacementMessage(player.getNameForScoreboard(), kart.getRaceNumber(), placement, bestLapTime);

                        // print track report
                        ItemStack trackReport = createGrandPrixItemStack(kart, player, bestLapTime, placement, this.settingLapCount, duration);
                        if(trackReport != null) this.tryPrintingTrackReport(trackReport);
                    }


                    // update each participant
                    for(UUID uuid : this.participantInfo.keySet()) {
                        PlayerEntity p = this.world.getPlayerByUuid(uuid);
                        if(p == null) continue;

                        // send messages
                        p.sendMessage(msg);
                        if(msgFinished != null) p.sendMessage(msgFinished);
                    }

                    // update leaderboard
                    this.broadcastLeaderboardUpdate();


                    // update local stats for player who crossed line
                    if (player instanceof ServerPlayerEntity serverPlayer) {
                        LeaderboardUpdatePersonalS2C leaderboardUpdatePersonalS2C = new LeaderboardUpdatePersonalS2C(
                                player.getNameForScoreboard() + " (" + String.format("%02d", kart.getRaceNumber()) + ")",
                                currentLapTime,
                                bestLapTime,
                                bestLapTime == this.trackRecordLapTime,
                                currentLap,
                                this.settingLapCount
                        );
                        ServerPlayNetworking.send(serverPlayer, leaderboardUpdatePersonalS2C);
                    }


                }
            }

            // and sync (notify writing to nbt)
            this.markDirty();
        }
    }




    private Text createTimeAttackMessage(String playerName, int currentLap, long lapTime, long deltaToPB, byte flagsPB) {

        // formatting
        Formatting lapTimeFormat = switch(flagsPB) {
            case 1 -> Formatting.LIGHT_PURPLE;
            case 2 -> Formatting.GREEN;
            default -> Formatting.WHITE;
        };
        Formatting deltaTimeFormat = (deltaToPB > 0) ? (Formatting.RED) : (Formatting.GREEN);

        // times to string
        char deltaTimePrefix = (deltaToPB > 0) ? ('+') : ('-');
        String lapTimeString = getFormattedLapTime(lapTime);
        String deltaLapTimeString = getFormattedLapTime(Math.abs(deltaToPB));

        // craft message
        return getPrefix()
                .append(Text.literal(playerName))
                .append(Text.literal(", [Lap " + currentLap + "] : "))
                .append(Text.literal(lapTimeString + " ").formatted(lapTimeFormat))
                .append(Text.literal("(" + deltaTimePrefix + deltaLapTimeString + ")").formatted(deltaTimeFormat));
    }

    private Text createGrandPrixLapMessage(String playerName, int kartNr, int laps, long lapTime, byte flagsPB) {

        // formatting
        Formatting lapTimeFormat = switch(flagsPB) {
            case 1 -> Formatting.LIGHT_PURPLE;
            case 2 -> Formatting.GREEN;
            default -> Formatting.WHITE;
        };

        // times to string
        String lapTimeString = getFormattedLapTime(lapTime);

        // craft message
        return getPrefix()
                .append(Text.literal(playerName + " (" + kartNr + ") completed [Lap " + laps + "]: ").formatted(Formatting.WHITE))
                .append(Text.literal(lapTimeString).formatted(lapTimeFormat));
    }

    private Text createGrandPrixPlacementMessage(String playerName, int kartNr, int placement, long best) {

        // formatting
        int playerNameColor = TrackReport.getPlacementColor(placement);

        // times to string
        String deltaLapTimeString = getFormattedLapTime(Math.abs(best));

        // craft message
        return getPrefix()
                .append(Text.literal(playerName + " (" + kartNr + ")").setStyle(Style.EMPTY.withColor(playerNameColor)))
                .append(Text.literal(" placed ").formatted(Formatting.WHITE))
                .append(Text.literal("#" + placement).setStyle(Style.EMPTY.withColor(playerNameColor)))
                .append(Text.literal(" (" + deltaLapTimeString + ")").formatted(Formatting.AQUA));
    }


    private ItemStack createTimeAttackItemStack(PlayerEntity player, long bestLap, long duration, int laps) {
        ItemStack res = ItemStack.EMPTY;

        if(this.participantInfo.containsKey(player.getUuid())) {
            // retrieve all data
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String todayAsString = today.format(formatter);
            String bestLapString = getFormattedLapTime(bestLap);
            String avgLapString = getFormattedLapTime(duration / laps);
            String durationString = getFormattedLapTime(duration);

            // create item
            res = new ItemStack(ModItems.TRACK_REPORT);

            res.set(ModDataComponentTypes.TRACK_REPORT_PLAYER_NAME, player.getNameForScoreboard());
            res.set(ModDataComponentTypes.TRACK_REPORT_BEST_LAP_TIME, bestLapString);
            res.set(ModDataComponentTypes.TRACK_REPORT_AVG_LAP_TIME, avgLapString);
            res.set(ModDataComponentTypes.TRACK_REPORT_DATE, todayAsString);
            res.set(ModDataComponentTypes.TRACK_REPORT_DURATION, durationString);
        }

        return res;
    }

    private ItemStack createGrandPrixItemStack(KartEntity kart, PlayerEntity player, long bestLap, int placement, int totalLaps, long duration) {
        ItemStack res = ItemStack.EMPTY;

        if(this.participantInfo.containsKey(player.getUuid())) {
            // retrieve all data
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");
            String todayAsString = now.format(formatter);
            String bestLapString = getFormattedLapTime(bestLap);
            String avgLapString = getFormattedLapTime(duration / totalLaps);
            String durationString = getFormattedLapTime(duration);

            // create item
            res = new ItemStack(ModItems.TRACK_REPORT);

            res.set(ModDataComponentTypes.KART_ITEM_RACE_NUMBER, kart.getRaceNumber());
            res.set(ModDataComponentTypes.TRACK_REPORT_PLAYER_NAME, player.getNameForScoreboard());
            res.set(ModDataComponentTypes.TRACK_REPORT_BEST_LAP_TIME, bestLapString);
            res.set(ModDataComponentTypes.TRACK_REPORT_AVG_LAP_TIME, avgLapString);
            res.set(ModDataComponentTypes.TRACK_REPORT_DATE, todayAsString);
            res.set(ModDataComponentTypes.TRACK_REPORT_DURATION, durationString);
            res.set(ModDataComponentTypes.TRACK_REPORT_PLACEMENT, placement);
            res.set(ModDataComponentTypes.TRACK_REPORT_TOTAL_LAPS, totalLaps);
        }

        return res;
    }

    private void tryPrintingTrackReport(ItemStack trackReportItemStack) {
        ItemStack input = this.inventory.getFirst();

        // has paper?
        if (input.isOf(Items.PAPER) && input.getCount() > 0) {

            // search valid output slot
            int targetSlot = -1;
            for (int i = 1; i < 10; i++) {
                ItemStack currentSlot = this.inventory.get(i);

                // accept if empty
                if (currentSlot.isEmpty()) {
                    targetSlot = i;
                    break;
                }
            }

            // perform transaction
            if (targetSlot != -1) {
                input.decrement(1);
                this.inventory.set(targetSlot, trackReportItemStack);
                this.markDirty();
            }
        }
    }









    public static void tick(World world, BlockPos pos, BlockState state, TrackTimerBlockEntity entity) {

        // do nothing when race is not yet started
        if(world == null || world.isClient() || !entity.hasStarted || entity.boundingBox == null) return;

        // search in the bounding box for karts
        List<KartEntity> karts = world.getEntitiesByClass(
                KartEntity.class,
                entity.boundingBox,
                kart -> kart.hasControllingPassenger() && kart.getControllingPassenger() instanceof PlayerEntity
        );

        // found kart(s)
        Set<UUID> currentlyNearbyUuids = new HashSet<>();

        for (KartEntity kart : karts) {
            if(kart.getControllingPassenger() == null) continue;
            if(kart.getControllingPassenger() instanceof PlayerEntity) {

                UUID uuid = kart.getUuid();
                currentlyNearbyUuids.add(uuid);

                // get positions
                Vec3d currentPos = kart.getPos();
                Vec3d previousPos = entity.previousKartPosition.get(uuid);

                // already seen this kart
                if (previousPos != null) {

                    // check crossing
                    if (hasCrossedFinishLine(entity, previousPos, currentPos)) {

                        // has completed one lap!
                        entity.handleLineCrossing(kart, world.getTime());
                    }
                }

                // update position
                entity.previousKartPosition.put(uuid, currentPos);
            }
        }
        // memory cleanup
        entity.previousKartPosition.keySet().retainAll(currentlyNearbyUuids);
    }




    // helpers
    private void calculateBoundingBox() {
        if(this.trackWidth == 0) return; //< loaded wrong data from nbt

        // calculate driving direction
        Direction facing = this.getCachedState().get(TrackTimer.FACING);
        this.drivingDirection = facing.rotateCounterclockwise(Direction.Axis.Y);

        // calculate corner points
        BlockPos corner1 = this.pos
                .offset(facing, 1)
                .offset(this.drivingDirection, BOUNDING_BOX_RADIUS);
        BlockPos corner2 = this.pos
                .offset(facing, this.trackWidth)
                .offset(this.drivingDirection.getOpposite(), BOUNDING_BOX_RADIUS);

        // create union of corner points
        this.boundingBox = new Box(corner1).union(new Box(corner2));
    }

    private static boolean hasCrossedFinishLine(TrackTimerBlockEntity entity, Vec3d previousPos, Vec3d currentPos) {
        double timerBlockEntityX = entity.pos.getX() + 0.5;
        double timerBlockEntityZ = entity.pos.getZ() + 0.5;

        return switch (entity.drivingDirection) {
            case NORTH -> // Driving North (Z is decreasing)
                    previousPos.z > timerBlockEntityZ && currentPos.z <= timerBlockEntityZ;
            case SOUTH -> // Driving South (Z is increasing)
                    previousPos.z < timerBlockEntityZ && currentPos.z >= timerBlockEntityZ;
            case WEST ->  // Driving West (X is decreasing)
                    previousPos.x > timerBlockEntityX && currentPos.x <= timerBlockEntityX;
            case EAST ->  // Driving East (X is increasing)
                    previousPos.x < timerBlockEntityX && currentPos.x >= timerBlockEntityX;
            default -> false;
        };
    }

    // formatted time strings
    public static String getFormattedLapTime(long lapTime) {

        // convert to milliseconds
        long deltaMillis = lapTime * 50;

        // calculate times
        long millis = deltaMillis % 1000;
        long totalSeconds = deltaMillis / 1000;

        long seconds = totalSeconds % 60;
        long totalMinutes = totalSeconds / 60;

        long minutes = totalMinutes % 60;
        long hours = totalMinutes / 60;

        // build string
        String formattedString = "";
        if (hours > 0) {
            formattedString = String.format("%d:%02d:%02d.%03d", hours, minutes, seconds, millis);
        } else if (minutes > 0) {
            formattedString = String.format("%d:%02d.%03d", minutes, seconds, millis);
        } else {
            formattedString = String.format("%d.%03d", seconds, millis);
        }
        return formattedString;
    }

    private static MutableText getPrefix() {
        return Text.literal("[TrackTimer] ").formatted(Formatting.GRAY)
                .append(Text.literal("» ").formatted(Formatting.DARK_GRAY));
    }





    // -------------------- interface for screen handler --------------------

    public void cycleMode() {
        // not allowed during race
        if(hasStarted) return;

        // cycle mode
        if(this.settingCurrentMode ==  TrackTimerMode.GRAND_PRIX) this.settingCurrentMode = TrackTimerMode.TIME_ATTACK;
        else this.settingCurrentMode = TrackTimerMode.GRAND_PRIX;
        this.markDirty();
    }

    public void setLapCount(int settingLapCount) {
        // not allowed during race
        if(hasStarted) return;

        // set laps
        if(settingLapCount > MAX_LAPS) settingLapCount = MAX_LAPS;
        else if(settingLapCount < 1) settingLapCount = 1;
        this.settingLapCount = settingLapCount;
        this.markDirty();
    }

    public void start(PlayerEntity player) {
        // cannot start twice
        if(this.hasStarted) {
            Text msg = getPrefix().append(Text.literal("Timer has already started. Reset it before starting again! ").formatted(Formatting.RED));
            player.sendMessage(msg);
            return;
        }

        // search for a reflector block
        if (this.world != null && !this.world.isClient()) {

            // get rotation of this block
            Direction facing = this.getCachedState().get(TrackTimer.FACING);

            // loop trough blocks in facing direction
            for(int width = 0; width < MAX_TRACK_WIDTH; width++) {
                // get block
                BlockPos inspectedPos = this.pos.offset(facing, width + 1);
                Block inspectedBlock =  this.world.getBlockState(inspectedPos).getBlock();

                // check block
                if(inspectedBlock.equals(ModBlocks.TRACK_TIMER_REFLECTOR_BLOCK)) {
                    if(this.world.getBlockState(inspectedPos).get(TrackTimer.FACING) == facing.getOpposite()) {
                        // correct placement => start
                            this.trackWidth = width;
                            calculateBoundingBox();
                            this.hasStarted = true;
                            this.markDirty();
                            Text msg = getPrefix().append(Text.literal("Timer has started!").formatted(Formatting.GREEN));
                            player.sendMessage(msg);
                            return;
                    }
                    Text msg = getPrefix().append(Text.literal("Found reflector but it is rotated wrongly!").formatted(Formatting.RED));
                    player.sendMessage(msg);
                    return;
                }
                else if(this.world.getBlockState(inspectedPos).getOpacity(world, pos) == 15) {
                    Text msg = getPrefix().append(Text.literal("Cannot find reflector. Maybe it is obstructed?").formatted(Formatting.RED));
                    player.sendMessage(msg);
                    return; //< only transparent blocks are valid
                }
            }
            Text msg = getPrefix().append(Text.literal("Cannot find reflector. Maybe it is too far away? (max: " + MAX_TRACK_WIDTH + ")").formatted(Formatting.RED));
            player.sendMessage(msg);
        }
    }

    public void stopOrReset(PlayerEntity player) {
        // reset everything server sided
        if (this.world != null && !this.world.isClient()) {

            this.boundingBox = null;
            this.previousKartPosition.clear();

            this.trackWidth = 0;

            this.hasStarted = false;
            this.trackRecordLapTime = 0;
            this.raceStartTimestamp = 0;


            // broadcast race stopped to players => stops hud overlay
            for(UUID uuid : this.participantInfo.keySet()) {
                PlayerEntity p = this.world.getPlayerByUuid(uuid);
                if(p instanceof ServerPlayerEntity serverPlayer) {
                        LeaderboardResetS2C leaderboardResetS2C = new LeaderboardResetS2C();
                        ServerPlayNetworking.send(serverPlayer, leaderboardResetS2C);
                }
            }
            // and clear participants
            this.participantInfo.clear();

            Text msg = getPrefix().append(Text.literal("Timer has stopped and reset. Ready to start again!").formatted(Formatting.GREEN));
            player.sendMessage(msg);

            // and save
            this.markDirty();
        }
    }

    public void printTimeAttackTrackRecord(PlayerEntity player) {
        if(!this.participantInfo.containsKey(player.getUuid())) return;

        ParticipantInfo stats = this.participantInfo.get(player.getUuid());
        if(stats.best() <= 0) return;
        if(stats.laps() <= 0) return;

        long duration = stats.lastSeenTimestamp() - this.raceStartTimestamp;

        ItemStack trackReport = createTimeAttackItemStack(player, stats.best(), duration, stats.laps());
        if(trackReport != null) this.tryPrintingTrackReport(trackReport);
    }






    // -------------------- persist data --------------------


    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        // settings
        if (this.settingCurrentMode != null) {
            nbt.putInt("SettingCurrentMode", this.settingCurrentMode.ordinal());
        }
        nbt.putInt("SettingLapCount", this.settingLapCount);

        // track width
        nbt.putInt("TrackWidth", this.trackWidth);
        calculateBoundingBox(); //< also recalculate bounding box!

        // race stats
        nbt.putBoolean("HasStarted", this.hasStarted);
        nbt.putLong("TrackRecordLapTime", this.trackRecordLapTime);
        nbt.putLong("RaceStartTimestamp", this.raceStartTimestamp);

        NbtList playerListTag = new NbtList();
        for (Map.Entry<UUID, ParticipantInfo> entry : this.participantInfo.entrySet()) {
            NbtCompound playerTag = new NbtCompound();
            playerTag.putUuid("KartUUID", entry.getKey());
            playerTag.put("Stats", entry.getValue().toNbt());
            playerListTag.add(playerTag);
        }
        nbt.put("PlayerInfo", playerListTag);

        // inventory
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        // settings
        if (nbt.contains("SettingCurrentMode")) {
            int ordinal = nbt.getInt("SettingCurrentMode");
            if(ordinal >= 0 && ordinal < TrackTimerMode.values().length) this.settingCurrentMode = TrackTimerMode.values()[ordinal];
        }
        this.settingLapCount = nbt.getInt("SettingLapCount");

        // track width
        this.trackWidth = nbt.getInt("TrackWidth");

        // race stats
        this.hasStarted = nbt.getBoolean("HasStarted");
        this.trackWidth = nbt.getInt("TrackWidth");
        this.trackRecordLapTime = nbt.getLong("TrackRecordLapTime");
        this.raceStartTimestamp = nbt.getLong("RaceStartTimestamp");

        this.participantInfo.clear();
        if (nbt.contains("PlayerInfo", NbtElement.LIST_TYPE)) {
            NbtList playerListTag = nbt.getList("PlayerInfo", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < playerListTag.size(); i++) {
                NbtCompound playerTag = playerListTag.getCompound(i);

                UUID uuid = playerTag.getUuid("KartUUID");
                ParticipantInfo stats = ParticipantInfo.fromNbt(playerTag.getCompound("Stats"));

                this.participantInfo.put(uuid, stats);
            }
        }

        // inventory
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory, registryLookup);
    }





    // -------------------- inventory interface --------------------

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(this.inventory, slot, amount);
        if (!result.isEmpty()) {
            this.markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = Inventories.removeStack(this.inventory, slot);
        this.markDirty();
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > stack.getMaxCount()) {
            stack.setCount(stack.getMaxCount());
        }
        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
        this.markDirty();
    }



    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) return TOP_SLOTS;
        if (side == Direction.DOWN) return BOTTOM_SLOTS;
        return SIDE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return dir == Direction.UP && slot == 0 && stack.isOf(Items.PAPER);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && slot > 0;
    }




    // -------------------- stuff for screen handler --------------------


    // property delegate for server -> client (screen + screen handler) sync
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> settingCurrentMode.ordinal();
                case 1 -> settingLapCount;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> {
                    if (value >= 0 && value < TrackTimerMode.values().length) {
                        settingCurrentMode = TrackTimerMode.values()[value];
                    }
                }
                case 1 -> settingLapCount = value;
            }
        }

        @Override
        public int size() {
            return 2; //< 2 variables tracked
        }
    };

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.bokarts.track_timer_block");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TrackTimerScreenHandler(
                syncId,
                playerInventory,
                ScreenHandlerContext.create(this.world, this.pos),
                this.propertyDelegate,
                this,
                this
        );
    }



    // -------------------- allow only one user --------------------

    public boolean tryOpen(PlayerEntity player) {
        if (this.currentUser == null || this.currentUser.isRemoved()) {
            this.currentUser = player;
            return true;
        }
        return this.currentUser == player;
    }

    public void close(PlayerEntity player) {
        if (this.currentUser == player) {
            this.currentUser = null;
        }
    }
}
