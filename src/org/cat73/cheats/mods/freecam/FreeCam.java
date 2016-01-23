package org.cat73.cheats.mods.freecam;

import org.cat73.cheats.mods.Mod;
import org.cat73.cheats.mods.ModInfo;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MovementInput;

@ModInfo(name="FreeCam")
public class FreeCam extends Mod {
    private EntityClientPlayerMP player = null;
    private FreeCamPlayer ghostPlayer = null;

    @Override
    public void onEnable() {
        this.player = Mod.minecraft.thePlayer;
        final WorldClient world = Mod.minecraft.theWorld;

        this.ghostPlayer = new FreeCamPlayer(world, player.getGameProfile(), this.player.movementInput);
        this.ghostPlayer.copyLocationAndAnglesFrom(player);

        MovementInput movementInput = new MovementInput();
        movementInput.jump = false;
        movementInput.sneak = false;
        movementInput.moveStrafe = 0.0F;
        movementInput.moveForward = 0.0F;
        this.player.movementInput = movementInput;
        
        world.spawnEntityInWorld(this.ghostPlayer);
        Mod.minecraft.renderViewEntity = this.ghostPlayer;
    }
    
    @Override
    public void onDisable() {
        if(this.player == Mod.minecraft.thePlayer) {
            this.player.movementInput = this.ghostPlayer.movementInput;
        } else {
            this.player = Mod.minecraft.thePlayer;
        }
        
        final WorldClient world = Mod.minecraft.theWorld;

        world.removeEntity(this.ghostPlayer);
        Mod.minecraft.renderViewEntity = player;
        Mod.minecraft.renderGlobal.loadRenderers();
        
        this.player = null;
        this.ghostPlayer = null;
    }
}
