package net.kynix.echoesfromorion;

import net.kynix.echoesfromorion.EchoesFromOrion; // Ana mod sınıfınızın paketi
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier; // Bu import doğru ve gerekli

public class MySoundEvent { // Sınıf adınızın MySoundEvent olduğunu varsayarak

    // Kendi jumpscare sesimizi tanımlıyoruz.
    public static final SoundEvent JUMPSCARE_ROAR = registerSoundEvent("entity.jumpscare_roar");

    private static SoundEvent registerSoundEvent(String name) {
        // Hata veren satırı değiştiriyoruz:
        // Eski: Identifier id = new Identifier(EchoesFromOrion.MOD_ID, name);
        // Yeni: EchoesFromOrion.id metodunu kullanıyoruz
        Identifier id = EchoesFromOrion.id(name); // <-- BU SATIRI DÜZELTİN!
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerModSoundEvents() {
        EchoesFromOrion.LOGGER.info("Registering Mod Sound Events for " + EchoesFromOrion.MOD_ID);
    }
}