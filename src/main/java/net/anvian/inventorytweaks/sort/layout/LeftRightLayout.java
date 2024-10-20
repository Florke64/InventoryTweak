package net.anvian.inventorytweaks.sort.layout;

import net.anvian.inventorytweaks.slots.ContainerSlots;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Map;
import java.util.TreeMap;

public class LeftRightLayout extends ArrangeLayout {

    public static final int BOX_SIZE = 3;

    private final int maxBoxes;
    private final int containerSlotsAmount;

    public LeftRightLayout(ScreenHandler screenHandler) {
        super(screenHandler);
        this.containerSlotsAmount = ContainerSlots.get().size();
        this.maxBoxes = (int) Math.floor((double) this.containerSlotsAmount / (double) BOX_SIZE);
    }

    public Map<Integer, Integer> apply() {

        // Map<Integer, Integer> slotsMap = <[Sorting Target Slot], [Item Source Slot]>
        Map<Integer, Integer> slotsMap = new TreeMap<>();

        int boxId = -1, subSlot = -1, countedItems = 0;
        Item lastItemStack = Items.AIR;
        for (Slot containerSlot : screenHandler.slots) {
            if (boxId >= maxBoxes - 1) break;

            final Item itemStackItem = containerSlot.getStack().getItem();
            if (itemStackItem.equals(Items.AIR)) continue;

            if (countedItems > (maxBoxes - boxId + 1) * BOX_SIZE) {
                if (subSlot >= BOX_SIZE) {
                    boxId++;
                    subSlot = 0;
                }

                int targetSlot = getBoxedSlot(boxId, subSlot);
                slotsMap.put(targetSlot, containerSlot.getIndex());
                subSlot++;
                continue;
            } else if (!lastItemStack.equals(itemStackItem)) {
                lastItemStack = itemStackItem;
                countedItems++;
                boxId++;
                subSlot = 0;
            } else if (subSlot >= BOX_SIZE) {
                boxId++;
                subSlot = 0;
            }

            int targetSlot = getBoxedSlot(boxId, subSlot);
            slotsMap.put(targetSlot, containerSlot.getIndex());
            subSlot++;
        }

        return slotsMap;
    }

    private int getBoxedSlot(int boxId, int subSlot) {
        if (subSlot >= BOX_SIZE)
            throw new IndexOutOfBoundsException("subSlot is greater than boxSize");

        int slotZero = boxId * BOX_SIZE;
        int targetSlot = slotZero + subSlot;

        if (targetSlot >= this.containerSlotsAmount)
            throw new IndexOutOfBoundsException("targetSlot is greater than containerSlotsAmount");

        return targetSlot;
    }

}
