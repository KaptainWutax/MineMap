package kaptainwutax.minemap.feature;

import kaptainwutax.featureutils.structure.Fortress;
import kaptainwutax.mcutils.state.Dimension;
import kaptainwutax.mcutils.version.MCVersion;

public class OWFortress extends Fortress {

	public OWFortress(MCVersion version) {
		super(version);
	}

	public OWFortress(Config config, MCVersion version) {
		super(config, version);
	}

	@Override
	public String getName() {
		return "OW_fortress";
	}

	@Override
	public boolean isValidDimension(Dimension dimension) {
		return dimension == Dimension.OVERWORLD;
	}

}
