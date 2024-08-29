package venture.Aust1n46.chat.controllers.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.ints.IntLists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.inject.Inject;

import venture.Aust1n46.chat.api.events.MutePlayerEvent;
import venture.Aust1n46.chat.controllers.PluginMessageController;
import venture.Aust1n46.chat.localization.LocalizedMessage;
import venture.Aust1n46.chat.model.ChatChannel;
import venture.Aust1n46.chat.model.MuteContainer;
import venture.Aust1n46.chat.model.UniversalCommand;
import venture.Aust1n46.chat.model.VentureChatPlayer;
import venture.Aust1n46.chat.service.ConfigService;
import venture.Aust1n46.chat.service.PlayerApiService;
import venture.Aust1n46.chat.utilities.FormatUtils;

public class Mute extends UniversalCommand {
	private static final List<String> COMMON_MUTE_TIMES = Collections.unmodifiableList(Arrays.asList(new String[] { "12h", "15m", "1d", "1h", "1m", "30s" }));

	@Inject
	private PluginMessageController pluginMessageController;
	@Inject
	private PlayerApiService playerApiService;
	@Inject
	private ConfigService configService;

	@Inject
	public Mute(String name) {
		super(name);
	}

	@Override
	public void executeCommand(CommandSender sender, String command, String[] args) {
		if (sender.hasPermission("venturechat.mute")) {
			if (args.length < 2) {
				sender.sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString().replace("{command}", "/mute").replace("{args}", "[channel] [player] {time} {reason}"));
				return;
			}
			if (configService.isChannel(args[0])) {
				ChatChannel channel = configService.getChannel(args[0]);
				if (channel.isMutable()) {
					long datetime = System.currentTimeMillis();
					long time = 0;
					int reasonStartPos = 2;
					String reason = "";
					if (args.length > 2) {
						String timeString = args[2];
						if (Character.isDigit(timeString.charAt(0))) {
							reasonStartPos = 3;
							time = FormatUtils.parseTimeStringToMillis(timeString);
							if (time <= 0) {
								sender.sendMessage(LocalizedMessage.INVALID_TIME.toString().replace("{args}", timeString));
								return;
							}
						}
						StringBuilder reasonBuilder = new StringBuilder();
						for (int a = reasonStartPos; a < args.length; a++) {
							reasonBuilder.append(args[a] + " ");
						}
						reason = FormatUtils.FormatStringAll(reasonBuilder.toString().trim());
					}
					if (channel.isBungeeEnabled()) {
						sendBungeeCordMute(sender, args[1], channel, time, reason);
						return;
					}
					VentureChatPlayer playerToMute = playerApiService.getMineverseChatPlayer(args[1]);
					if (playerToMute == null || (!playerToMute.isOnline() && !sender.hasPermission("venturechat.mute.offline"))) {
						sender.sendMessage(LocalizedMessage.PLAYER_OFFLINE.toString().replace("{args}", args[1]));
						return;
					}
					if (playerToMute.getMutes().containsKey(channel.getName())) {
						sender.sendMessage(LocalizedMessage.PLAYER_ALREADY_MUTED.toString().replace("{player}", playerToMute.getName())
								.replace("{channel_color}", channel.getColor()).replace("{channel_name}", channel.getName()));
						return;
					}

					if (time > 0) {
						if (reason.isEmpty()) {
							playerToMute.getMutes().put(channel.getName(), new MuteContainer(channel.getName(), datetime + time, ""));
							String timeString = FormatUtils.parseTimeStringFromMillis(time);
							sender.sendMessage(LocalizedMessage.MUTE_PLAYER_SENDER_TIME.toString().replace("{player}", playerToMute.getName())
									.replace("{channel_color}", channel.getColor()).replace("{channel_name}", channel.getName()).replace("{time}", timeString));
							if (playerToMute.isOnline()) {
								playerToMute.getPlayer().sendMessage(LocalizedMessage.MUTE_PLAYER_PLAYER_TIME.toString().replace("{channel_color}", channel.getColor())
										.replace("{channel_name}", channel.getName()).replace("{time}", timeString));
							} else {
								playerToMute.setModified(true);
							}

							if(sender instanceof Player) {
								new MutePlayerEvent(playerToMute.getPlayer(), plugin.getServer().getPlayer(sender.getName()), Collections.singleton(channel), time).callEvent();
							} else {
								new MutePlayerEvent(playerToMute.getPlayer(), null, Collections.singleton(channel), time).callEvent();
							}

							return;
						} else {
							playerToMute.getMutes().put(channel.getName(), new MuteContainer(channel.getName(), datetime + time, reason));
							String timeString = FormatUtils.parseTimeStringFromMillis(time);
							sender.sendMessage(LocalizedMessage.MUTE_PLAYER_SENDER_TIME_REASON.toString().replace("{player}", playerToMute.getName())
									.replace("{channel_color}", channel.getColor()).replace("{channel_name}", channel.getName()).replace("{time}", timeString)
									.replace("{reason}", reason));
							if (playerToMute.isOnline()) {
								playerToMute.getPlayer().sendMessage(LocalizedMessage.MUTE_PLAYER_PLAYER_TIME_REASON.toString().replace("{channel_color}", channel.getColor())
										.replace("{channel_name}", channel.getName()).replace("{time}", timeString).replace("{reason}", reason));
							} else {
								playerToMute.setModified(true);
							}

							if(sender instanceof Player) {
								new MutePlayerEvent(playerToMute.getPlayer(), plugin.getServer().getPlayer(sender.getName()), Collections.singleton(channel), time, reason).callEvent();
							} else {
								new MutePlayerEvent(playerToMute.getPlayer(), null, Collections.singleton(channel), time, reason).callEvent();
							}

							return;
						}
					} else {
						if (reason.isEmpty()) {
							playerToMute.getMutes().put(channel.getName(), new MuteContainer(channel.getName(), 0, ""));
							sender.sendMessage(LocalizedMessage.MUTE_PLAYER_SENDER.toString().replace("{player}", playerToMute.getName())
									.replace("{channel_color}", channel.getColor()).replace("{channel_name}", channel.getName()));
							if (playerToMute.isOnline()) {
								playerToMute.getPlayer().sendMessage(
										LocalizedMessage.MUTE_PLAYER_PLAYER.toString().replace("{channel_color}", channel.getColor()).replace("{channel_name}", channel.getName()));
							} else {
								playerToMute.setModified(true);
							}

							if(sender instanceof Player) {
								new MutePlayerEvent(playerToMute.getPlayer(), plugin.getServer().getPlayer(sender.getName()), Collections.singleton(channel)).callEvent();
							} else {
								new MutePlayerEvent(playerToMute.getPlayer(), null, Collections.singleton(channel)).callEvent();
							}

							return;
						} else {
							playerToMute.getMutes().put(channel.getName(), new MuteContainer(channel.getName(), 0, reason));
							sender.sendMessage(LocalizedMessage.MUTE_PLAYER_SENDER_REASON.toString().replace("{player}", playerToMute.getName())
									.replace("{channel_color}", channel.getColor()).replace("{channel_name}", channel.getName()).replace("{reason}", reason));
							if (playerToMute.isOnline()) {
								playerToMute.getPlayer().sendMessage(LocalizedMessage.MUTE_PLAYER_PLAYER_REASON.toString().replace("{channel_color}", channel.getColor())
										.replace("{channel_name}", channel.getName()).replace("{reason}", reason));
							} else {
								playerToMute.setModified(true);
							}

							if(sender instanceof Player) {
								new MutePlayerEvent(playerToMute.getPlayer(), plugin.getServer().getPlayer(sender.getName()), Collections.singleton(channel), 0, reason).callEvent();
							} else {
								new MutePlayerEvent(playerToMute.getPlayer(), null, Collections.singleton(channel), 0, reason).callEvent();
							}

							return;
						}
					}
				}
				sender.sendMessage(LocalizedMessage.CHANNEL_CANNOT_MUTE.toString().replace("{channel_color}", channel.getColor()).replace("{channel_name}", channel.getName()));
				return;
			}
			sender.sendMessage(LocalizedMessage.INVALID_CHANNEL.toString().replace("{args}", args[0]));
			return;
		}
		sender.sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], configService.getChatChannels().stream().map(ChatChannel::getName).collect(Collectors.toList()), completions);
			Collections.sort(completions);
			return completions;
		}
		if (args.length == 2) {
			if (configService.isChannel(args[0])) {
				ChatChannel chatChannelObj = configService.getChannel(args[0]);
				if (chatChannelObj.isBungeeEnabled()) {
					StringUtil.copyPartialMatches(args[1], playerApiService.getNetworkPlayerNames(), completions);
					Collections.sort(completions);
					return completions;
				}
				StringUtil.copyPartialMatches(args[1], playerApiService.getOnlineMineverseChatPlayers().stream().filter(mcp -> !mcp.getMutes().containsKey(chatChannelObj.getName()))
						.map(VentureChatPlayer::getName).collect(Collectors.toList()), completions);
				Collections.sort(completions);
				return completions;
			}
		}
		if (args.length == 3) {
			StringUtil.copyPartialMatches(args[2], COMMON_MUTE_TIMES, completions);
			Collections.sort(completions);
			return completions;

		}
		return Collections.emptyList();
	}

	private void sendBungeeCordMute(CommandSender sender, String playerToMute, ChatChannel channel, long time, String reason) {
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteOutStream);
		try {
			out.writeUTF("Mute");
			out.writeUTF("Send");
			if (sender instanceof Player) {
				out.writeUTF(((Player) sender).getUniqueId().toString());
			} else {
				out.writeUTF("VentureChat:Console");
			}
			out.writeUTF(playerToMute);
			out.writeUTF(channel.getName());
			out.writeLong(time);
			out.writeUTF(reason);
			pluginMessageController.sendPluginMessage(byteOutStream);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
