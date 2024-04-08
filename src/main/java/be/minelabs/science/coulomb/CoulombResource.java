package be.minelabs.science.coulomb;

import be.minelabs.Minelabs;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoulombResource implements SimpleSynchronousResourceReloadListener {

    public static CoulombResource INSTANCE = new CoulombResource();

    private final Map<Identifier, CoulombData> resource_data;

    public CoulombResource() {
        resource_data = new HashMap<>();
    }

    public CoulombData getCoulombData(String name) {
        Identifier identifier = new Identifier(Minelabs.MOD_ID, "science/coulomb/" + name.toLowerCase() + ".json");
        for (Identifier id : resource_data.keySet()) {
            if (id.equals(identifier)) {
                return getCoulombData(id);
            }
        }
        return null;
    }

    public CoulombData getCoulombData(Identifier identifier) {
        return resource_data.get(identifier);
    }

    public Map<Identifier, CoulombData> getResourceData() {
        return Collections.unmodifiableMap(resource_data);
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier(Minelabs.MOD_ID, "coulumb_resources");
    }

    @Override
    public void reload(ResourceManager manager) {
        Map<Identifier, Resource> data = manager.findResources("science/coulomb", (i) -> true);

        for (Identifier id : data.keySet()) {
            try {
                InputStream inputStream = manager.open(id);
                CoulombData json = new Gson().fromJson(JsonParser.parseReader(new InputStreamReader(inputStream)), CoulombData.class);
                json.validate();

                resource_data.put(id, json);
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
