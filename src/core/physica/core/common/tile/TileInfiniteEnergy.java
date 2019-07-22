package physica.core.common.tile;

import net.minecraft.tileentity.TileEntity;
import physica.api.core.abstraction.AbstractionLayer;
import physica.api.core.abstraction.FaceDirection;
import physica.api.core.electricity.IElectricityProvider;
import physica.core.common.configuration.ConfigCore;
import physica.library.location.Location;
import physica.library.tile.TileBase;

public class TileInfiniteEnergy extends TileBase implements IElectricityProvider {

	public static final int VISIBLE_STORAGE = Integer.MAX_VALUE;

	@Override
	public void updateServer(int ticks)
	{
		Location loc = getLocation();
		if (ConfigCore.DISABLE_INFINITE_ENERGY_CUBE)
		{
			loc.setBlockAir(worldObj);
			return;
		}
		for (FaceDirection dir : FaceDirection.VALID_DIRECTIONS)
		{
			TileEntity tile = worldObj.getTileEntity(loc.xCoord + dir.offsetX, loc.yCoord + dir.offsetY, loc.zCoord + dir.offsetZ);
			if (tile != null)
			{
				if (AbstractionLayer.Electricity.isElectricReceiver(tile))
				{
					if (AbstractionLayer.Electricity.canConnectElectricity(tile, dir.getOpposite()))
					{
						AbstractionLayer.Electricity.receiveElectricity(tile, dir.getOpposite(), VISIBLE_STORAGE, false);
					}
				}
			}
		}
	}

	@Override
	public boolean canConnectElectricity(FaceDirection from)
	{
		return true;
	}

	@Override
	public int extractElectricity(FaceDirection from, int maxExtract, boolean simulate)
	{
		return maxExtract;
	}

	@Override
	public int getElectricityStored(FaceDirection from)
	{
		return VISIBLE_STORAGE;
	}

	@Override
	public int getElectricCapacity(FaceDirection from)
	{
		return VISIBLE_STORAGE;
	}

}
