package venture.Aust1n46.chat.initiators.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import venture.Aust1n46.chat.model.IVentureChatPlayer;
import venture.Aust1n46.chat.controllers.PluginMessageController;
import venture.Aust1n46.chat.initiators.application.VentureChat;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.Alias;
import venture.Aust1n46.chat.model.ChatChannel;
import venture.Aust1n46.chat.model.GuiSlot;
import venture.Aust1n46.chat.model.VentureChatPlayer;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.FormatService;
import venture.Aust1n46.chat.service.PlayerApiService;
import venture.Aust1n46.chat.service.VentureChatDatabaseService;
import venture.Aust1n46.chat.utilities.FormatUtils;
import venture.Aust1n46.chat.xcut.VersionService;

import java.io.FileNotFoundException;

@Singleton
public class PreProcessCommandListener implements CommandExecutor, Listener {
	@Inject
	private VentureChat plugin;
	@Inject
	private FormatService formatService;
	@Inject
	private VentureChatDatabaseService databaseService;
	@Inject
	private PluginMessageController pluginMessageController;
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;
	@Inject
	private VersionService versionService;

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) throws FileNotFoundException {
		if (event.getPlayer() == null) {
			plugin.getServer().getConsoleSender()
					.sendMessage(FormatUtils.FormatStringAll("&8[&eVentureChat&8]&c - Event.getPlayer() returned null in PlayerCommandPreprocessEvent"));
			return;
		}
		ConfigurationSection cs = plugin.getConfig().getConfigurationSection("commandspy");
		Boolean wec = cs.getBoolean("worldeditcommands", true);
		IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer(event.getPlayer());
		if (!mcp.getPlayer().hasPermission("venturechat.commandspy.override")) {
			for (IVentureChatPlayer p : playerApiService.getOnlineMineverseChatPlayers()) {
				if (configService.isCommandSpy(p)) {
					if (wec) {
						p.getPlayer().sendMessage(FormatUtils.FormatStringAll(cs.getString("format").replace("{player}", mcp.getName()).replace("{command}", event.getMessage())));
					} else {
						if (!(event.getMessage().toLowerCase().startsWith("//"))) {
							p.getPlayer()
									.sendMessage(FormatUtils.FormatStringAll(cs.getString("format").replace("{player}", mcp.getName()).replace("{command}", event.getMessage())));
						} else {
							if (!(event.getMessage().toLowerCase().startsWith("//"))) {
								p.getPlayer().sendMessage(ChatColor.GOLD + mcp.getName() + ": " + event.getMessage());
							}
						}
					}
				}
			}
		}

		String[] blocked = event.getMessage().split(" ");
		if (mcp.getBlockedCommands().contains(blocked[0])) {
			mcp.getPlayer().sendMessage(LocalizedMessage.BLOCKED_COMMAND.toString().replace("{command}", event.getMessage()));
			event.setCancelled(true);
			return;
		}

		String message = event.getMessage();

		if (databaseService.isEnabled()) {
			databaseService.writeVentureChat(mcp.getUuid().toString(), mcp.getName(), "Local", "Command_Component", event.getMessage().replace("'", "''"), "Command");
		}

		for (Alias a : configService.getAliases()) {
			if (message.toLowerCase().substring(1).split(" ")[0].equals(a.getName().toLowerCase())) {
				for (String s : a.getComponents()) {
					if (!mcp.getPlayer().hasPermission(a.getPermission()) && a.hasPermission()) {
						mcp.getPlayer().sendMessage(ChatColor.RED + "You do not have permission for this alias.");
						event.setCancelled(true);
						return;
					}
					int num = 1;
					if (message.length() < a.getName().length() + 2 || a.getArguments() == 0)
						num = 0;
					int arg = 0;
					if (message.substring(a.getName().length() + 1 + num).length() == 0)
						arg = 1;
					String[] args = message.substring(a.getName().length() + 1 + num).split(" ");
					String send = "";
					if (args.length - arg < a.getArguments()) {
						String keyword = "arguments.";
						if (a.getArguments() == 1)
							keyword = "argument.";
						mcp.getPlayer().sendMessage(ChatColor.RED + "Invalid arguments for this alias, enter at least " + a.getArguments() + " " + keyword);
						event.setCancelled(true);
						return;
					}
					for (int b = 0; b < args.length; b++) {
						send += " " + args[b];
					}
					if (send.length() > 0)
						send = send.substring(1);
					s = FormatUtils.FormatStringAll(s);
					if (mcp.getPlayer().hasPermission("venturechat.color.legacy")) {
						send = FormatUtils.FormatStringLegacyColor(send);
					}
					if (mcp.getPlayer().hasPermission("venturechat.color")) {
						send = FormatUtils.FormatStringColor(send);
					}
					if (mcp.getPlayer().hasPermission("venturechat.format")) {
						send = FormatUtils.FormatString(send);
					}
					if (s.startsWith("Command:")) {
						mcp.getPlayer().chat(s.substring(9).replace("$", send));
						event.setCancelled(true);
					}
					if (s.startsWith("Message:")) {
						mcp.getPlayer().sendMessage(s.substring(9).replace("$", send));
						event.setCancelled(true);
					}
					if (s.startsWith("Broadcast:")) {
						formatService.broadcastToServer(s.substring(11).replace("$", send));
						event.setCancelled(true);
					}
				}
			}
		}

		if (!configService.areAliasesRegisteredAsCommands()) {
			for (ChatChannel channel : configService.getChatChannels()) {
				if (!channel.isPermissionRequired() || mcp.getPlayer().hasPermission(channel.getPermission())) {
					if (message.equals("/" + channel.getAlias())) {
						mcp.getPlayer().sendMessage(
								LocalizedMessage.SET_CHANNEL.toString().replace("{channel_color}", channel.getColor() + "").replace("{channel_name}", channel.getName()));
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
						event.setCancelled(true);
						return;
					}
					if (message.toLowerCase().startsWith("/" + channel.getAlias() + " ")) {
						message = message.substring(channel.getAlias().length() + 1);
						mcp.getListening().add(channel.getName());
						if (channel.isBungeeEnabled()) {
							pluginMessageController.synchronize(mcp, true);
						}
						mcp.setQuickChannel(channel);
						mcp.setQuickChat(true);
						mcp.getPlayer().chat(message);
						event.setCancelled(true);
					}
				}
			}
		}
	}

	// old 1.8 command map
	@EventHandler
	public void onServerCommand(ServerCommandEvent event) {
		if (databaseService.isEnabled()) {
			databaseService.writeVentureChat("N/A", "Console", "Local", "Command_Component", event.getCommand().replace("'", "''"), "Command");
		}
	}

	/**
	 * Unused
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "This command must be run by a player.");
			return true;
		}
		IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer((Player) sender);
		for (ChatChannel channel : configService.getChatChannels()) {
			if (command.getName().toLowerCase().equals(channel.getAlias())) {
				if (args.length == 0) {
					mcp.getPlayer().sendMessage(ChatColor.RED + "Invalid command: /" + channel.getAlias() + " message");
					return true;
				}
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
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOW)
	public void InventoryClick(InventoryClickEvent e) {
		if (!e.getView().getTitle().contains("VentureChat")) {
			return;
		}
		e.setCancelled(true);
		ItemStack item = e.getCurrentItem();
		if (item == null) {
			return;
		}
		IVentureChatPlayer mcp = playerApiService.getOnlineMineverseChatPlayer((Player) e.getWhoClicked());
		String playerName = e.getView().getTitle().replace(" GUI", "").replace("VentureChat: ", "");
		VentureChatPlayer target = playerApiService.getMineverseChatPlayer(playerName);
		ItemStack skull = e.getInventory().getItem(0);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		ChatChannel channel = configService.getChannel(ChatColor.stripColor(skullMeta.getLore().get(0)).replace("Channel: ", ""));
		int hash = Integer.parseInt(ChatColor.stripColor(skullMeta.getLore().get(1).replace("Hash: ", "")));
		if (versionService.is1_7()) {
			if (item.getType() == Material.BEDROCK) {
				mcp.getPlayer().closeInventory();
			}
		} else {
			if (item.getType() == Material.BARRIER) {
				mcp.getPlayer().closeInventory();
			}
		}
		for (GuiSlot g : configService.getGuiSlots()) {
			if (g.getIcon() == item.getType() && g.getDurability() == item.getDurability() && g.getSlot() == e.getSlot()) {
				String command = g.getCommand().replace("{channel}", channel.getName()).replace("{hash}", hash + "");
				if (target != null) {
					command = command.replace("{player_name}", target.getName());
					if (target.isOnline()) {
						command = FormatUtils.FormatStringAll(PlaceholderAPI.setBracketPlaceholders(target.getPlayer(), command));
					}
				} else {
					command = command.replace("{player_name}", "Discord_Message");
				}
				mcp.getPlayer().chat(command);
			}
		}
	}
}
