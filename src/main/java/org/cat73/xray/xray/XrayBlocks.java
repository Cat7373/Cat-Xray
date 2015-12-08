package org.cat73.xray.xray;

import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;

public class XrayBlocks {
    private static final String[] defaultBlocks = new String[]{
        "0 0 128 200 -1 minecraft:lapis_ore",
        "255 0 0 200 -1 minecraft:redstone_ore",
        "255 255 0 200 -1 minecraft:gold_ore",
        "0 255 0 200 -1 minecraft:emerald_ore",
        "0 191 255 200 -1 minecraft:diamond_ore"
    };
    private static final HashMap<String, XrayBlocks> blocks = new HashMap<String, XrayBlocks>();

    public final byte meta;
    public final byte r;
    public final byte g;
    public final byte b;
    public final byte a;

    private XrayBlocks(byte meta, byte r, byte g, byte b, byte a) {
        this.meta = meta;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    private static void fromString(final String s) {
        if(!s.startsWith("//")) {
            final String[] info = s.split(" ");

            final byte meta = Byte.parseByte(info[1]);
            final byte r = (byte) Integer.parseInt(info[2]);
            final byte g = (byte) Integer.parseInt(info[3]);
            final byte b = (byte) Integer.parseInt(info[4]);
            final byte a = (byte) Integer.parseInt(info[5]);

            blocks.put(info[0], new XrayBlocks(meta, r, g, b, a));
        }
    }

    public static void load(final Configuration config) {
        final String[] configBlocksList = config.getStringList("Blocks", "Xray", defaultBlocks, "Blocks for X-ray");
        blocks.clear();
        for(final String configBlock : configBlocksList) {
            XrayBlocks.fromString(configBlock);
        }
    }
    
    public static XrayBlocks find(String name) {
        return blocks.get(name);
    }
}
