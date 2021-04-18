package kaptainwutax.minemap.config;

import com.google.gson.annotations.Expose;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.mcutils.version.MCVersion;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SaltsConfig extends Config {
    // can't use the enum as the key as it is not correctly deserialized by GSON
    @Expose
    protected Map<String, Map<String, Integer>> SALTS = new LinkedHashMap<>();
    @Expose
    protected Map<String, Map<String, Integer>> OVERRIDES = new LinkedHashMap<>();

    public Map<String, Map<String, Integer>> getAllDefaultSalts() {
        return SALTS;
    }

    public Map<String, Map<String, Integer>> getAllSalts() {
        Map<String, Map<String, Integer>> salts = new LinkedHashMap<>();
        for (String s : SALTS.keySet()) {
            salts.put(s, getSalts(s));
        }
        return salts;
    }

    public Map<String, Map<String, Integer>> getAllOverridesSalts() {
        return OVERRIDES;
    }

    public Map<String, Integer> getDefaultSalts(MCVersion version) {
        return SALTS.getOrDefault(version.toString(), null);
    }

    public Map<String, Integer> getSalts(MCVersion version) {
        return getSalts(version.toString());
    }

    private Map<String, Integer> getSalts(String version) {
        if (SALTS.containsKey(version)) {
            Map<String, Integer> salts = new LinkedHashMap<>(SALTS.get(version));
            if (OVERRIDES.containsKey(version)) {
                Map<String, Integer> overrides = OVERRIDES.get(version);
                for (String s : overrides.keySet()) {
                    salts.put(s, overrides.get(s));
                }
            }
            return salts;
        }
        return null;
    }

    public Map<String, Integer> getOverrides(MCVersion version) {
        return OVERRIDES.getOrDefault(version.toString(), null);
    }

    /* user generated salts */
    public Integer getSalt(MCVersion version, String name) {
        if (OVERRIDES.containsKey(version.toString()) && OVERRIDES.get(version.toString()).containsKey(name)) {
            return OVERRIDES.get(version.toString()).get(name);
        }
        if (SALTS.containsKey(version.toString()) && SALTS.get(version.toString()).containsKey(name)) {
            return SALTS.get(version.toString()).get(name);
        }
        return null;
    }

    public Integer getDefaultSalt(MCVersion version, String name) {
        return (SALTS.containsKey(version.toString()) && SALTS.get(version.toString()).containsKey(name)) ?
                SALTS.get(version.toString()).get(name) : null;
    }

    public Integer getOverride(MCVersion version, String name) {
        return (OVERRIDES.containsKey(version.toString()) && OVERRIDES.get(version.toString()).containsKey(name)) ?
                OVERRIDES.get(version.toString()).get(name) : null;
    }

    @Override
    public String getName() {
        return "salts";
    }

    @Override
    protected void resetConfig() {
        for (MCVersion version : MCVersion.values()) {
            this.resetConfig(version);
        }
    }

    private void resetConfig(MCVersion version) {
        this.addDefaultEntry(version, Structure.getName(BastionRemnant.class), BastionRemnant.CONFIGS.getAsOf(version) == null ? null : BastionRemnant.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(BuriedTreasure.class), BuriedTreasure.CONFIGS.getAsOf(version) == null ? null : BuriedTreasure.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(DesertPyramid.class), DesertPyramid.CONFIGS.getAsOf(version) == null ? null : DesertPyramid.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(EndCity.class), EndCity.CONFIGS.getAsOf(version) == null ? null : EndCity.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(Fortress.class), Fortress.CONFIGS.getAsOf(version) == null ? null : Fortress.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(Igloo.class), Igloo.CONFIGS.getAsOf(version) == null ? null : Igloo.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(JunglePyramid.class), JunglePyramid.CONFIGS.getAsOf(version) == null ? null : JunglePyramid.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(Mansion.class), Mansion.CONFIGS.getAsOf(version) == null ? null : Mansion.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(Monument.class), Monument.CONFIGS.getAsOf(version) == null ? null : Monument.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(NetherFossil.class), NetherFossil.CONFIGS.getAsOf(version) == null ? null : NetherFossil.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(OceanRuin.class), OceanRuin.CONFIGS.getAsOf(version) == null ? null : OceanRuin.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(PillagerOutpost.class), PillagerOutpost.CONFIGS.getAsOf(version) == null ? null : PillagerOutpost.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(RuinedPortal.class), RuinedPortal.OVERWORLD_CONFIGS.getAsOf(version) == null ? null : RuinedPortal.OVERWORLD_CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(Shipwreck.class), Shipwreck.CONFIGS.getAsOf(version) == null ? null : Shipwreck.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(SwampHut.class), SwampHut.CONFIGS.getAsOf(version) == null ? null : SwampHut.CONFIGS.getAsOf(version).salt);
        this.addDefaultEntry(version, Structure.getName(Village.class), Village.CONFIGS.getAsOf(version) == null ? null : Village.CONFIGS.getAsOf(version).salt);
    }

    public void resetOverrides(MCVersion version){
        if (this.OVERRIDES.containsKey(version.toString())){
            this.OVERRIDES.get(version.toString()).clear();
            this.OVERRIDES.remove(version.toString());
        }
    }

    private void addDefaultEntry(MCVersion version, String name, Integer salt) {
        Map<String, Integer> saltMap = this.SALTS.computeIfAbsent(version.toString(), s -> new LinkedHashMap<>());
        if (salt != null) {
            saltMap.put(name, salt);
        }
    }

    public void flush() {
        try {
            this.writeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addOverrideEntry(MCVersion version, String name, Integer salt) {
        Map<String, Integer> saltMap = this.OVERRIDES.computeIfAbsent(version.toString(), s -> new LinkedHashMap<>());
        if (salt != null) {
            saltMap.put(name, salt);
        }
    }
}
