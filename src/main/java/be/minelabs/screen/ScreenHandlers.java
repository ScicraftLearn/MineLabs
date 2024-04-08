package be.minelabs.screen;

import be.minelabs.Minelabs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlers {

    public static final ExtendedScreenHandlerType<LewisBlockScreenHandler> LEWIS_SCREEN_HANDLER = register(LewisBlockScreenHandler::new, "lewis_block");
    public static final ExtendedScreenHandlerType<IonicBlockScreenHandler> IONIC_SCREEN_HANDLER = register(IonicBlockScreenHandler::new, "ionic_block");
    public static final ScreenHandlerType<LabChestScreenHandler> LAB_CHEST_SCREEN_HANDLER = registerSimple(LabChestScreenHandler::new, "lab_chest");
    public static final ScreenHandlerType<ChargedPointScreenHandler> CHARGED_POINT_SCREEN_HANDLER = registerSimple(ChargedPointScreenHandler::new, "charged_point");

    public static final ScreenHandlerType<AtomStorageScreenHandler> ATOM_STORAGE_SCREEN_HANDLER = registerSimple(AtomStorageScreenHandler::new, "atom_storage");

    /**
     * Register a Screen
     * <p>
     *
     * @param factory : ScreenHandlerType.Factory<T> constructor of screenhandler to register
     * @param identifier        : String name of the Item
     * @return {@link ScreenHandler}
     */
    private static <T extends ScreenHandler> ScreenHandlerType<T> registerSimple(ScreenHandlerType.Factory<T> factory, String identifier) {
        return (ScreenHandlerType) Registry.register(Registries.SCREEN_HANDLER,
                new Identifier(Minelabs.MOD_ID, identifier),
                new ScreenHandlerType(factory, FeatureFlags.VANILLA_FEATURES));
    }

    /**
     * Register an {@link ExtendedScreenHandlerType}.
     * This handler has an addition attribute to pass in a buffer with data,
     * it uses to initially open.
     *
     * @param factory
     * @param identifier
     * @return
     * @param <T>
     */
    private static <T extends ScreenHandler> ExtendedScreenHandlerType<T> register(ExtendedScreenHandlerType.ExtendedFactory factory, String identifier) {
        ExtendedScreenHandlerType type = new ExtendedScreenHandlerType<>(factory);
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(Minelabs.MOD_ID, identifier), type);
    }


    /**
     * Main class method
     * Registers all Screens
     */
    public static void onInitialize() {
    }
}
