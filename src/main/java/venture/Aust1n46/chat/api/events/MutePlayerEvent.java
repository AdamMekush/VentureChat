package venture.Aust1n46.chat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import venture.Aust1n46.chat.model.ChatChannel;

import java.util.Collection;

public class MutePlayerEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private Player victim;
	private Player operator;
	private Collection<ChatChannel> channels;
	private String reason = null;
	private long time = 0;

	public MutePlayerEvent(Player victim, Player operator, Collection<ChatChannel> channels, long time, String reason) {
		this(victim, operator, channels, time);
		this.reason = reason;
	}

	public MutePlayerEvent(Player victim, Player operator, Collection<ChatChannel> channels, long time) {
		this(victim, operator, channels);
		this.time = time;
	}

	public MutePlayerEvent(Player victim, Player operator, Collection<ChatChannel> channels) {
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

	public void setChannel(Collection<ChatChannel> channels) {
		this.channels = channels;
	}

	public Collection<ChatChannel> getChannels() {
		return this.channels;
	}

	public long getTime() {
		return this.time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getReason(){
		return  this.reason;
	}
}
