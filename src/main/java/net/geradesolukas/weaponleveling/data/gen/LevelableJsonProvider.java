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


    private final DataGenerator dataProvider;
    private final String modid;
    private final String itemname;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static Map<ResourceLocation, LevelableItem> levelableItems = ImmutableMap.of();

    //private final ArrayList<LevelableItem> levelableItems = new ArrayList<>();

    public LevelableJsonProvider(DataGenerator dataProvider, String modid, String itemname) {
        this.dataProvider = dataProvider;
        this.modid = modid;
        this.itemname = itemname;
    }



    @Override
    public void run(HashCache pCache) throws IOException {
        List<ResourceLocation> entries = new ArrayList<>();
        levelableItems.forEach((resourceLocation, levelableItem) -> {
            entries.add(levelableItem.getItem().getRegistryName());
            Path path = this.dataProvider.getOutputFolder().resolve("data/" + levelableItem.getItem().getRegistryName().getNamespace() + "/levelable_items/" + levelableItem.getItem().getRegistryName().getPath() + ".json");
            JsonObject jsonObject = new JsonObject();

        });


        //Path path = this.dataProvider.getOutputFolder();

        //saveJson(pCache,);

        //levelableItems.forEach(sound -> {
        //    //try {
        //    //    LevelableItem levelableItem = levelableItems.get(1)
        //    //    DataProvider.save(GSON, pCache, levelableItems.);
        //    //}
        //});
    }

    private static void saveJson(HashCache pCache, JsonObject itemJson, Path pPath) {

    }

    @Override
    public String getName() {
        return null;
    }
}
