package venture.Aust1n46.chat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.api.interfaces.IChatChannel;

public class MutePlayerEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private Player player;
	private IChatChannel channel;
	private int time;

	public MutePlayerEvent(Player player, IChatChannel channel, int time) {
		this.player = player;
		this.channel = channel;
		this.time = time;
		this.cancelled = false;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setChannel(IChatChannel channel) {
		this.channel = channel;
	}

	public IChatChannel getChannel() {
		return this.channel;
	}

	public int getTime() {
		return this.time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
