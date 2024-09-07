package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.IVentureChatPlayer;
import venture.Aust1n46.chat.model.UniversalCommand;
import venture.Aust1n46.chat.service.PlayerApiService;

public class Forceall extends UniversalCommand {
	@Inject
	private PlayerApiService playerApiService;

	@Inject
	public Forceall(String name) {
		super(name);
	}

	@Override
	public void executeCommand(CommandSender sender, String command, String[] args) {
		if (sender.hasPermission("venturechat.forceall")) {
			if (args.length < 1) {
				sender.sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString().replace("{command}", "/forceall").replace("{args}", "[message]"));
				return;
			}
			String forcemsg = "";
			for (int x = 0; x < args.length; x++) {
				if (args[x].length() > 0) {
					forcemsg += args[x] + " ";
				}
			}
			sender.sendMessage(LocalizedMessage.FORCE_ALL.toString().replace("{message}", forcemsg));
			for (IVentureChatPlayer player : playerApiService.getOnlineMineverseChatPlayers()) {
				player.getPlayer().chat(forcemsg);
			}
			return;
		}
		sender.sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
	}
}
