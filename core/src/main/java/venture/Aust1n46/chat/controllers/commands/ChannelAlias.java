package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import venture.Aust1n46.chat.api.events.ChannelLeaveEvent;
import venture.Aust1n46.chat.model.IVentureChatPlayer;
import venture.Aust1n46.chat.controllers.PluginMessageController;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.ChatChannel;
import venture.Aust1n46.chat.model.PlayerCommand;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.PlayerApiService;

public class ChannelAlias extends PlayerCommand {
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;
	@Inject
	private PluginMessageController pluginMessageController;

	public ChannelAlias() {
		super("channelalias");
	}

	@Override
	protected void executeCommand(final Player player, final String commandLabel, final String[] args) {
		IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer(player);
		for (ChatChannel channel : configService.getChatChannels()) {
			if (commandLabel.toLowerCase().equals(channel.getAlias())) {
				if (args.length == 0) {
					ChannelLeaveEvent channelLeaveEvent = new ChannelLeaveEvent(mcp.getPlayer(), mcp.getCurrentChannel());
					channelLeaveEvent.callEvent();
					if(channelLeaveEvent.isCancelled()){
						return;
					}

					mcp.getPlayer()
							.sendMessage(LocalizedMessage.SET_CHANNEL.toString().replace("{channel_color}", channel.getColor() + "").replace("{channel_name}", channel.getName()));
					if (mcp.getConversation() != null) {
						for (IVentureChatPlayer p : playerApiService.getOnlineMineverseChatPlayers()) {
							if (configService.isSpy(p)) {
								p.getPlayer().sendMessage(LocalizedMessage.EXIT_PRIVATE_CONVERSATION_SPY.toString().replace("{player_sender}", mcp.getName())
										.replace("{player_receiver}", playerApiService.getMineverseChatPlayer(mcp.getConversation()).getName()));
							}
						}
						mcp.getPlayer().sendMessage(LocalizedMessage.EXIT_PRIVATE_CONVERSATION.toString().replace("{player_receiver}",
								playerApiService.getMineverseChatPlayer(mcp.getConversation()).getName()));
						mcp.setConversation(null);
					}
					mcp.getListening().add(channel.getName());
					mcp.setCurrentChannel(channel);
					if (channel.isBungeeEnabled()) {
						pluginMessageController.synchronize(mcp, true);
					}
					return;
				} else {
					mcp.setQuickChat(true);
					mcp.setQuickChannel(channel);
					mcp.getListening().add(channel.getName());
					if (channel.isBungeeEnabled()) {
						pluginMessageController.synchronize(mcp, true);
					}
					String msg = "";
					for (int x = 0; x < args.length; x++) {
						if (args[x].length() > 0)
							msg += " " + args[x];
					}
					mcp.getPlayer().chat(msg);
					return;
				}
			}
		}
	}
}
