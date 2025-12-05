package com.salvagesack;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages fetching and caching of item icons from the OSRS wiki
 */
@Slf4j
public class ItemIconManager
{
	private static final String WIKI_IMAGE_BASE_URL = "https://oldschool.runescape.wiki/images/";
	private static final int ICON_SIZE = 32;
	
	private final Map<String, BufferedImage> iconCache;
	private final BufferedImage placeholderIcon;

	public ItemIconManager()
	{
		this.iconCache = new ConcurrentHashMap<>();
		this.placeholderIcon = createPlaceholderIcon();
	}

	/**
	 * Get icon for an item by name
	 * @param itemName Name of the item
	 * @return BufferedImage of the item icon, or placeholder if not found
	 */
	public BufferedImage getItemIcon(String itemName)
	{
		// Check cache first
		BufferedImage cached = iconCache.get(itemName);
		if (cached != null)
		{
			return cached;
		}

		// Try to fetch from wiki
		BufferedImage icon = fetchIconFromWiki(itemName);
		if (icon != null)
		{
			iconCache.put(itemName, icon);
			return icon;
		}

		// Return placeholder if fetch failed
		return placeholderIcon;
	}

	/**
	 * Fetch icon from OSRS wiki
	 */
	private BufferedImage fetchIconFromWiki(String itemName)
	{
		try
		{
			// Convert item name to wiki image format
			// Example: "Plank" -> "Plank_detail.png"
			String imageName = itemName.replace(" ", "_") + "_detail.png";
			String encodedName = URLEncoder.encode(imageName, StandardCharsets.UTF_8.toString())
				.replace("+", "%20");
			
			String imageUrl = WIKI_IMAGE_BASE_URL + encodedName;
			
			log.debug("Fetching icon from: {}", imageUrl);
			
			URL url = new URL(imageUrl);
			try (InputStream in = url.openStream())
			{
				BufferedImage image = ImageIO.read(in);
				if (image != null)
				{
					// Resize to standard icon size
					return ImageUtil.resizeImage(image, ICON_SIZE, ICON_SIZE);
				}
			}
		}
		catch (IOException e)
		{
			log.debug("Failed to fetch icon for {}: {}", itemName, e.getMessage());
		}

		return null;
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

	/**
	 * Clear the icon cache
	 */
	public void clearCache()
	{
		iconCache.clear();
	}
}
