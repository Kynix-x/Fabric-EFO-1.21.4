package net.kynix.echoesfromorion;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.concurrent.ThreadLocalRandom;

public class WindowShake {
    private static int ticksLeft = 0;
    private static int baseX, baseY;
    private static long windowHandle;

    /** başlat: kaç tick, ne kadar piksel */
    public static void start(int durationTicks, int amplitude) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;

        // Tam ekrandaysa çık
        //if (client.isFullscreen()) client.toggleFullscreen();

        Window window = client.getWindow();
        windowHandle = window.getHandle();

        // Mevcut konumu kaydet
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer px = stack.mallocInt(1);
            IntBuffer py = stack.mallocInt(1);
            GLFW.glfwGetWindowPos(windowHandle, px, py);
            baseX = px.get(0);
            baseY = py.get(0);
        }

        ticksLeft = durationTicks;
        WindowShake.amplitude = amplitude;
    }

    private static int amplitude = 8; // varsayılan ±8 px

    /** her client tick çağır */
    public static void tick() {
        if (ticksLeft <= 0) return;

        int dx = ThreadLocalRandom.current().nextInt(-amplitude, amplitude + 1);
        int dy = ThreadLocalRandom.current().nextInt(-amplitude, amplitude + 1);
        GLFW.glfwSetWindowPos(windowHandle, baseX + dx, baseY + dy);

        ticksLeft--;
        if (ticksLeft == 0) { // süre bitti → pencereyi geri koy
            GLFW.glfwSetWindowPos(windowHandle, baseX, baseY);
        }
    }

    /** kayıt: EchoesFromOrionClient içinde çağır */
    public static void registerTick() {
        ClientTickEvents.END_CLIENT_TICK.register(c -> WindowShake.tick());
    }
}
