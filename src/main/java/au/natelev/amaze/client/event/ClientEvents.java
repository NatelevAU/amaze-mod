package au.natelev.amaze.client.event;

import au.natelev.amaze.Amaze;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Amaze.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

}
