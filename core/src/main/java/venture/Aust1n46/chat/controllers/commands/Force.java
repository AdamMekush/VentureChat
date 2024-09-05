package venture.Aust1n46.chat.controllers.commands;

import org.bukkit.command.CommandSender;

import com.google.inject.Inject;

import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.UniversalCommand;
import venture.Aust1n46.chat.model.VentureChatPlayer;
import venture.Aust1n46.chat.service.PlayerApiService;

public class Force extends UniversalCommand {
	@Inject
	private PlayerApiService playerApiService;

	@Inject
	public Force(String name) {
		super(name);
	}

	@Override
	public void executeCommand(CommandSender sender, String command, String[] args) {
		if (sender.hasPermission("venturechat.force")) {
			if (args.length < 2) {
				sender.sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString().replace("{command}", "/force").replace("{args}", "[player] [message]"));
				return;
			}
			VentureChatPlayer player = playerApiService.getOnlineMineverseChatPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(LocalizedMessage.PLAYER_OFFLINE.toString().replace("{args}", args[0]));
				return;
			}
			String forcemsg = "";
			for (int x = 1; x < args.length; x++)
				if (args[x].length() > 0)
					forcemsg += args[x] + " ";
			sender.sendMessage(LocalizedMessage.FORCE_PLAYER.toString().replace("{player}", player.getName()).replace("{message}", forcemsg));
			player.getPlayer().chat(forcemsg);
			return;
		}
		sender.sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
	}
}
