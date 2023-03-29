package it.unibo.caesena.controller;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import it.unibo.caesena.model.tile.Tile;
import it.unibo.caesena.model.tile.TileFactory;
import it.unibo.caesena.model.tile.TileType;
import it.unibo.caesena.utils.ResourceUtil;

public class ConfigurationLoader {

    private final List<Tile> tiles = new ArrayList<>();
    private final String fileName;
    
    public ConfigurationLoader(final String fileName) {
        this.fileName = fileName;
    }

    public final List<Tile> getTiles(final TileFactory factory) {
        try {
            final Object fileJson = new JSONParser()
                    .parse(new InputStreamReader(ResourceUtil.getInputStreamFromFile(fileName, List.of())));
            final JSONObject jsonObject = (JSONObject) fileJson;
            final JSONArray array = (JSONArray) jsonObject.get("Tiles");
            for (int i = 0; i < array.size(); i++) {
                final JSONObject object = (JSONObject) array.get(i);
                for (final var key : object.keySet()) {
                    for (int j = 0; j < Integer.parseInt(object.get(key).toString()); j++) {
                        tiles.add(TileType.valueOf(key.toString()).createTile(factory));
                    }
                }
            }

            Collections.shuffle(tiles);

            for (int i = 0; i < tiles.size(); i++) {
                if (tiles.get(i).getTileType().equals(TileType.valueOf(jsonObject.get("Starting Tile").toString()))) {
                    final Tile firstTile = tiles.get(i);
                    final Tile currentTile = tiles.get(0);
                    tiles.set(i, currentTile);
                    tiles.set(0, firstTile);
                }
            }

            return this.tiles;
        } catch (final Exception e) {
            throw new IllegalStateException("Error reading tiles from file, maybe it's missing", e);
        }
    }

}
