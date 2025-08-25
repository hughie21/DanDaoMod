package com.hughie.dandao.common.entity;

import com.hughie.dandao.DanDao;
import com.hughie.dandao.common.effect.ModEffects;
import com.hughie.dandao.common.item.ModItems;
import com.hughie.dandao.common.recipe.AlchemyFurnaceRecipe;
import com.hughie.dandao.common.screen.AlchemyFurnaceMenu;
import com.hughie.dandao.common.sound.ModSounds;
import com.hughie.dandao.common.util.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AlchemyFurnaceBlockEntity extends BlockEntity implements MenuProvider {
    private boolean isRender = false;
    private Direction direction = Direction.SOUTH;

    private final ItemStackHandler itemHandler = new ItemStackHandler(12);
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private static final int FUEL_INPUT_1 = 0;
    private static final int FUEL_INPUT_2 = 1;
    private static final int FUEL_INPUT_3 = 2;
    private static final int FUEL_INPUT_4 = 3;

    private static final int INGREDIENT_INPUT_START = 4;
    private static final int INGREDIENT_INPUT_END = 12;

    protected final ContainerData data;
    private int burning = 0;
    private int cooling = 0;
    private int heat = 0;
    private int maxBurning = 1000;
    private int maxHeat = 400;
    private int maxCooling = 100;
    private int isOpen = 1;
    private boolean isCrafting = false;

    private double baseSuccessRate = 0.3;

    private UUID playerUID;

    private long startTime;
    private long endTime;

    private int minusCount = 0;

    public int getHeat() {
        return heat;
    }

    public int getCooling() {
        return cooling;
    }

    public int getBurning() {
        return burning;
    }

    public boolean isCrafting() {
        return isCrafting;
    }

    public void bomb(Level level, BlockPos pos) {
        ExplosionUtils.createTNTLikeExplosion(level, pos.getX(), pos.getY(), pos.getZ(), 4, true, false, null);
        isCrafting = false;
        heat = 0;

        for(int i = INGREDIENT_INPUT_START; i < INGREDIENT_INPUT_END; i++) {
            ItemStack danEmbryo = itemHandler.getStackInSlot(i);
            if(danEmbryo.isEmpty()) {
                continue;
            }
            itemHandler.setStackInSlot(i, new ItemStack(Items.GUNPOWDER));
        }
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(12);
        for(int i = 0; i < 12; i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Player player = null;
        if (level != null) {
            player = level.getPlayerByUUID(playerUID);
        }
        BlockPos pos = player.getOnPos();
        Containers.dropContents(this.level, pos, inventory);
    }

    public AlchemyFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.ALCHEMY_FURNACE.get(), pos, state);
        Player instancePlayer = Minecraft.getInstance().player;
        this.playerUID = instancePlayer.getUUID();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AlchemyFurnaceBlockEntity.this.burning;
                    case 1 -> AlchemyFurnaceBlockEntity.this.maxBurning;
                    case 2 -> AlchemyFurnaceBlockEntity.this.heat;
                    case 3 -> AlchemyFurnaceBlockEntity.this.maxHeat;
                    case 4 -> AlchemyFurnaceBlockEntity.this.isOpen;
                    case 5 -> AlchemyFurnaceBlockEntity.this.cooling;
                    case 6 -> AlchemyFurnaceBlockEntity.this.maxCooling;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AlchemyFurnaceBlockEntity.this.burning = value;
                    case 1 -> AlchemyFurnaceBlockEntity.this.maxBurning = value;
                    case 2 -> AlchemyFurnaceBlockEntity.this.heat = value;
                    case 3 -> AlchemyFurnaceBlockEntity.this.maxHeat = value;
                    case 4 -> AlchemyFurnaceBlockEntity.this.isOpen = value;
                    case 5 -> AlchemyFurnaceBlockEntity.this.cooling = value;
                    case 6 -> AlchemyFurnaceBlockEntity.this.maxCooling = value;
                    default -> {}
                }
            }

            @Override
            public int getCount() {
                return 7;
            }
        };
    }

    public boolean isOpen() {
        return this.isOpen == 1;
    }

    public void toggleButtonState() {
        isOpen = isOpen == 0 ? 1 : 0;
        BlockPos pos = getBlockPos();
        if(isOpen == 1) {
            level.playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    ModSounds.OPEN_FURNACE.get(), SoundSource.BLOCKS, 1, 1, 0);
        }else {
            level.playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    ModSounds.CLOSE_FURNACE.get(), SoundSource.BLOCKS, 1, 1, 0);
        }
        setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("IsRender", isRender);
        tag.putString("Direction", direction.getSerializedName());
        tag.put("AlchemyFurnaceInventory", itemHandler.serializeNBT());
        tag.putInt("Burning", burning);
        tag.putInt("Cooling", cooling);
        tag.putInt("Heat", heat);
        tag.putLong("StartTime", startTime);
        tag.putInt("IsOpen", isOpen);
        tag.putBoolean("IsCrafting", isCrafting);
        tag.putInt("MinusCount", minusCount);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        isRender = tag.getBoolean("IsRender");
        direction = Direction.byName(tag.getString("Direction"));
        itemHandler.deserializeNBT(tag.getCompound("AlchemyFurnaceInventory"));
        burning = tag.getInt("Burning");
        cooling = tag.getInt("Cooling");
        heat = tag.getInt("Heat");
        startTime = tag.getLong("StartTime");
        isOpen = tag.getInt("IsOpen");
        isCrafting = tag.getBoolean("IsCrafting");
        minusCount = tag.getInt("MinusCount");
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    public void render(boolean isRender, Direction direction) {
        this.isRender = isRender;
        this.direction = direction;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isRender() {
        return isRender;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.dandao.alchemy_furnace");
    }

    @Override
    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        this.playerUID = player.getUUID();
        return new AlchemyFurnaceMenu(containerId, inventory, this, this.data);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public boolean isOverTime() {
        if(isCrafting) {
            long currentTime = level.getGameTime();
            return currentTime - startTime > 20 * 60 * 3;
        }
        return false;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if(isValidFuel() && burning == 0 && cooling == 0) {
            burningFuel();
        }

        if(isCoolingThing() && heat != 0 && burning == 0) {
            coolDown();
        }

        if(cooling != 0) {
            coolDownHeat();
        }

        if(burning != 0) {
            transferToHeat();
        }

        if(burning != 0 || heat != 0) {
            if(level.random.nextInt(10) == 0) {
                level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 0.5F + level.random.nextFloat(), level.random.nextFloat() * 0.7F + 0.6F);
            }
        }

        if(burning == 0 && heat != 0 && cooling == 0) {
            if(!isCrafting) {
                if(level.getGameTime() % (5*20) == 0) {
                    heat --;
                }
            }else {
                if(level.getGameTime() % (15*20) == 0) {
                    heat --;
                }
            }
        }

        if(!isOpen() && haveDans() && heat != 0) {
            if(!isCrafting) {
                startCrafting(level);
            }
        } else if (isOpen() && isCrafting) {
            settlement(level);
        }

        if(level.getGameTime() - startTime > 5*60*20 && isCrafting) {
            bomb(level, pos);
        }

        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    private void coolDownHeat() {
        if(level.getGameTime() % 10 == 0) {
            cooling--;
            heat--;
            if(heat < 0) {
                heat = 0;
                cooling = 0;
            }
        }
    }

    private void coolDown() {
        CoolingValues coolingValues = new CoolingValues();

        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = itemHandler.getStackInSlot(i);
            if(itemStack.isEmpty()) {
                continue;
            }
            cooling += coolingValues.getValue(itemStack);
            if(cooling > maxCooling) {
                cooling = maxCooling;
            }
            if (itemStack.is(Items.POTION)) {
                itemHandler.setStackInSlot(i, new ItemStack(Items.GLASS_BOTTLE));
            } else if (itemStack.is(Items.WATER_BUCKET)) {
                itemHandler.setStackInSlot(i, new ItemStack(Items.BUCKET));
            } else if (itemStack.is(Items.POWDER_SNOW_BUCKET)) {
                itemHandler.setStackInSlot(i, new ItemStack(Items.BUCKET));
            } else {
                itemHandler.extractItem(i, 1, false);
            }
        }
        burning = 0;
    }

    private boolean isCoolingThing() {
        ItemStack fuelInput1 = itemHandler.getStackInSlot(FUEL_INPUT_1);
        ItemStack fuelInput2 = itemHandler.getStackInSlot(FUEL_INPUT_2);
        ItemStack fuelInput3 = itemHandler.getStackInSlot(FUEL_INPUT_3);
        ItemStack fuelInput4 = itemHandler.getStackInSlot(FUEL_INPUT_4);
        boolean isCoolingThing = fuelInput1.is(ModTags.COOLING) || fuelInput2.is(ModTags.COOLING) || fuelInput3.is(ModTags.COOLING) || fuelInput4.is(ModTags.COOLING);
        boolean notFuel = !fuelInput1.is(ModTags.FUELS) && !fuelInput2.is(ModTags.FUELS) && !fuelInput3.is(ModTags.FUELS) && !fuelInput4.is(ModTags.FUELS);

        return isCoolingThing && notFuel;
    }

    private void transferToHeat() {
        burning--;
        minusCount++;
        if (minusCount == 10) {
            heat++;
            if (heat > maxHeat) {
                heat = maxHeat;
            }
            minusCount = 0;
        }

    }

    private double successRate() {
        double resultRate = this.baseSuccessRate;
        Player player = level.getPlayerByUUID(this.playerUID);
        if(player.hasEffect(MobEffects.LUCK)) {
            int level = player.getEffect(MobEffects.LUCK).getAmplifier();
            resultRate += 0.05 + (level - 1)*0.05;
        }else if (player.hasEffect(ModEffects.DAN_WISDOM.get())) {
            resultRate += 0.2;
        }
        return resultRate > 1? 1 : resultRate;
    }

    private void settlement(Level level) {
        endTime = level.getGameTime();
        long craftingTime = endTime - startTime;
        int successCount = 0;
        ItemStack lastResult = null;
        DanDao.LOGGER.debug(String.format("所用时间：%d, 热量：%d", craftingTime, heat));

        for(int i = INGREDIENT_INPUT_START; i < INGREDIENT_INPUT_END; i++) {
            ItemStack danEmbryo = itemHandler.getStackInSlot(i);
            if(danEmbryo.isEmpty()) {
                continue;
            }
            List<AlchemyFurnaceRecipe> matchingRecipes = getMatchingRecipes(danEmbryo);
            if (!matchingRecipes.isEmpty()) {
                AlchemyFurnaceRecipe recipe = selectRecipe(matchingRecipes, danEmbryo, craftingTime);
                if (recipe != null) {
                    DanDao.LOGGER.debug("存在配方，开始合成");
                    ItemStack result = recipe.getResultItem(null);
                    lastResult = result;
                    if(level.random.nextDouble() > successRate()) {
                        itemHandler.setStackInSlot(i, new ItemStack(result.getItem(), 1));
                        successCount++;
                    }else {
                        itemHandler.setStackInSlot(i, new ItemStack(ModItems.HUAIDAN.get(), 1));
                    }
                }else {
                    itemHandler.setStackInSlot(i, new ItemStack(ModItems.HUAIDAN.get(), 1));
                }
            }
        }

        // 保底
        if(successCount == 0) {
            int index = INGREDIENT_INPUT_START + level.random.nextInt(INGREDIENT_INPUT_END - INGREDIENT_INPUT_START);
            if (lastResult != null) {
                DanDao.LOGGER.debug("触发保底");
                itemHandler.setStackInSlot(index, new ItemStack(lastResult.getItem(), 1));
            }
        }

        heat = heat / 2;
        isCrafting = false;
    }

    private AlchemyFurnaceRecipe selectRecipe(List<AlchemyFurnaceRecipe> matchingRecipes, ItemStack itemStack, long craftingTime) {
        Pair<MedicinalProperties, Integer> currentMedPro = MedicinalPropertiesNBT.loadProperties(itemStack);
        DanDao.LOGGER.debug("当前物品属性：" + currentMedPro.getFirst().getName() + " 等级: " + currentMedPro.getSecond());
        for(int i = 0; i < matchingRecipes.size(); i++) {
            AlchemyFurnaceRecipe recipe = matchingRecipes.get(i);
            Pair<MedicinalProperties, AlchemyFurnaceRecipe.PropertiesData> recipeMedPro = recipe.getMedicinalProperties();
            if(recipeMedPro.getFirst() != currentMedPro.getFirst()) {
               continue;
            }
            AlchemyFurnaceRecipe.PropertiesData properties = recipeMedPro.getSecond();

            int requireMinLevel = properties.getMinLevel();
            int requireMaxLevel = properties.getMaxLevel();
            float requireTime = properties.getTime() * (60*20);
            int requireMinHeat = properties.getHeat() * 100;
            int requireMaxHeat = properties.getHeat() * 100 + 100;
            DanDao.LOGGER.debug(String.format("当前配方要求: %s 等级[%d, %d] 时间：%f 温度[%d, %d]", currentMedPro.getFirst().getName(), requireMinLevel, requireMaxLevel, requireTime, requireMinHeat, requireMaxHeat));

            if(currentMedPro.getSecond() < requireMinLevel || currentMedPro.getSecond() > requireMaxLevel) {
                DanDao.LOGGER.debug("等级不符合");
                continue;
            }

            if(craftingTime > requireTime + (15*20) || craftingTime < requireTime - (15*20)) {
                DanDao.LOGGER.debug("时间不符合");
                continue;
            }

            if(heat < requireMinHeat || heat > requireMaxHeat) {
                DanDao.LOGGER.debug("温度不符合");
                continue;
            }
            DanDao.LOGGER.debug("找到符合的配方");
            return recipe;
        }
        return null;
    }

    private List<AlchemyFurnaceRecipe> getMatchingRecipes(ItemStack itemStack) {
        SimpleContainer inventory = new SimpleContainer(1);

        inventory.setItem(0, itemStack);

        // 获取匹配的配方
        return level.getRecipeManager().getAllRecipesFor(AlchemyFurnaceRecipe.Type.INSTANCE).stream()
                .filter(recipe -> recipe.matches(inventory, level))
                .collect(Collectors.toList());
    }

    private void startCrafting(Level level) {
        isCrafting = true;
        startTime = level.getGameTime();
    }

    private boolean haveDans() {
        for(int i = INGREDIENT_INPUT_START; i < INGREDIENT_INPUT_END; i++) {
            ItemStack itemStack = itemHandler.getStackInSlot(i);
            if (itemStack.is(ModItems.DAN_EMBRYO.get())) {
                return true;
            }
        }
        return false;
    }

    private void burningFuel() {
        FuelValues fuelValues = new FuelValues();
        for(int i = 0; i < 4; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);

            if(stack.isEmpty()) {
                continue;
            }

            burning += fuelValues.getValue(stack);
            if(burning > maxBurning) {
                burning = maxBurning;
            }
            if(stack.is(Items.LAVA_BUCKET)) {
                itemHandler.setStackInSlot(i, new ItemStack(Items.BUCKET, 1));
            }else {
                itemHandler.extractItem(i, 1, false);
            }
        }
        DanDao.LOGGER.debug("燃烧物品得到热值：" + burning);
    }

    private boolean isValidFuel() {
        ItemStack fuelInput1 = itemHandler.getStackInSlot(FUEL_INPUT_1);
        ItemStack fuelInput2 = itemHandler.getStackInSlot(FUEL_INPUT_2);
        ItemStack fuelInput3 = itemHandler.getStackInSlot(FUEL_INPUT_3);
        ItemStack fuelInput4 = itemHandler.getStackInSlot(FUEL_INPUT_4);

        return fuelInput1.is(ModTags.FUELS) || fuelInput2.is(ModTags.FUELS) || fuelInput3.is(ModTags.FUELS) || fuelInput4.is(ModTags.FUELS);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AlchemyFurnaceBlockEntity be) {
        if(be.getHeat() != 0 && be.isOpen()) {
            be.spawnFlareParticle(level, pos);
        }

        if(!be.isOpen() && be.isCrafting() && !be.isOverTime()) {
            be.spawnSmokeParticle(level, pos);
        }

        if(be.isOverTime()) {
            be.spawnBlackSmokeParticle(level, pos);
        }
    }

    private void spawnBlackSmokeParticle(Level level, BlockPos pos) {
        if (level.isClientSide) {
            double speedY = 0.01D + level.random.nextDouble() * 0.01D;
            level.addParticle(
                    ParticleTypes.LARGE_SMOKE,
                    pos.getX() + 0.5 + level.random.nextDouble() * 0.1 - 0.05,
                    pos.getY() + 1.8,
                    pos.getZ() + 0.5 + level.random.nextDouble() * 0.1 - 0.05,
                    0.0D, speedY, 0.0D
            );
        }
    }

    private void spawnSmokeParticle(Level level, BlockPos pos) {
        if (level.isClientSide) {
            double speedY = 0.001D + level.random.nextDouble() * 0.01D;
            level.addParticle(
                    ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                    pos.getX() + 0.5 + level.random.nextDouble() * 0.1 - 0.05,
                    pos.getY() + 1.8,
                    pos.getZ() + 0.5 + level.random.nextDouble() * 0.1 - 0.05,
                    0.0D, speedY, 0.0D
            );
        }
    }

    private void spawnFlareParticle(Level level, BlockPos pos) {
        if (level.isClientSide) {
            double[] positions = getFlarePositions(pos, level);
            level.addParticle(
                    ParticleTypes.FLAME,
                    positions[0] + level.random.nextDouble() * 0.1 - 0.05,
                    positions[1] + level.random.nextDouble() * 0.05,
                    positions[2] + level.random.nextDouble() * 0.1 - 0.05,
                    0.0D, 0.0D, 0.0D
            );
        }
    }

    private double[] getFlarePositions(BlockPos pos, Level level) {
        double x = pos.getX() + 0.5; // 方块中心X
        double y = pos.getY() + 0.46;
        double z = pos.getZ() + 0.5; // 方块中心Z

        double randomSize = level.random.nextInt(-1, 1) * 0.1;
        switch (direction) {
            case SOUTH:
                return new double[] {x + randomSize, y, z + 1.25};
            case WEST:
                return new double[] {x - 1.25, y , z + randomSize};
            case EAST:
                return new double[] {x + 1.25, y, z + randomSize};
            default:
                return new double[] {x + randomSize, y, z - 1.25};
        }
    }
}
