package net.nunnerycode.bukkit.mythicdrops.ruins;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.FilenameException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.desht.dhutils.TerrainManager;

public final class RuinsWrapper implements Listener {

  private List<File> schematicFiles;
  private WorldEditPlugin worldEditPlugin;

  public RuinsWrapper() {
    schematicFiles = new ArrayList<>();
    worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
  }

  public void addSchematicFile(File file) {
    schematicFiles.add(file);
  }

  @EventHandler
  public void onChunkGenerate(ChunkPopulateEvent event) {
    TerrainManager terrainManager = getTerrainManager(event.getWorld());
    try {
      terrainManager.loadSchematic(schematicFiles.get(RandomUtils.nextInt(schematicFiles.size())),
                                   event.getChunk().getBlock(7, 64, 7).getLocation());
    } catch (FilenameException | DataException | IOException | MaxChangedBlocksException | EmptyClipboardException e) {
      e.printStackTrace();
    }
  }

  public TerrainManager getTerrainManager(World world) {
    return new TerrainManager(worldEditPlugin, world);
  }

}
