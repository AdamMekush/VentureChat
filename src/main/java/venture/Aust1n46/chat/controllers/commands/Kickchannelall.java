package venture.Aust1n46.chat.controllers.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.google.inject.Inject;

import org.bukkit.entity.Player;
import venture.Aust1n46.chat.api.events.KickchannelPlayerEvent;
import venture.Aust1n46.chat.controllers.PluginMessageController;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.ChatChannel;
import venture.Aust1n46.chat.model.UniversalCommand;
import venture.Aust1n46.chat.model.VentureChatPlayer;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.PlayerApiService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Kickchannelall extends UniversalCommand {
	@Inject
	private PluginMessageController pluginMessageController;
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;

	@Inject
	public Kickchannelall(String name) {
		super(name);
	}

	@Override
	public void executeCommand(CommandSender sender, String command, String[] args) {
		if (sender.hasPermission("venturechat.kickchannelall")) {
			if (args.length < 1) {
				sender.sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString().replace("{command}", "/kickchannelall").replace("{args}", "[player]"));
				return;
			}
			VentureChatPlayer player = playerApiService.getMineverseChatPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(LocalizedMessage.PLAYER_OFFLINE.toString().replace("{args}", args[0]));
				return;
			}
			boolean isThereABungeeChannel = false;
			for (String channel : player.getListening()) {
				if (configService.isChannel(channel)) {
					ChatChannel chatChannelObj = configService.getChannel(channel);
					if (chatChannelObj.isBungeeEnabled()) {
						isThereABungeeChannel = true;
					}
				}
			}
			Collection<ChatChannel> kickedChannels = player.getListening()
					.stream()
					.map(configService::getChannel)
					.collect(Collectors.toList());
			player.getListening().clear();
			sender.sendMessage(LocalizedMessage.KICK_CHANNEL_ALL_SENDER.toString().replace("{player}", player.getName()));
			player.getListening().add(configService.getDefaultChannel().getName());
			player.setCurrentChannel(configService.getDefaultChannel());
			if (configService.getDefaultChannel().isBungeeEnabled()) {
				isThereABungeeChannel = true;
			}
			if (isThereABungeeChannel) {
				pluginMessageController.synchronize(player, true);
			}
			if (player.isOnline()) {
				player.getPlayer().sendMessage(LocalizedMessage.KICK_CHANNEL_ALL_PLAYER.toString());
				player.getPlayer().sendMessage(LocalizedMessage.MUST_LISTEN_ONE_CHANNEL.toString());
				player.getPlayer()
						.sendMessage(LocalizedMessage.SET_CHANNEL.toString().replace("{channel_color}", ChatColor.valueOf(configService.getDefaultColor().toUpperCase()) + "")
								.replace("{channel_name}", configService.getDefaultChannel().getName()));
			} else {
				player.setModified(true);
			}

			if(sender instanceof Player) {
				new KickchannelPlayerEvent(player.getPlayer(), plugin.getServer().getPlayer(sender.getName()), kickedChannels).callEvent();
			} else {
				new KickchannelPlayerEvent(player.getPlayer(), null, kickedChannels).callEvent();
			}

			return;
		}
		sender.sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
	}
}
