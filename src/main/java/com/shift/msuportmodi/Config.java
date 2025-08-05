package com.shift.msuportmodi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.*;
import org.apache.commons.lang3.tuple.Pair;


public class Config
{
    public static class Server
    {
        public final ConfigValue<List<? extends String>> blacklistedEntities;
        public final ConfigValue<List<? extends String>> blacklistedBlockEntities;


        private Server(ModConfigSpec.Builder builder)
        {
            builder.push("entities");
            blacklistedEntities = builder.comment("A list of entity ID that are NOT allowed to be captchalogued. If you need to blacklist a Tag, create a datapack swapping: msuportmodi\\tags\\entity_type\\blacklisted.json")
                    .define("blacklistedEntities", new ArrayList<>(Arrays.asList("minecraft:wither", "minecraft:ender_dragon")));
            blacklistedBlockEntities = builder.comment("A list of block entity ID that are NOT allowed to be captchalogued.  If you need to blacklist a Tag, create a datapack swapping: msuportmodi\\tags\\block_entity_type\\blacklistedblocks.json")
                    .define("blacklistedBlockEntities", new ArrayList<>(Arrays.asList("minecraft:shulker_box")));
            builder.pop();
        }
    }

    public static final ModConfigSpec serverSpec;
    public static final Server SERVER;

    static
    {
        Pair<Server, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Server::new);
        SERVER = pair.getLeft();
        serverSpec = pair.getRight();
    }
}