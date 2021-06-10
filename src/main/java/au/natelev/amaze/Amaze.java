package au.natelev.amaze;

import au.natelev.amaze.game.Game;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

@Mod(Amaze.MOD_ID)
public class Amaze {
    public static final String MOD_ID = "amaze";
    public final IEventBus MOD_EVENT_BUS;

    private static final Logger LOGGER = LogManager.getLogger();
    private Game game;

    public Amaze() {
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
        game = new Game(event.getServer());
    }


    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (game != null && !game.isLevelWon() && event.getAction() == GLFW.GLFW_PRESS) {
            switch (event.getKey()) {
                case GLFW.GLFW_KEY_UP:
                    game.moveUp();
                    break;
                case GLFW.GLFW_KEY_DOWN:
                    game.moveDown();
                    break;
                case GLFW.GLFW_KEY_LEFT:
                    game.moveLeft();
                    break;
                case GLFW.GLFW_KEY_RIGHT:
                    game.moveRight();
                    break;
                case GLFW.GLFW_KEY_R:
                    game.reset();
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(final PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity playerEntity = event.getPlayer();
        if (playerEntity.getServer() != null) {
            MinecraftServer server = playerEntity.getServer();
            Commands commands = server.getCommands();
            CommandSource commandSource = server.createCommandSourceStack();
            commands.performCommand(commandSource, "gamerule sendCommandFeedback false");
            commands.performCommand(commandSource, "gamerule doDaylightCycle false");
            commands.performCommand(commandSource, "time set day");
            commands.performCommand(commandSource, "tp @p 0 179 -5 0 71");
        }
        playerEntity.teleportTo(0, 179, -5);
        playerEntity.setGameMode(GameType.CREATIVE);
        game.addPlayer(Objects.requireNonNull(playerEntity.getServer()).getPlayerList().getPlayer(playerEntity.getUUID()));
    }

    @SubscribeEvent
    public void onPlayerMove(final LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() == null || !(event.getEntityLiving() instanceof PlayerEntity))
            return;
        event.setCanceled(true);
    }
}
