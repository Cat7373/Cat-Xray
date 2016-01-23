package org.cat73.cheats.mods;

import java.util.Collection;
import java.util.HashMap;

import org.cat73.cheats.config.Hotkey;
import org.cat73.cheats.mods.creategive.CreateGive;
import org.cat73.cheats.mods.freecam.FreeCam;
import org.cat73.cheats.mods.fullbright.Fullbright;
import org.cat73.cheats.mods.xray.Xray;
import org.cat73.cheats.reference.Reference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

/**
 * MOD 管理器
 * @author Cat73
 */
public final class ModManager {
    public final static Minecraft minecraft = Minecraft.getMinecraft();

    /** MOD 列表 */
    private final static HashMap<String, Mod> mods = new HashMap<String, Mod>();
    /** 热键列表 */
    private final static HashMap<Mod, KeyBinding> hotkeys = new HashMap<Mod, KeyBinding>();
    
    /**
     * 初始化 Mod 管理器
     */
    public ModManager() {
        // 注册热键事件
        FMLCommonHandler.instance().bus().register(this);

        // 注册 Mod
        registerMod(new Xray());
        registerMod(new Fullbright());
        registerMod(new FreeCam());
        registerMod(new CreateGive());
        
        // 注册热键
        registerHotkeys();
        
        // 注册 FirstTick 通知器
        registerFirstTick();
    }

    /**
     * 注册 Mod
     * @param mod 要被注册的 Mod
     */
    private void registerMod(final Mod mod) {
        mods.put(mod.name, mod);
    }
    
    /**
     * 注册热键
     */
    private void registerHotkeys() {
        // 获取配置文件里保存的热键列表
        final HashMap<String, Integer> hotkeys = Hotkey.getHotkeys();
        
        // 循环注册每一个 Mod
        for(String key : ModManager.mods.keySet()) {
            // 获取一个 Mod 对象
            final Mod mod = getMod(key);
            // 获取配置文件里保存的该 Mod 的热键
            Integer keyCode = hotkeys.get(key);
            
            // 如果该 Mod 在 Gui 中显示才注册热键
            if(mod.shouInGui) {
                // 如果配置文件里没有保存该 Mod 的热键则使用 Mod 的默认热键
                if(keyCode == null) {
                    keyCode = mod.defaultHotkey;
                }
                
                // 注册热键
                final KeyBinding hotkey = new KeyBinding(mod.name, keyCode, Reference.NAME);
                ModManager.hotkeys.put(mod, hotkey);
            }
        }
    }
    
    /**
     * 注册 FirstTick 通知器
     */
    private void registerFirstTick() {
        // FirstTick 通知
        FMLCommonHandler.instance().bus().register(new Object() {
            @SubscribeEvent
            public void onTickInGame(final ClientTickEvent event) {
                FMLCommonHandler.instance().bus().unregister(this);

                // 执行每一个 Mod 的 onFirstTick
                for(final Mod mod : ModManager.mods.values()) {
                    mod.onFirstTick();
                }
            }
        });
    }

    /**
     * 通过 Mod 的名称获取 Mod
     * @param name 要被获取的 Mod 的名称
     * @return 该名称对应的 Mod, 如果名称不存在则返回 null
     */
    public static Mod getMod(String name) {
        return mods.get(name);
    }
    
    /**
     * 获取所有 Mod
     * @return 所有 Mod
     */
    public static Collection<Mod> getMods() {
        return mods.values();
    }
    
    /**
     * 获取 Mod 的总数
     * @return Mod 的总数
     */
    public static int getSize() {
        return mods.size();
    }
    
    /**
     * 给一个 Mod 设置热键
     * @param mod 要被设置热键的 Mod
     * @param key 要设置的热键值
     */
    public static void setHotKey(Mod mod, int key) {
        // 重新设置热键
        final KeyBinding hotkey = ModManager.hotkeys.get(mod);
        hotkey.setKeyCode(key);
        KeyBinding.resetKeyBindingArrayAndHash();
        
        // 将新的热键保存进热键列表
        final HashMap<String, Integer> hotkeys = Hotkey.getHotkeys();
        hotkeys.put(mod.name, key);
        
        // 将新的热键保存到配置文件
        Hotkey.save();
    }
    
    /**
     * 获取指定 Mod 的热键
     * @param mod 要获取热键的 Mod
     * @return 指定 Mod 的热键
     */
    public static int getHotkey(Mod mod) {
        KeyBinding key = ModManager.hotkeys.get(mod);
        return key.getKeyCode();
    }
    
    /**
     * 按键触发 通过 Mod 的热键来开关 Mod
     */
    @SubscribeEvent
    public void keyboardEvent(final KeyInputEvent event) {
        if (ModManager.minecraft.currentScreen == null) {
            for(Mod mod : hotkeys.keySet()) {
                if(ModManager.hotkeys.get(mod).isPressed()) {
                    mod.toggle();
                }
            }
        }
    }
}
