package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import venture.Aust1n46.chat.api.events.ChannelLeaveEvent;
import venture.Aust1n46.chat.model.IVentureChatPlayer;
import venture.Aust1n46.chat.controllers.PluginMessageController;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.ChatChannel;
import venture.Aust1n46.chat.model.PlayerCommand;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.PlayerApiService;

public class Leave extends PlayerCommand {
	@Inject
	private PluginMessageController pluginMessageController;
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;

	@Inject
	public Leave(String name) {
		super(name);
	}

	@Override
	public void executeCommand(Player sender, String command, String[] args) {
		IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer((Player) sender);
		if (args.length > 0) {
			ChatChannel channel = configService.getChannel(args[0]);
			if (channel == null) {
				mcp.getPlayer().sendMessage(LocalizedMessage.INVALID_CHANNEL.toString().replace("{args}", args[0]));
				return;
			}

			ChannelLeaveEvent channelLeaveEvent = new ChannelLeaveEvent(mcp, channel);
			channelLeaveEvent.callEvent();
			if(channelLeaveEvent.isCancelled()){
				return;
			}

			mcp.getListening().remove(channel.getName());
			mcp.getPlayer().sendMessage(LocalizedMessage.LEAVE_CHANNEL.toString().replace("{channel_color}", channel.getColor() + "").replace("{channel_name}", channel.getName()));
			boolean isThereABungeeChannel = channel.isBungeeEnabled();
			if (mcp.getListening().size() == 0) {
				mcp.getListening().add(configService.getDefaultChannel().getName());
				mcp.setCurrentChannel(configService.getDefaultChannel());
				if (configService.getDefaultChannel().isBungeeEnabled()) {
					isThereABungeeChannel = true;
				}
				mcp.getPlayer().sendMessage(LocalizedMessage.MUST_LISTEN_ONE_CHANNEL.toString());
				mcp.getPlayer()
						.sendMessage(LocalizedMessage.SET_CHANNEL.toString().replace("{channel_color}", ChatColor.valueOf(configService.getDefaultColor().toUpperCase()) + "")
								.replace("{channel_name}", configService.getDefaultChannel().getName()));
			}
			if (isThereABungeeChannel) {
				pluginMessageController.synchronize(mcp, true);
			}
			return;
		}
		mcp.getPlayer().sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString().replace("{command}", "/leave").replace("{args}", "[channel]"));
	}
}
