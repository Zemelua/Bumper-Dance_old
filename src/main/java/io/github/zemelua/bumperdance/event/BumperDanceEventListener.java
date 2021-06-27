package io.github.zemelua.bumperdance.event;

import io.github.zemelua.bumperdance.BumperDance;
import io.github.zemelua.bumperdance.config.BumperDanceConfigManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class BumperDanceEventListener implements Listener {
	private final BumperDance bumperDance;

	public BumperDanceEventListener(BumperDance bumperDance) {
		this.bumperDance = bumperDance;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (this.bumperDance.getConfigManager().isHarvestEXPEnable()) {
			event.setExpToDrop(
					this.dropExp(event.getBlock(), event.getExpToDrop())
			);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (this.bumperDance.getConfigManager().isEasyReapEnable()) {
			this.replaceCrops(event.getPlayer(), event.getClickedBlock(), event.getAction());
		}
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		BumperDanceConfigManager configManager = this.bumperDance.getConfigManager();

		if (configManager.isSneakGrowEnable()) {
			if (event.isSneaking()) {
				this.growAroundPlants(event.getPlayer(), configManager.getSneakGrowRangeH(), configManager.getSneakGrowRangeV());
			}
		}
	}

	private int dropExp(Block block, int originalAmount) {
		BlockData blockData = block.getBlockData().clone();
		if ((blockData instanceof Ageable)) {
			Ageable ageableData = (Ageable) blockData;
			if (ageableData.getAge() == ageableData.getMaximumAge()) {
				return ((int) Math.floor((double) this.bumperDance.getRandom().nextInt(100) / 20))
						* this.bumperDance.getConfigManager().getHarvestEXPCoefficient();
			}
		}

		return originalAmount;
	}

	private void replaceCrops(Player player, Block block, Action action) {
		ItemStack heldItemStack = player.getInventory().getItemInMainHand();
		Material heldItemStackType = heldItemStack.getType();

		if (((heldItemStackType == Material.WOODEN_HOE || heldItemStackType == Material.STONE_HOE
				|| heldItemStackType == Material.IRON_HOE || heldItemStackType == Material.GOLDEN_HOE
				|| heldItemStackType == Material.DIAMOND_HOE || heldItemStackType == Material.NETHERITE_HOE)
				|| this.bumperDance.getConfigManager().isUnnecessaryHoe())
				&& action == Action.RIGHT_CLICK_BLOCK
		) {
			BlockData blockData = block.getBlockData().clone();
			if (blockData instanceof Ageable) {
				Ageable ageableData = (Ageable) blockData;
				int maximumAge = ageableData.getMaximumAge();
				if (maximumAge > 0 && ageableData.getAge() == maximumAge) {
					Location location = block.getLocation();
					for (ItemStack itemStack : block.getDrops(heldItemStack, player)) {
						block.getWorld().dropItem(location, itemStack);
					}
					ageableData.setAge(0);
					block.getWorld().getBlockAt(location).setBlockData(ageableData);
				}
			}
		}
	}

	private void growAroundPlants(Player player, int hRange, int vRange) {
		Location center = player.getLocation();
		for (int x = -hRange; x <= hRange; x++) {
			for (int y = -vRange; y <= vRange; y++) {
				for (int z = -hRange; z <= hRange; z++) {
					Block block = player.getWorld().getBlockAt(x + center.getBlockX(), y + center.getBlockY(), z + center.getBlockZ());
					int probability = (Math.abs(x) + Math.abs(z)) / 2;
					if (!this.bumperDance.getConfigManager().isSneakGrowDistanceDecay()) {
						probability = 0;
					}

					if (this.bumperDance.getRandom().nextInt(hRange) >= probability) {
						if (block.getBlockData() instanceof Ageable || block.getBlockData() instanceof Sapling) {
							block.applyBoneMeal(BlockFace.EAST);
						}
					}
				}
			}
		}
	}
}
