package venture.Aust1n46.chat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.model.IChatChannel;

import java.util.Set;

public class MutePlayerEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private Player victim;
	private Player operator;
	private Set<IChatChannel> channels;
	private String reason = null;
	private long time = 0;

	public MutePlayerEvent(Player victim, Player operator, Set<IChatChannel> channels, long time, String reason) {
		this(victim, operator, channels, time);
		this.reason = reason;
	}

	public MutePlayerEvent(Player victim, Player operator, Set<IChatChannel> channels, long time) {
		this(victim, operator, channels);
		this.time = time;
	}

	public MutePlayerEvent(Player victim, Player operator, Set<IChatChannel> channels) {
		this.victim = victim;
		this.operator = operator;
		this.channels = channels;
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

	public Player getVictim() {
		return this.victim;
	}

	public Player getOperator() {
		return this.operator;
	}

	public Set<IChatChannel> getChannels() {
		return this.channels;
	}

	public long getTime() {
		return this.time;
	}

	public String getReason(){
		return  this.reason;
	}
}
