package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.entity.Player;
import venture.Aust1n46.chat.api.events.ChannelJoinEvent;
import venture.Aust1n46.chat.api.interfaces.IChatChannel;
import venture.Aust1n46.chat.controllers.PluginMessageController;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.ChatChannel;
import venture.Aust1n46.chat.model.PlayerCommand;
import venture.Aust1n46.chat.model.VentureChatPlayer;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.PlayerApiService;

@Singleton
public class Channel extends PlayerCommand {
	@Inject
	private PluginMessageController pluginMessageController;
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;

	@Inject
	public Channel(String name) {
		super(name);
	}

	@Override
	protected void executeCommand(final Player player, final String commandLabel, final String[] args) {
		final VentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer(player);
		if (args.length > 0) {
			if (!configService.isChannel(args[0])) {
				mcp.getPlayer().sendMessage(LocalizedMessage.INVALID_CHANNEL.toString().replace("{args}", args[0]));
				return;
			}
			ChatChannel channel = configService.getChannel(args[0]);
			ChannelJoinEvent channelJoinEvent = new ChannelJoinEvent(mcp.getPlayer(), channel,
					LocalizedMessage.SET_CHANNEL.toString().replace("{channel_color}", channel.getColor() + "").replace("{channel_name}", channel.getName()));
			plugin.getServer().getPluginManager().callEvent(channelJoinEvent);
			handleChannelJoinEvent(channelJoinEvent);
			return;
		}
		mcp.getPlayer().sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString().replace("{command}", "/channel").replace("{args}", "[channel]"));
	}

	private void handleChannelJoinEvent(final ChannelJoinEvent event) {
		if (event.isCancelled())
			return;
		IChatChannel channel = event.getChannel();
		VentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer(event.getPlayer());
		if (channel.isPermissionRequired()) {
			if (!mcp.getPlayer().hasPermission(channel.getPermission())) {
				mcp.getListening().remove(channel.getName());
				mcp.getPlayer().sendMessage(LocalizedMessage.CHANNEL_NO_PERMISSION.toString());
				return;
			}
		}
		if (mcp.getConversation() != null) {
			for (VentureChatPlayer p : playerApiService.getOnlineMineverseChatPlayers()) {
				if (configService.isSpy(p)) {
					p.getPlayer().sendMessage(LocalizedMessage.EXIT_PRIVATE_CONVERSATION_SPY.toString().replace("{player_sender}", mcp.getName()).replace("{player_receiver}",
							playerApiService.getMineverseChatPlayer(mcp.getConversation()).getName()));
				}
			}
			mcp.getPlayer().sendMessage(
					LocalizedMessage.EXIT_PRIVATE_CONVERSATION.toString().replace("{player_receiver}", playerApiService.getMineverseChatPlayer(mcp.getConversation()).getName()));
			mcp.setConversation(null);
		}
		mcp.getListening().add(channel.getName());
		mcp.setCurrentChannel(channel);
		mcp.getPlayer().sendMessage(event.getMessage());
		if (channel.isBungeeEnabled()) {
			pluginMessageController.synchronize(mcp, true);
		}
	}
}
