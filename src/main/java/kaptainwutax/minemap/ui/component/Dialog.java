package kaptainwutax.minemap.ui.component;

import kaptainwutax.minemap.MineMap;
import kaptainwutax.minemap.init.Configs;
import kaptainwutax.minemap.ui.map.MapPanel;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;
import wearblackallday.swing.SwingUtils;
import wearblackallday.swing.components.LPanel;
import wearblackallday.swing.components.SelectionBox;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public abstract class Dialog extends JDialog {
	public Dialog(String title, JFrame owner) {
		super(owner);
		this.setTitle(title);
		this.setAlwaysOnTop(true);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setVisible(false);
	}

	protected MineMap mineMap() {
		return (MineMap)this.getOwner();
	}

	protected void format() {
		this.pack();
		this.setLocationRelativeTo(null);
	}

	public static class CoordHopperDialogue extends Dialog {
		public CoordHopperDialogue(JFrame owner) {
			super("Go to Coordinates", owner);
			SelectionBox<Type> typeSelectionBox = new SelectionBox<>(Type::getName, Type.values());
//			this.getRootPane().setDefaultButton(continueButton);
			this.setContentPane(new LPanel()
				.withLayout(new GridLayout(0, 2))
				.defaultSize(70, 40)
				.addTextField("X Coordinate...", "x")
				.addTextField("Z Coordinate...", "z")
				.addComponent(typeSelectionBox)
				.addButton("Continue", (customPanel, button, event) -> {
					try {
						int x, z;
						x = customPanel.getInt("x");
						z = customPanel.getInt("z");
						x = typeSelectionBox.getSelected().transform(x);
						z = typeSelectionBox.getSelected().transform(z);
						MapPanel map = this.mineMap().worldTabs.getSelectedMapPanel();
						if(map != null) map.getManager().setCenterPos(x, z);
						this.setVisible(false);
					} catch(NumberFormatException ignored) {
					}
				}));
			this.format();
		}

		private enum Type {
			BLOCK("Block Coordinates", i -> i),
			CHUNK("Chunk Coordinates", i -> i << 4),
			REGION_32("Chunk Region Coordinates (32x32)", i -> CHUNK.transform(i) << 5);

			private final String name;
			private final IntUnaryOperator transformation;

			Type(String name, IntUnaryOperator transformation) {
				this.name = name;
				this.transformation = transformation;
			}

			public String getName() {
				return this.name;
			}

			public int transform(int i) {
				return this.transformation.applyAsInt(i);
			}
		}
	}

	public static class EnterSeedDialog extends Dialog {
		private final LPanel content;

		public EnterSeedDialog(JFrame owner) {
			super("Load new Seed", owner);
			int cores = Runtime.getRuntime().availableProcessors();
			SelectionBox<Integer> threadSelection =
				new SelectionBox<>(i -> i + (i == 1 ? " thread" : " threads"), IntStream.rangeClosed(1, cores).boxed());
			SelectionBox<MCVersion> versionSelection =
				new SelectionBox<>(Arrays.stream(MCVersion.values()).filter(v -> v.isNewerOrEqualTo(MCVersion.v1_8)));
			this.setContentPane(this.content = new LPanel()
				.withLayout(new GridLayout(3, 0))
				.addTextField("Enter your seed here", "seed")
				.addComponent(threadSelection)
				.addComponent(versionSelection));
			SwingUtils.addSet(this.content, Arrays.stream(Dimension.values()).map(dimension -> {
				JCheckBoxMenuItem check = new JCheckBoxMenuItem("Load " +
					Character.toUpperCase(dimension.getName().charAt(0)) + dimension.getName().substring(1));
				check.setState(Configs.USER_PROFILE.isDimensionEnabled(dimension));
				check.addChangeListener(e -> Configs.USER_PROFILE.setDimensionState(dimension, check.getState()));
				return check;
			}).toArray(JCheckBoxMenuItem[]::new));
			JButton continueButton = new JButton("Continue");
			this.getRootPane().setDefaultButton(continueButton);
			continueButton.addActionListener(e -> {
				int usedCores = threadSelection.getSelected();
				MCVersion ver = versionSelection.getSelected();
				this.mineMap().worldTabs.load(ver,
					this.content.getText("seed").trim(),
					usedCores, Configs.USER_PROFILE.getEnabledDimensions());
				Configs.USER_PROFILE.setThreadCount(usedCores);
				Configs.USER_PROFILE.setVersion(ver);
				this.setVisible(false);
			});
			SwingUtils.addSet(this, new JLabel(), continueButton);
			this.format();
		}
	}

	public static class RenameTabDialog extends Dialog {

		public RenameTabDialog(JFrame owner) {
			super("Rename Tab", owner);
			LPanel content;
			JButton continueButton = new JButton("Continue");

			this.getRootPane().setDefaultButton(continueButton);
			this.setContentPane(content = new LPanel()
				.withLayout(new GridLayout(2, 0))
				.defaultSize(200, 40)
				.addTextField("Enter your Tab name", "head")
				.addComponent(continueButton));
			continueButton.addActionListener(e -> {
				this.mineMap().worldTabs.getSelectedHeader().setName(content.getText("head"));
				this.setVisible(false);
			});
			this.format();
		}
	}

	public static class SaltDialog extends Dialog {

		public SaltDialog(JFrame owner) {
			super("Change salts", owner);
			//TODO Wutax: "I think i will rewrite it anyway"
		}
	}
}
