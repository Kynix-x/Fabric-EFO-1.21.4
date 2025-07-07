package net.kynix.echoesfromorion.item;

import net.kynix.echoesfromorion.EchoesFromOrion;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    public static final Item KYNIX_YUSUFTE = register("kynix_yusufte", new Item(new Item.Settings()));

    public static <I extends Item> I register(String name, I item){
        return Registry.register(Registries.ITEM, EchoesFromOrion.id(name), item);
    }

    public static void load() {}


}
