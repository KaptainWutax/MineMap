package kaptainwutax.minemap.ui.map.icon;

import kaptainwutax.featureutils.Feature;
import kaptainwutax.featureutils.structure.RegionStructure;
import kaptainwutax.featureutils.structure.Structure;
import kaptainwutax.minemap.MineMap;
import kaptainwutax.minemap.init.Configs;
import kaptainwutax.minemap.ui.map.MapContext;
import kaptainwutax.minemap.ui.map.fragment.Fragment;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.pos.BPos;
import kaptainwutax.seedutils.mc.pos.CPos;

import java.util.List;
import java.util.concurrent.Callable;

public class RegionIcon extends StaticIcon {

    public RegionIcon(MapContext context) {
        super(context);
    }

    @Override
    public boolean isValidFeature(Feature<?, ?> feature) {
        return feature instanceof RegionStructure;
    }

    @Override
    public void addPositions(Feature<?, ?> feature, Fragment fragment, List<BPos> positions) {
        RegionStructure<?, ?> structure = (RegionStructure<?, ?>)feature;
        int increment = 16 * structure.getSpacing();
        ChunkRand rand = new ChunkRand();

        long worldSeedWithSalt=this.getContext().worldSeed;
        if (Configs.SALTS.getSalt(this.getContext().version,feature.getName())!=null){
            worldSeedWithSalt-=Configs.SALTS.getDefaultSalt(this.getContext().version,feature.getName());
            worldSeedWithSalt+=Configs.SALTS.getSalt(this.getContext().version,feature.getName());
        }
        for(int x = fragment.getX() - increment; x < fragment.getX() + fragment.getSize() + increment; x += increment) {
            for(int z = fragment.getZ() - increment; z < fragment.getZ() + fragment.getSize() + increment; z += increment) {
                RegionStructure.Data<?> data = structure.at(x >> 4, z >> 4);
                CPos pos = structure.getInRegion(worldSeedWithSalt, data.regionX, data.regionZ, rand);

                if(pos != null && structure.canSpawn(pos.getX(), pos.getZ(), this.getContext().getBiomeSource())) {
                    positions.add(pos.toBlockPos().add(9, 0, 9));
                }
            }
        }
    }

}
