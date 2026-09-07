package M6FGR.mapi.builders;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ItemsBuilder {

    private final DeferredRegister<Item> rawRegistry;
    private final String modID;

    private ItemsBuilder(String modID) {
        this.modID = modID;
        this.rawRegistry = DeferredRegister.create(Registries.ITEM, modID);
    }

    private static boolean usedBuilderRegistry = false;

    public static ItemsBuilder createRegistry(String modID) {
        usedBuilderRegistry = true;
        return new ItemsBuilder(modID);
    }

    /**
     * Registers a new item with easy property manipulation.
     * @param itemID Unique identifier for the item.
     * @param constructor The constructor reference (e.g., MyItem::new).
     * @param propertyModifier A lambda to adjust item properties (e.g., p -> p.stacksTo(1)).
     */
    public <T extends Item> RegistryObject<T> newItem(
            String itemID,
            Function<Item.Properties, T> constructor,
            UnaryOperator<Item.Properties> propertyModifier
    ) {
        if (!usedBuilderRegistry) {
            throw new IllegalArgumentException("Cannot register items for: " + itemID + ", Use ItemsBuilder.createRegistry() instead of DeferredRegister.create()!");
        }
        return this.rawRegistry.register(itemID, () -> {
            Item.Properties props = propertyModifier.apply(new Item.Properties());
            return constructor.apply(props);
        });
    }


    public void register(IEventBus modBus) {
        this.rawRegistry.register(modBus);
    }

    // Overload for simple items that don't need custom properties
    public <T extends Item> RegistryObject<T> newItem(String itemID, Function<Item.Properties, T> constructor) {
        return newItem(itemID, constructor, p -> p);
    }
}