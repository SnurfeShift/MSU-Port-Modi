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

        private Server(ModConfigSpec.Builder builder)
        {
            builder.push("entities");
            blacklistedEntities = builder.comment("A list of entity IDs that are NOT allowed to be captchalogued.")
                    .define("blacklistedEntities", new ArrayList<>(Arrays.asList("minecraft:cow", "minecraft:chicken")));
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