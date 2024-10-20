package net.anvian.inventorytweaks.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import net.anvian.inventorytweaks.InventoryTweak;
import net.anvian.inventorytweaks.sort.layout.ArrangeLayout;
import net.anvian.inventorytweaks.sort.layout.LeftRightLayout;
import net.minecraft.screen.ScreenHandler;

@SuppressWarnings("unused")
@Modmenu(modId = InventoryTweak.MOD_ID)
@Config(name = "inventorytweak-config", wrapperName = "InventoryTweakConfig")
public class ModConfig {
    public SortType sortType = SortType.NAME;
    public ArrangeLayoutType arrangeLayoutType = ArrangeLayoutType.LEFT_TO_RIGHT;

    public enum SortType {
        NAME, TYPE
    }

    public enum ArrangeLayoutType {
        DISABLED(null),
        LEFT_TO_RIGHT(LeftRightLayout.class),
//        RIGHT_TO_LEFT,
//        TOP_TO_BOTTOM,
//        BOTTOM_TO_TOP
        ;

        private final Class<? extends ArrangeLayout> arrangeLayoutClass;

        ArrangeLayoutType(Class<? extends ArrangeLayout> arrangeLayoutClass) {
            this.arrangeLayoutClass = arrangeLayoutClass;
        }

        public ArrangeLayout createArrangeLayout(ScreenHandler screenHandler) {
            if (arrangeLayoutClass == null) return null;
            try {
                return arrangeLayoutClass.getConstructor(ScreenHandler.class).newInstance(screenHandler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
