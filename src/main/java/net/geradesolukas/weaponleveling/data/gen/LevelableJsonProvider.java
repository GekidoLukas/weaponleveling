package net.geradesolukas.weaponleveling.data.gen;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.geradesolukas.weaponleveling.data.LevelableItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LevelableJsonProvider implements DataProvider {

// UNUSED
    private final DataGenerator dataProvider;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Map<ResourceLocation, LevelableItem> levelableItems = ImmutableMap.of();


    public LevelableJsonProvider(DataGenerator dataProvider) {
        this.dataProvider = dataProvider;
    }



    @Override
    public void run(HashCache pCache) throws IOException {
        List<ResourceLocation> entries = new ArrayList<>();
        levelableItems.forEach((resourceLocation, levelableItem) -> {
            entries.add(levelableItem.getItem().getRegistryName());
            Path path = this.dataProvider.getOutputFolder().resolve("data/" + levelableItem.getItem().getRegistryName().getNamespace() + "/levelable_items/" + levelableItem.getItem().getRegistryName().getPath() + ".json");
            JsonObject jsonObject = new JsonObject();
            if (!this.levelableItems.isEmpty())
            {
                try {
                    this.save(pCache,jsonObject,path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        });

    }



    @Override
    public String getName() {
        return "Levelable Items";
    }

    private void save(HashCache pCache, JsonObject itemJson, Path pPath) throws IOException {
        DataProvider.save(GSON,pCache, this.mapToJson(this.levelableItems), pPath);
    }

    private JsonObject mapToJson(final Map<ResourceLocation, LevelableItem> map)
    {
        final JsonObject obj = new JsonObject();
        // namespaces are ignored when serializing
        map.forEach((k, v) -> obj.add(k.getPath(), v.serialize()));
        return obj;
    }
}
