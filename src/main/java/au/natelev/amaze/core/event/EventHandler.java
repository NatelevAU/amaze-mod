package au.natelev.amaze.core.event;

import au.natelev.amaze.Amaze;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Amaze.MOD_ID, bus = Bus.FORGE)
public class EventHandler {

//    @SubscribeEvent
//    public static void onPlayerJoin(final Event event) {
//
//    }

    @SubscribeEvent
    public static void onKeyInput(final InputEvent.KeyInputEvent event) {
        switch (event.getKey()) {
            case GLFW.GLFW_KEY_W:
            case GLFW.GLFW_KEY_UP:
                // moving up
                System.out.println("Moving up");
                break;
            case GLFW.GLFW_KEY_S:
            case GLFW.GLFW_KEY_DOWN:
                // moving down
                System.out.println("Moving down");
                break;
            case GLFW.GLFW_KEY_A:
            case GLFW.GLFW_KEY_LEFT:
                // moving left
                System.out.println("Moving left");
                break;
            case GLFW.GLFW_KEY_D:
            case GLFW.GLFW_KEY_RIGHT:
                // moving right
                System.out.println("Moving right");
                break;
        }
    }
}
