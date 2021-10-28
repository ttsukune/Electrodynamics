package electrodynamics.api.capability.ceramicplate;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.DeferredRegisters;
import electrodynamics.SoundRegister;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CapabilityCeramicPlateHandler {

    private static final float LETHAL_DAMAGE_AMOUNT = 18.0f;

    @SubscribeEvent
    public static void takeDamageWithArmor(LivingHurtEvent event) {

	ItemStack[] ARMOR_PIECES = new ItemStack[] { new ItemStack(DeferredRegisters.COMPOSITE_HELMET.get()),
		new ItemStack(DeferredRegisters.COMPOSITE_CHESTPLATE.get()), new ItemStack(DeferredRegisters.COMPOSITE_LEGGINGS.get()),
		new ItemStack(DeferredRegisters.COMPOSITE_BOOTS.get()) };

	List<ItemStack> armorPieces = new ArrayList<>();
	event.getEntityLiving().getArmorSlots().forEach(armorPieces::add);

	if (ItemStack.isSameIgnoreDurability(armorPieces.get(0), ARMOR_PIECES[3])
		&& ItemStack.isSameIgnoreDurability(armorPieces.get(1), ARMOR_PIECES[2])
		&& ItemStack.isSameIgnoreDurability(armorPieces.get(2), ARMOR_PIECES[1])
		&& ItemStack.isSameIgnoreDurability(armorPieces.get(3), ARMOR_PIECES[0])) {
	    ItemStack stack = armorPieces.get(2);
	    stack.getCapability(CapabilityCeramicPlate.CERAMIC_PLATE_HOLDER_CAPABILITY).ifPresent(h -> {
		if (event.getAmount() >= LETHAL_DAMAGE_AMOUNT && h.getPlateCount() > 0) {

		    event.setAmount((float) Math.sqrt(event.getAmount()));
		    h.decreasePlateCount(1);
		    event.getEntityLiving().getCommandSenderWorld().playSound(null, event.getEntityLiving().blockPosition(),
			    SoundRegister.SOUND_CERAMICPLATEBREAKING.get(), SoundSource.PLAYERS, 1, 1);
		}
	    });

	}
    }
}
