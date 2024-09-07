package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.IVentureChatPlayer;
import venture.Aust1n46.chat.model.PlayerCommand;
import venture.Aust1n46.chat.service.PlayerApiService;

public class Notifications extends PlayerCommand {
	@Inject
	private PlayerApiService playerApiService;

	@Inject
	public Notifications(String name) {
		super(name);
	}

	@Override
	public void executeCommand(Player player, String command, String[] args) {
		IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer(player);
		if (!mcp.isNotifications()) {
			mcp.setNotifications(true);
			mcp.getPlayer().sendMessage(LocalizedMessage.NOTIFICATIONS_ON.toString());
			return;
		}
		mcp.setNotifications(false);
		mcp.getPlayer().sendMessage(LocalizedMessage.NOTIFICATIONS_OFF.toString());
		return;
	}
}
