package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import venture.Aust1n46.chat.api.events.UnmutePlayerEvent;
import venture.Aust1n46.chat.controllers.PluginMessageController;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.ChatChannel;
import venture.Aust1n46.chat.model.IChatChannel;
import venture.Aust1n46.chat.model.UniversalCommand;
import venture.Aust1n46.chat.model.VentureChatPlayer;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.PlayerApiService;

import java.util.Set;
import java.util.stream.Collectors;

public class Unmuteall extends UniversalCommand {
	@Inject
	private PluginMessageController pluginMessageController;
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;

	@Inject
	public Unmuteall(String name) {
		super(name);
	}

	@Override
	public void executeCommand(CommandSender sender, String command, String[] args) {
		UnmutePlayerEvent unmutePlayerEvent;
		if (sender.hasPermission("venturechat.mute")) {
			if (args.length < 1) {
				sender.sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString().replace("{command}", "/unmuteall").replace("{args}", "[player]"));
				return;
			}
			VentureChatPlayer player = playerApiService.getMineverseChatPlayer(args[0]);
			if (player == null || (!player.isOnline() && !sender.hasPermission("venturechat.mute.offline"))) {
				sender.sendMessage(LocalizedMessage.PLAYER_OFFLINE.toString().replace("{args}", args[0]));
				return;
			}
			boolean bungee = false;

			Set<IChatChannel> unmutedChannels = player.getMutes()
					.keySet()
					.stream()
					.map(configService::getChannel)
					.collect(Collectors.toSet());
			if(sender instanceof Player) {
				unmutePlayerEvent = new UnmutePlayerEvent(player, playerApiService.getMineverseChatPlayer(sender.getName()), unmutedChannels);
			} else {
				unmutePlayerEvent = new UnmutePlayerEvent(player, null, unmutedChannels);
			}
			unmutePlayerEvent.callEvent();
			if(unmutePlayerEvent.isCancelled()){
				return;
			}

			for (ChatChannel channel : configService.getChatChannels()) {
				player.getMutes().remove(channel.getName());
				if (channel.isBungeeEnabled()) {
					bungee = true;
				}
			}
			if (bungee) {
				pluginMessageController.synchronize(player, true);
			}
			sender.sendMessage(LocalizedMessage.UNMUTE_PLAYER_ALL_SENDER.toString().replace("{player}", player.getName()));
			if (player.isOnline()) {
				player.getPlayer().sendMessage(LocalizedMessage.UNMUTE_PLAYER_ALL_PLAYER.toString());
			} else
				player.setModified(true);
			return;
		} else {
			sender.sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
			return;
		}
	}
}
