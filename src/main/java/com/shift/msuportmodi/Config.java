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
        public final ConfigValue<Integer> walletModusSize;

        private Server(ModConfigSpec.Builder builder)
        {
            builder.push("wallet");
            blacklistedEntities = builder.comment("A list of entities not allowed to be captchalogued by wallet modus. This accepts any entity ID or word, for example \"minestuck\" will blacklist all minestuck entities. If you need to blacklist a Tag, create a datapack swapping: msuportmodi\\tags\\entity_type\\blacklisted.json")
                    .define("blacklistedEntities", new ArrayList<>(Arrays.asList("minecraft:wither", "minecraft:ender_dragon")));
            blacklistedBlockEntities = builder.comment("A list of block entities not allowed to be captchalogued by wallet modus.")
                    .define("blacklistedBlockEntities", new ArrayList<>(List.of("lootr")));
            walletModusSize = builder.comment("Max size for wallet modus. By default, this value is set at 10.")
                    .define("walletModusSize", 3);
            builder.pop();
        }
        //Arrays.asList("lootr")
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