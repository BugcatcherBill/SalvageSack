package com.salvagesack;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.AsyncBufferedImage;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages fetching and caching of item icons using RuneLite's ItemManager
 */
@Slf4j
public class ItemIconManager
{
	private static final int ICON_SIZE = 32;
	
	private final Map<Integer, BufferedImage> iconCache;
	private final BufferedImage placeholderIcon;

	@Setter
	private Runnable onIconLoaded;

	@Setter
	private ItemManager itemManager;

	public ItemIconManager()
	{
		this.iconCache = new ConcurrentHashMap<>();
		this.placeholderIcon = createPlaceholderIcon();
	}

	/**
	 * Get icon for an item by ID
	 * @param itemId ID of the item
	 * @return BufferedImage of the item icon, or placeholder if not found
	 */
	public BufferedImage getItemIcon(int itemId)
	{
		if (itemId <= 0)
		{
			return placeholderIcon;
		}

		// Check cache first
		BufferedImage cached = iconCache.get(itemId);
		if (cached != null)
		{
			return cached;
		}

		// Try to get from ItemManager
		if (itemManager != null)
		{
			try
			{
				AsyncBufferedImage asyncImage = itemManager.getImage(itemId);
				if (asyncImage != null)
				{
					// Cache when the image loads and trigger callback
					asyncImage.onLoaded(() -> {
						iconCache.put(itemId, asyncImage);
						log.debug("Cached icon for item {}", itemId);
						if (onIconLoaded != null)
						{
							onIconLoaded.run();
						}
					});
					// Return the async image (it extends BufferedImage)
					return asyncImage;
				}
			}
			catch (Exception e)
			{
				log.debug("Failed to get icon for item {}: {}", itemId, e.getMessage());
			}
		}

		// Return placeholder if fetch failed
		return placeholderIcon;
	}

	/**
	 * Create a simple placeholder icon
	 */
	private BufferedImage createPlaceholderIcon()
	{
		BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);
		// Create a simple gray square
		for (int x = 0; x < ICON_SIZE; x++)
		{
			for (int y = 0; y < ICON_SIZE; y++)
			{
				if (x < 2 || x >= ICON_SIZE - 2 || y < 2 || y >= ICON_SIZE - 2)
				{
					image.setRGB(x, y, 0xFF666666); // Border
				}
				else
				{
					image.setRGB(x, y, 0xFF333333); // Fill
				}
			}
		}
		return image;
	}
}
