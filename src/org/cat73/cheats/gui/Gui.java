package org.cat73.cheats.gui;

import org.cat73.cheats.gui.main.Gui_Main;
import org.cat73.cheats.mods.Mod;
import org.cat73.cheats.reference.Names;
import org.cat73.cheats.reference.Reference;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class Gui {
    public final static Minecraft minecraft = Minecraft.getMinecraft();

    private final KeyBinding guiKey;

    public Gui() {
        FMLCommonHandler.instance().bus().register(this);
        this.guiKey = new KeyBinding(Names.Keys.GUI, Keyboard.KEY_F6, Reference.NAME);

        ClientRegistry.registerKeyBinding(this.guiKey);
    }

    private void enabledGui() {
        Gui.minecraft.displayGuiScreen(new Gui_Main(Mod.minecraft.currentScreen));
    }

    @SubscribeEvent
    public void keyboardEvent(final KeyInputEvent event) {
        if (Gui.minecraft.currentScreen == null) {
            if (this.guiKey.isPressed()) {
                this.enabledGui();
            }
        }
    }
}
