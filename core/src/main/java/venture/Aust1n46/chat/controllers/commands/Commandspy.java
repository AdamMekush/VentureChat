package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import venture.Aust1n46.chat.model.IVentureChatPlayer;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.PlayerCommand;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.PlayerApiService;

public class Commandspy extends PlayerCommand {
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;

	@Inject
	public Commandspy(String name) {
		super(name);
	}

	@Override
	public void executeCommand(Player sender, String command, String[] args) {
		IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer((Player) sender);
		if (mcp.getPlayer().hasPermission("venturechat.commandspy")) {
			if (!configService.isCommandSpy(mcp)) {
				mcp.setCommandSpy(true);
				mcp.getPlayer().sendMessage(LocalizedMessage.COMMANDSPY_ON.toString());
				return;
			}
			mcp.setCommandSpy(false);
			mcp.getPlayer().sendMessage(LocalizedMessage.COMMANDSPY_OFF.toString());
			return;
		}
		mcp.getPlayer().sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
	}
}
