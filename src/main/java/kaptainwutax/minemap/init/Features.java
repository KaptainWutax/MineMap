package kaptainwutax.minemap.init;

import kaptainwutax.featureutils.Feature;
import kaptainwutax.featureutils.decorator.EndGateway;
import kaptainwutax.featureutils.misc.SlimeChunk;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.minemap.feature.OWBastionRemnant;
import kaptainwutax.minemap.feature.OWFortress;
import kaptainwutax.minemap.feature.SpawnPoint;
import kaptainwutax.minemap.ui.map.fragment.FeatureFactory;
import kaptainwutax.mcutils.version.MCVersion;

import java.util.HashMap;
import java.util.Map;

public class Features {

    public static final Map<Class<? extends Feature<?, ?>>, FeatureFactory<?>> REGISTRY = new HashMap<>();

    public static void registerFeatures() {
        register(BastionRemnant.class, BastionRemnant::new);
        register(BuriedTreasure.class, BuriedTreasure::new);
        register(DesertPyramid.class, DesertPyramid::new);
        register(EndCity.class, EndCity::new);
        register(Fortress.class, Fortress::new);
        register(Igloo.class, Igloo::new);
        register(JunglePyramid.class, JunglePyramid::new);
        register(Mansion.class, Mansion::new);
        register(Mineshaft.class, Mineshaft::new);
        register(Monument.class, Monument::new);
        register(NetherFossil.class, NetherFossil::new);
        register(OceanRuin.class, OceanRuin::new);
        register(PillagerOutpost.class, PillagerOutpost::new);
        register(RuinedPortal.class, RuinedPortal::new);
        register(Shipwreck.class, Shipwreck::new);
        register(SwampHut.class, SwampHut::new);
        register(Village.class, Village::new);
        register(Stronghold.class, Stronghold::new);

        register(OWBastionRemnant.class, OWBastionRemnant::new);
        register(OWFortress.class, OWFortress::new);

        register(EndGateway.class, EndGateway::new);
        register(SlimeChunk.class, SlimeChunk::new);

        register(SpawnPoint.class, SpawnPoint::new);
    }
    
    public static <T extends Feature<?, ?>> void register(Class<T> clazz, FeatureFactory<T> factory) {
        REGISTRY.put(clazz, factory);
    }

    public static Map<Class<? extends Feature<?, ?>>, Feature<?, ?>> getForVersion(MCVersion version) {
        Map<Class<? extends Feature<?, ?>>, Feature<?, ?>> result = new HashMap<>();

        for(Map.Entry<Class<? extends Feature<?, ?>>, FeatureFactory<?>> entry: REGISTRY.entrySet()) {
            try {
                Feature<?, ?> feature = entry.getValue().create(version);

                if(feature.getConfig() != null) {
                    result.put(entry.getKey(), feature);
                }
            } catch(NullPointerException ignored) {
            }
        }

        return result;
    }

}
