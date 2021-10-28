package electrodynamics.common.tile.generic;

import java.util.ArrayList;
import java.util.HashSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import electrodynamics.api.electricity.CapabilityElectrodynamic;
import electrodynamics.api.electricity.IElectrodynamic;
import electrodynamics.api.network.conductor.IConductor;
import electrodynamics.common.network.ElectricNetwork;
import electrodynamics.common.network.ElectricityUtilities;
import electrodynamics.prefab.network.AbstractNetwork;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.utilities.Scheduler;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class GenericTileWire extends GenericTile implements IConductor {

    public ElectricNetwork electricNetwork;

    protected GenericTileWire(BlockEntityType<?> tileEntityTypeIn) {
	super(tileEntityTypeIn);
	for (Direction dir : Direction.values()) {
	    handler.add(new IElectrodynamic() {
		@Override
		public double getMaxJoulesStored() {
		    return 0;
		}

		@Override
		public double getJoulesStored() {
		    return 0;
		}

		@Override
		public TransferPack receivePower(TransferPack transfer, boolean debug) {
		    ArrayList<BlockEntity> ignored = new ArrayList<>();
		    ignored.add(level.getBlockEntity(new BlockPos(worldPosition).relative(dir)));
		    if (!debug) {
			getNetwork().addProducer(ignored.get(0), transfer.getVoltage());
		    }
		    return getNetwork().receivePower(transfer, debug);
		}
	    });
	}
	addComponent(new ComponentPacketHandler().customPacketReader(this::readCustomPacket).customPacketWriter(this::writeCustomPacket));
    }

    private ArrayList<IElectrodynamic> handler = new ArrayList<>();

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
	if (capability == CapabilityElectrodynamic.ELECTRODYNAMIC) {
	    return LazyOptional.of(() -> handler.get(facing.ordinal())).cast();
	}
	return super.getCapability(capability, facing);
    }

    @Override
    public ElectricNetwork getNetwork() {
	return getNetwork(true);
    }

    private HashSet<IConductor> getConnectedConductors() {
	HashSet<IConductor> set = new HashSet<>();

	for (Direction dir : Direction.values()) {
	    BlockEntity facing = level.getBlockEntity(worldPosition.relative(dir));
	    if (facing instanceof IConductor) {
		set.add((IConductor) facing);
	    }
	}
	return set;
    }

    @Override
    public ElectricNetwork getNetwork(boolean createIfNull) {
	if (electricNetwork == null && createIfNull) {
	    HashSet<IConductor> adjacentCables = getConnectedConductors();
	    HashSet<ElectricNetwork> connectedNets = new HashSet<>();
	    for (IConductor wire : adjacentCables) {
		if (wire.getNetwork(false) != null && wire.getNetwork() instanceof ElectricNetwork) {
		    connectedNets.add((ElectricNetwork) wire.getNetwork());
		}
	    }
	    if (connectedNets.isEmpty()) {
		electricNetwork = new ElectricNetwork(Sets.newHashSet(this));
	    } else if (connectedNets.size() == 1) {
		electricNetwork = (ElectricNetwork) connectedNets.toArray()[0];
		electricNetwork.conductorSet.add(this);
	    } else {
		electricNetwork = new ElectricNetwork(connectedNets, false);
		electricNetwork.conductorSet.add(this);
	    }
	}
	return electricNetwork;
    }

    @Override
    public void setNetwork(AbstractNetwork<?, ?, ?, ?> network) {
	if (electricNetwork != network && network instanceof ElectricNetwork) {
	    removeFromNetwork();
	    electricNetwork = (ElectricNetwork) network;
	}
    }

    @Override
    public void refreshNetwork() {
	if (!level.isClientSide) {
	    updateAdjacent();
	    ArrayList<ElectricNetwork> foundNetworks = new ArrayList<>();
	    for (Direction dir : Direction.values()) {
		BlockEntity facing = level.getBlockEntity(worldPosition.relative(dir));
		if (facing instanceof IConductor && ((IConductor) facing).getNetwork() instanceof ElectricNetwork) {
		    foundNetworks.add((ElectricNetwork) ((IConductor) facing).getNetwork());
		}
	    }
	    if (!foundNetworks.isEmpty()) {
		foundNetworks.get(0).conductorSet.add(this);
		electricNetwork = foundNetworks.get(0);
		if (foundNetworks.size() > 1) {
		    foundNetworks.remove(0);
		    for (ElectricNetwork network : foundNetworks) {
			getNetwork().merge(network);
		    }
		}
	    }
	    getNetwork().refresh();
	}
    }

    private boolean[] connections = new boolean[6];
    private BlockEntity[] tileConnections = new BlockEntity[6];

    public boolean updateAdjacent() {
	boolean flag = false;
	for (Direction dir : Direction.values()) {
	    BlockEntity tile = level.getBlockEntity(worldPosition.relative(dir));
	    boolean is = ElectricityUtilities.isElectricReceiver(tile, dir.getOpposite());
	    if (connections[dir.ordinal()] != is) {
		connections[dir.ordinal()] = is;
		tileConnections[dir.ordinal()] = tile;
		flag = true;
	    }

	}
	return flag;
    }

    @Override
    public BlockEntity[] getAdjacentConnections() {
	return tileConnections;
    }

    @Override
    public void refreshNetworkIfChange() {
	if (updateAdjacent()) {
	    refreshNetwork();
	}
    }

    @Override
    public void removeFromNetwork() {
	if (electricNetwork != null) {
	    electricNetwork.removeFromNetwork(this);
	}
    }

    @Override
    public AbstractNetwork<?, ?, ?, ?> getAbstractNetwork() {
	return electricNetwork;
    }

    @Override
    public void destroyViolently() {
	level.setBlockAndUpdate(worldPosition, Blocks.FIRE.defaultBlockState());
    }

    @Override
    protected void invalidateCaps() {
	super.invalidateCaps();
	if (!level.isClientSide && electricNetwork != null) {
	    getNetwork().split(this);
	}
    }

    @Override
    public void onLoad() {
	super.onLoad();
	Scheduler.schedule(1, this::refreshNetwork);
    }

    protected abstract void writeCustomPacket(CompoundTag nbt);

    protected abstract void readCustomPacket(CompoundTag nbt);

}
