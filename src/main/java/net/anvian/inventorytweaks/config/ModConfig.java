package net.anvian.inventorytweaks.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.SectionHeader;
import net.anvian.inventorytweaks.InventoryTweak;
import net.anvian.inventorytweaks.sort.layout.ArrangeLayout;
import net.anvian.inventorytweaks.sort.layout.LeftRightLayout;
import net.minecraft.screen.ScreenHandler;

@SuppressWarnings("unused")
@Modmenu(modId = InventoryTweak.MOD_ID)
@Config(name = InventoryTweak.MOD_ID + "-config", wrapperName = "InventoryTweakConfig")
public class ModConfig {
    @SectionHeader("sortingInventorySection")
    public boolean activateSortingInventory = true;
    public SortType sortType = SortType.TYPE;
    public ArrangeLayoutType arrangeLayoutType = ArrangeLayoutType.DISABLED;

    public enum SortType {
        TYPE, NAME, RARITY
    }

    @SectionHeader("durabilityWarningSection")
    public boolean activateDurabilityWarning = true;
    @RangeConstraint(min = 0, max = 100)
    public byte percentageDurabilityWarning = 10;
    public boolean activateDurabilityWarningSound = true;
    @RangeConstraint(min = 0, max = 100)
    public int durabilityWarningSoundVolume = 100;

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
