package venture.Aust1n46.chat.placeholderapi;

import com.google.inject.Inject;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import venture.Aust1n46.chat.api.interfaces.IChatChannel;
import venture.Aust1n46.chat.api.interfaces.IVentureChatPlayer;
import venture.Aust1n46.chat.initiators.application.VentureChat;
import venture.Aust1n46.chat.service.PlayerApiService;

public class VentureChatPlaceholders extends PlaceholderExpansion {
	private static final String AUTHOR = "Aust1n46";
	private static final String IDENTIFIER = "venturechat";

	@Inject
	private VentureChat plugin;
	@Inject
	private PlayerApiService playerApiService;

	private String version;

	@Inject
	public void postConstruct() {
		version = plugin.getDescription().getVersion();
	}

	@Override
	public String onPlaceholderRequest(final Player p, final String identifier) {
		if (p == null) {
			return null;
		}
		final IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer(p);
		if (mcp == null) {
			return StringUtils.EMPTY;
		}
		if (identifier.startsWith("channel_")) {
			IChatChannel currentChannel = mcp.isQuickChat() ? mcp.getQuickChannel() : mcp.getCurrentChannel();
			if (currentChannel == null) {
				return StringUtils.EMPTY;
			}
			switch (identifier) {
			case "channel_name":
				return currentChannel.getName();
			case "channel_alias":
				return currentChannel.getAlias();
			case "channel_color":
				return (String) currentChannel.getColor();
			case "channel_chatcolor":
				return currentChannel.getChatColor();
			case "channel_is_bungee":
				return currentChannel.isBungeeEnabled() ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
			case "channel_cooldown":
				return currentChannel.getCooldown() + "";
			case "channel_distance":
				return currentChannel.getDistance() + "";
			case "channel_prefix":
				return currentChannel.getPrefix();
			}
		}
		return null;
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getAuthor() {
		return AUTHOR;
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public String getVersion() {
		return version;
	}
}
