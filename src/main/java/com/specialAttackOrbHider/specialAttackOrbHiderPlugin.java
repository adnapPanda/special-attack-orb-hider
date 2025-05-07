package com.specialAttackOrbHider;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.Objects;

@Slf4j
@PluginDescriptor(
	name = "Special Attack Orb Hider"
)
public class specialAttackOrbHiderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private specialAttackOrbHiderConfig config;

	private static final int SPEC_ORB_ID = 10485793;

	@Override
	protected void startUp() throws Exception
	{

	}

	@Override
	protected void shutDown() throws Exception
	{

	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged itemContainerChanged)
	{
		// Hides the spec orb for all weapons if option is set
		Widget SpecOrb = client.getWidget(SPEC_ORB_ID);
		if (config.hideSpecOrbAll() && SpecOrb != null) SpecOrb.setHidden(true);
		// Checks whether equipped weapon is specified in the list provided by player and disables spec orb if so
		else {
			final ItemContainer itemContainer = client.getItemContainer(InventoryID.WORN);
			if (itemContainer != null) {
				final Item weapon = itemContainer.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
				if (weapon != null && SpecOrb != null) {
					String weaponName = itemManager.getItemComposition(weapon.getId()).getName().toLowerCase();
					SpecOrb.setHidden(config.hideSpecifiedSpecOrb().toLowerCase().contains(weaponName));
				}
			}
		}
	}

	@Provides
	specialAttackOrbHiderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(specialAttackOrbHiderConfig.class);
	}
}
