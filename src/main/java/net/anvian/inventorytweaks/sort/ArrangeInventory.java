package net.anvian.inventorytweaks.sort;

import net.anvian.inventorytweaks.InventoryTweak;
import net.anvian.inventorytweaks.config.ModConfig;
import net.anvian.inventorytweaks.handler.Interaction;
import net.anvian.inventorytweaks.sort.layout.ArrangeLayout;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class ArrangeInventory {

    private static final Logger logger = Logger.getLogger(InventoryTweak.MOD_ID);

    private final ScreenHandler screenHandler;

    public ArrangeInventory(ScreenHandler screenHandler) {
        this.screenHandler = screenHandler;
    }

    public void apply() {
        // Only chest are supported for now
        if (!(screenHandler instanceof GenericContainerScreenHandler))
            throw new IllegalStateException("ScreenHandler is not a GenericContainerScreenHandler");

        ModConfig.ArrangeLayoutType layoutType = InventoryTweak.CONFIG.arrangeLayoutType();
        ArrangeLayout layout = layoutType.createArrangeLayout(screenHandler);

        if (layoutType == ModConfig.ArrangeLayoutType.DISABLED || layout == null) return;

        Map<Integer, Integer> slotsMap = layout.apply();
        shuffle(slotsMap);
    }

    // Map<Integer, Integer> slotsMap = <[Sorting Target Slot], [Item Source Slot]>
    public static void shuffle(Map<Integer, Integer> slotsMap) {
        shuffle(slotsMap, 0);
    }

    // Map<Integer, Integer> slotsMap = <[Sorting Target Slot], [Item Source Slot]>
    public static void shuffle(Map<Integer, Integer> slotsMap, int n) {
        Map<Integer, Integer> slotsMapCopy = new TreeMap<>(slotsMap);
        List<Integer> keyList = new ArrayList<>(slotsMap.keySet());

        Integer sortingTargetSlot = keyList.get(n);

        int itemSourceSlot = slotsMap.get(sortingTargetSlot);
        Interaction.swapStacks(sortingTargetSlot, itemSourceSlot);

        logger.info("Commanded to swap " + sortingTargetSlot + " with " + itemSourceSlot);

        // Update slotsMap
        for (int key : slotsMap.keySet()) {
            if (!slotsMap.get(key).equals(sortingTargetSlot)) continue;
            slotsMapCopy.put(key, itemSourceSlot);
            break;
        }

        slotsMap.clear();
        slotsMap.putAll(slotsMapCopy);

        // Hey! Recursion.
        if (n < keyList.size() - 1)
            shuffle(slotsMap, n + 1);
    }

}
