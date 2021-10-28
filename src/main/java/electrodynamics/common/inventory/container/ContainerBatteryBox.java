package electrodynamics.common.inventory.container;

import electrodynamics.DeferredRegisters;
import electrodynamics.common.item.subtype.SubtypeProcessorUpgrade;
import electrodynamics.common.tile.TileBatteryBox;
import electrodynamics.prefab.inventory.container.GenericContainer;
import electrodynamics.prefab.inventory.container.slot.SlotRestricted;
import electrodynamics.prefab.item.ItemElectric;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerBatteryBox extends GenericContainer<TileBatteryBox> {

    public ContainerBatteryBox(int id, Inventory playerinv) {
	this(id, playerinv, new SimpleContainer(6), new SimpleContainerData(3));
    }

    public ContainerBatteryBox(int id, Inventory pinv, Container inv, ContainerData data) {
	super(DeferredRegisters.CONTAINER_BATTERYBOX.get(), id, pinv, inv, data);
    }

    @Override
    public void addInventorySlots(Container inv, Inventory playerinv) {
	addSlot(new SlotRestricted(inv, nextIndex(), 153, 14, DeferredRegisters.SUBTYPEITEM_MAPPINGS.get(SubtypeProcessorUpgrade.basiccapacity),
		DeferredRegisters.SUBTYPEITEM_MAPPINGS.get(SubtypeProcessorUpgrade.advancedcapacity)));
	addSlot(new SlotRestricted(inv, nextIndex(), 153, 34, DeferredRegisters.SUBTYPEITEM_MAPPINGS.get(SubtypeProcessorUpgrade.basiccapacity),
		DeferredRegisters.SUBTYPEITEM_MAPPINGS.get(SubtypeProcessorUpgrade.advancedcapacity)));
	addSlot(new SlotRestricted(inv, nextIndex(), 153, 54, DeferredRegisters.SUBTYPEITEM_MAPPINGS.get(SubtypeProcessorUpgrade.basiccapacity),
		DeferredRegisters.SUBTYPEITEM_MAPPINGS.get(SubtypeProcessorUpgrade.advancedcapacity)));
	addSlot(new SlotRestricted(inv, nextIndex(), 133, 14, false, ItemElectric.class));
    }

}