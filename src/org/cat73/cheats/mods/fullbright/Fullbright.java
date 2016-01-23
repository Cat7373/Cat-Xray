package org.cat73.cheats.mods.fullbright;

import org.cat73.cheats.mods.Mod;
import org.cat73.cheats.mods.ModInfo;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

@ModInfo(name="Fullbright")
public class Fullbright extends Mod {
    private final GameSettings gameSettings;
    private float gamma;
    private int cooldown;
    
    public Fullbright() {
        this.gameSettings = Mod.minecraft.gameSettings;
    }

    @Override
    public void onEnable() {
        setGamma();
        this.gameSettings.gammaSetting = 16.0F;
        this.cooldown = 0;

        FMLCommonHandler.instance().bus().register(this);
    }
    
    @Override
    public void onDisable() {
        FMLCommonHandler.instance().bus().unregister(this);

        this.gameSettings.gammaSetting = this.gamma;
    }
    
    @Override
    public void onFirstTick() {
        // 如果上次退出游戏的时候仍然开着夜视功能, 则在这里恢复到正常的 Gamma
        setGamma();
        this.gameSettings.gammaSetting = this.gamma;
    }
    
    @SubscribeEvent
    public void onTickInGame(final ClientTickEvent event) {
        if(event.phase != Phase.END) {
            return;
        }

        if(this.cooldown-- == 0) {
            final EntityPlayerSP player = Mod.minecraft.thePlayer;
            if(player != null) {
                player.removePotionEffectClient(Potion.blindness.id);
                player.removePotionEffectClient(Potion.confusion.id);
            }
            
            this.cooldown = 10;
        }
    }
    
    private void setGamma() {
        this.gamma = this.gameSettings.gammaSetting;
        this.gamma = this.gamma > 1.0F ? 1.0F : this.gamma;
    }
}
