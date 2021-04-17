package kaptainwutax.minemap.feature;

import kaptainwutax.featureutils.structure.BastionRemnant;
import kaptainwutax.mcutils.state.Dimension;
import kaptainwutax.mcutils.version.MCVersion;

public class OWBastionRemnant extends BastionRemnant {

	public OWBastionRemnant(MCVersion version) {
		super(version);
	}

	public OWBastionRemnant(Config config, MCVersion version) {
		super(config, version);
	}

	@Override
	public String getName() {
		return "OW_bastion_remnant";
	}

	@Override
	public boolean isValidDimension(Dimension dimension) {
		return dimension == Dimension.OVERWORLD;
	}

}
