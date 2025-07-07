package net.kynix.echoesfromorion.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.kynix.echoesfromorion.EchoesFromOrion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item TEST_ITEM = registerItem(new Item(new Item.Settings()));



    private static Item registerItem(Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(EchoesFromOrion.MOD_ID, "test_item"), item);
    }

    public static void registerModItems() {
        EchoesFromOrion.LOGGER.info("Registering Mod Items for" + EchoesFromOrion.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(fabricItemGroupEntries -> {
           fabricItemGroupEntries.add(TEST_ITEM);
        });
    }
}
