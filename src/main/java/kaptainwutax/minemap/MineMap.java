package kaptainwutax.minemap;

import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import kaptainwutax.minemap.init.Configs;
import kaptainwutax.minemap.init.Features;
import kaptainwutax.minemap.init.Icons;
import kaptainwutax.minemap.ui.component.WorldTabs;
import kaptainwutax.minemap.ui.component.Dialog;
import kaptainwutax.minemap.ui.map.MapPanel;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.seed.WorldSeed;
import wearblackallday.swing.Events;
import wearblackallday.swing.components.LMenuBar;
import wearblackallday.util.TriConsumer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MineMap extends JFrame {
	public final WorldTabs worldTabs = new WorldTabs();
	public final Dialog coordHopperDialogue = new Dialog.CoordHopperDialogue(this);
	public final Dialog enterSeedDialog = new Dialog.EnterSeedDialog(this);
	public final Dialog renameTabDialog = new Dialog.RenameTabDialog(this);
	public final Dialog saltDialog = new Dialog.SaltDialog(this);

	public static void main(String[] args) {
		Features.registerFeatures();
		Icons.registerIcons();
		Configs.registerConfigs();
		FlatOneDarkIJTheme.install();
		new MineMap().setVisible(true);
	}

	public MineMap() {
		super("MineMap");

		this.setContentPane(this.worldTabs);
		this.setJMenuBar(new LMenuBar()
			.addMenu("File", lMenu -> lMenu
				.withItem("new from Seed", (menu, item, event) ->
					SwingUtilities.invokeLater(() -> this.enterSeedDialog.setVisible(true)))
				.withItem("Screenshot...", (menu, item, event) -> {
					MapPanel map = this.worldTabs.getSelectedMapPanel();
					if(map == null) return;
					BufferedImage image = map.getScreenshot();

					File dir = new File("screenshots/");
					if(!dir.exists() && !dir.mkdirs()) return;

					try {
						ImageIO.write(image, "png", new File("screenshots/" +
							new SimpleDateFormat("yyyyMMdd_HHmmss")
								.format(Calendar.getInstance().getTime()) + ".png"));
					} catch(IOException exception) {
						exception.printStackTrace();
					}
				}))
			.addMenu("World", lMenu -> lMenu
				.withItem("Go to Coordinates", (menu, item, event) ->
					SwingUtilities.invokeLater(() -> this.coordHopperDialogue.setVisible(true)))
				.withItem("Load Shadow Seed", (menu, item, event) ->
					SwingUtilities.invokeLater(() -> {
						MapPanel map = this.worldTabs.getSelectedMapPanel();
						this.worldTabs.load(
							map.getContext().version,
							String.valueOf(WorldSeed.getShadowSeed(map.getContext().worldSeed)),
							map.threadCount, new Dimension[]{map.getContext().dimension});
					}))
				.withItem("Change Salts", (menu, item, event) ->
					SwingUtilities.invokeLater(() -> this.saltDialog.setVisible(true)))
				.addMenuListener(Events.Menu.onSelected(e -> {
					for(Component c : lMenu.getMenuComponents()) {
						c.setEnabled(this.worldTabs.getSelectedMapPanel() != null);
					}
					})))
			.addMenu("Settings", lMenu -> lMenu
				.subMenu("Style", styleMenu -> {
					ButtonGroup buttonGroup = new ButtonGroup();
					for(String style : Configs.BIOME_COLORS.getStyles()) {
						JRadioButtonMenuItem button = new JRadioButtonMenuItem(style);
						button.addActionListener(e -> {
							Configs.USER_PROFILE.getUserSettings().style = style;
							this.worldTabs.invalidateAll();
							Configs.USER_PROFILE.flush();
						});
						styleMenu.add(button);
						buttonGroup.add(button);
					}
				})
				.withCheckBox("Restrict Maximum Zoom", (menu, checkBox, event) -> {
					Configs.USER_PROFILE.getUserSettings().restrictMaximumZoom = checkBox.getState();
					Configs.USER_PROFILE.flush();
				})
				.subMenu("Fragment Metric", fragmentMenu -> {
					ButtonGroup fragmentGroup = new ButtonGroup();
					fragmentMenu
						.withRadioBox("Euclidean", this.fragmentListener(fragmentGroup))
						.withRadioBox("Manhattan", this.fragmentListener(fragmentGroup))
						.withRadioBox("Chebyshev", this.fragmentListener(fragmentGroup));
				})));

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private TriConsumer<LMenuBar.LMenu, JRadioButtonMenuItem, ActionEvent> fragmentListener(ButtonGroup bg) {
		return (menu, radiobutton, event) -> {
			bg.add(radiobutton);
			Configs.USER_PROFILE.getUserSettings().fragmentMetric = radiobutton.getText();
			Configs.USER_PROFILE.flush();
			MapPanel map = this.worldTabs.getSelectedMapPanel();
			if (map != null) map.scheduler.scheduledModified.set(true);
		};
	}
}
