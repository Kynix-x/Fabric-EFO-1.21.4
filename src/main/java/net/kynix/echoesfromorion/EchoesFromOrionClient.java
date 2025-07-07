package net.kynix.echoesfromorion;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class EchoesFromOrionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WindowShake.registerTick(); // her tick’te sallama kontrolü

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(
                        ClientCommandManager.literal("shake")   // ← DOĞRU literal
                                .executes(ctx -> {
                                    WindowShake.start(30, 12);      // 1.5 sn ±12 px
                                    return 1;
                                })
                )
        );
    }
}
