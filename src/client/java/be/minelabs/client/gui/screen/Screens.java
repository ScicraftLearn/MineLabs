package be.minelabs.client.gui.screen;

import be.minelabs.screen.ScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class Screens {

    public static void onInitializeClient(){
        HandledScreens.register(ScreenHandlers.LEWIS_SCREEN_HANDLER, LewisScreen::new);
        HandledScreens.register(ScreenHandlers.IONIC_SCREEN_HANDLER, IonicScreen::new);
        HandledScreens.register(ScreenHandlers.LAB_CHEST_SCREEN_HANDLER, LabChestScreen::new);
        HandledScreens.register(ScreenHandlers.CHARGED_POINT_SCREEN_HANDLER, ChargedPointScreen::new);
        HandledScreens.register(ScreenHandlers.ATOM_STORAGE_SCREEN_HANDLER, AtomStorageScreen::new);
    }

}
