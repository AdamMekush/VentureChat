package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import venture.Aust1n46.chat.api.interfaces.IVentureChatPlayer;
import venture.Aust1n46.chat.controllers.PluginMessageController;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.PlayerCommand;
import venture.Aust1n46.chat.model.VentureChatPlayer;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.PlayerApiService;

public class Spy extends PlayerCommand {
	@Inject
	private PluginMessageController pluginMessageController;
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;

	@Inject
	public Spy(String name) {
		super(name);
	}

	@Override
	public void executeCommand(Player player, String command, String[] args) {
		IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer(player);
		if (mcp.getPlayer().hasPermission("venturechat.spy")) {
			if (!configService.isSpy(mcp)) {
				mcp.setSpy(true);
				mcp.getPlayer().sendMessage(LocalizedMessage.SPY_ON.toString());
				pluginMessageController.synchronize(mcp, true);
				return;
			}
			mcp.setSpy(false);
			mcp.getPlayer().sendMessage(LocalizedMessage.SPY_OFF.toString());
			pluginMessageController.synchronize(mcp, true);
			return;
		}
		mcp.getPlayer().sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
		return;
	}
}
