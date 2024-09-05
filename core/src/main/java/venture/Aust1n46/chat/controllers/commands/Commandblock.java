package venture.Aust1n46.chat.controllers.commands;

import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import venture.Aust1n46.chat.api.interfaces.IVentureChatPlayer;
import venture.Aust1n46.chat.initiators.application.VentureChat;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.UniversalCommand;
import venture.Aust1n46.chat.model.VentureChatPlayer;
import venture.Aust1n46.chat.service.PlayerApiService;

import java.util.List;

public class Commandblock extends UniversalCommand {
	@Inject
    private VentureChat plugin;
	@Inject
	private PlayerApiService playerApiService;
	
	@Inject
	public Commandblock(String name) {
		super(name);
	}

    @Override
    public void executeCommand(CommandSender sender, String command, String[] args) {
        if (sender.hasPermission("venturechat.commandblock")) {
            if (args.length > 1) {
                IVentureChatPlayer player = playerApiService.getOnlineMineverseChatPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage(LocalizedMessage.PLAYER_OFFLINE.toString()
                            .replace("{args}", args[0]));
                    return;
                }
                boolean match = false;
                for (String cb : (List<String>) plugin.getConfig().getStringList("blockablecommands"))
                    if (args[1].equals("/" + cb))
                        match = true;
                if (match || player.getBlockedCommands().contains(args[1])) {
                    if (!player.getBlockedCommands().contains(args[1])) {
                        player.getBlockedCommands().add(args[1]);
                        player.getPlayer().sendMessage(LocalizedMessage.BLOCK_COMMAND_PLAYER.toString()
                                .replace("{command}", args[1]));
                        sender.sendMessage(LocalizedMessage.BLOCK_COMMAND_SENDER.toString()
                                .replace("{player}", player.getName())
                                .replace("{command}", args[1]));
                        return;
                    }
                    player.getBlockedCommands().remove(args[1]);
                    player.getPlayer().sendMessage(LocalizedMessage.UNBLOCK_COMMAND_PLAYER.toString()
                            .replace("{command}", args[1]));
                    sender.sendMessage(LocalizedMessage.UNBLOCK_COMMAND_SENDER.toString()
                            .replace("{player}", player.getName())
                            .replace("{command}", args[1]));
                    return;
                }
                sender.sendMessage(LocalizedMessage.COMMAND_NOT_BLOCKABLE.toString());
                return;
            }
            sender.sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString()
                    .replace("{command}", "/commandblock")
                    .replace("{args}", "[player] [command]"));
            return;
        }
        sender.sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
    }
}
