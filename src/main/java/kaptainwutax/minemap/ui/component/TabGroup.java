package kaptainwutax.minemap.ui.component;

import kaptainwutax.minemap.ui.map.MapPanel;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TabGroup {
	private final MCVersion version;
	private long worldSeed;

	protected Map<Dimension, MapPanel> mapPanels = new LinkedHashMap<>();

	public TabGroup(MCVersion version, String worldSeed, int threadCount) {
		this(version, worldSeed, threadCount, Dimension.values());
	}
	public TabGroup(MCVersion version, String worldSeed, int threadCount, Dimension[] dimensions) {
		this.version = version;

		if(worldSeed.isEmpty()) {
			this.loadSeed(new Random().nextLong(), threadCount, dimensions);
			return;
		}
		try {
			this.loadSeed(Long.parseLong(worldSeed), threadCount, dimensions);
		} catch(NumberFormatException e) {
			this.loadSeed(worldSeed.hashCode(), threadCount, dimensions);
		}

	}

	public MCVersion getVersion() {
		return this.version;
	}

	public long getWorldSeed() {
		return this.worldSeed;
	}

	public Collection<MapPanel> getMapPanels() {
		return this.mapPanels.values();
	}

	private void loadSeed(long worldSeed, int threadCount, Dimension[] dimensions) {
		this.worldSeed = worldSeed;

		for(Dimension dimension : dimensions) {
			this.mapPanels.put(dimension, new MapPanel(this.getVersion(), dimension, this.worldSeed, threadCount));
		}
	}

	public void add(WorldTabs tabs) {
		String prefix = "[" + this.version + "] ";
		AtomicBoolean first = new AtomicBoolean(true);

		this.mapPanels.forEach((dimension, mapPanel) -> {
			String s = dimension.getName().substring(0, 1).toUpperCase() + dimension.getName().substring(1);
			tabs.addMapTab(prefix + s + " " + this.worldSeed, this, mapPanel);

			if(first.get()) {
				tabs.setSelectedIndex(tabs.getTabCount() - 1);
				first.set(false);
			}
		});
	}

	public void invalidateAll() {
		this.mapPanels.values().forEach(MapPanel::restart);
	}

	public void removeIfPresent(MapPanel mapPanel) {
		this.mapPanels.entrySet().removeIf(e -> e.getValue() == mapPanel);
	}

	public boolean contains(MapPanel mapPanel) {
		return this.mapPanels.containsValue(mapPanel);
	}

	public boolean isEmpty() {
		return this.mapPanels.isEmpty();
	}

}
