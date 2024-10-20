package net.anvian.inventorytweaks.sort.layout;

import net.minecraft.screen.ScreenHandler;

import java.util.Map;

public abstract class ArrangeLayout {

    final ScreenHandler screenHandler;

    public ArrangeLayout (ScreenHandler screenHandler) {
        this.screenHandler = screenHandler;
    }

    public abstract Map<Integer, Integer> apply();

}
