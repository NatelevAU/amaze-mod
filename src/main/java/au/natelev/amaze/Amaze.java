package au.natelev.amaze;

import au.natelev.amaze.game.Game;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.stream.Collectors;

@Mod(Amaze.MOD_ID)
public class Amaze {
    public static final String MOD_ID = "amaze";
    public final IEventBus MOD_EVENT_BUS;

    private static final Logger LOGGER = LogManager.getLogger();
    private Game game;

    public Amaze() {
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        MOD_EVENT_BUS.addListener(this::setup);
        MOD_EVENT_BUS.addListener(this::enqueueIMC);
        MOD_EVENT_BUS.addListener(this::processIMC);
        MOD_EVENT_BUS.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("amaze", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
        game = new Game(event.getServer());
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            LOGGER.info("HELLO from Register Block");
        }
    }


    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (event.getAction() == GLFW.GLFW_PRESS) {
            switch (event.getKey()) {
//            case GLFW.GLFW_KEY_W:
                case GLFW.GLFW_KEY_UP:
                    game.moveUp();
                    break;
//            case GLFW.GLFW_KEY_S:
                case GLFW.GLFW_KEY_DOWN:
                    game.moveDown();
                    break;
//            case GLFW.GLFW_KEY_A:
                case GLFW.GLFW_KEY_LEFT:
                    game.moveLeft();
                    break;
//            case GLFW.GLFW_KEY_D:
                case GLFW.GLFW_KEY_RIGHT:
                    game.moveRight();
                    break;
                case GLFW.GLFW_KEY_Z:
                    game.resetLevel();
                    break;
                case GLFW.GLFW_KEY_R:
                    game.reset();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().teleportTo(0, 79, 0);
    }

    @SubscribeEvent
    public void onPlayerMove(final LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() == null || !(event.getEntityLiving() instanceof PlayerEntity))
            return;
//        PlayerEntity player = (PlayerEntity) event.getEntity();
        // only make event cancellable if player is moving
        event.setCanceled(true);
    }
}
