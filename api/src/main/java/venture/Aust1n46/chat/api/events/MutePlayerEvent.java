package venture.Aust1n46.chat.api.events;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.model.IChatChannel;
import venture.Aust1n46.chat.model.IVentureChatPlayer;

import java.util.Set;

public class MutePlayerEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	@Getter
    private IVentureChatPlayer victim;
	@Getter
    private IVentureChatPlayer operator;
	@Getter
    private Set<IChatChannel> channels;
	@Getter
    private String reason = null;
	@Getter
    private long time = 0;

	public MutePlayerEvent(IVentureChatPlayer victim, IVentureChatPlayer operator, Set<IChatChannel> channels, long time, String reason) {
		this(victim, operator, channels, time);
		this.reason = reason;
	}

	public MutePlayerEvent(IVentureChatPlayer victim, IVentureChatPlayer operator, Set<IChatChannel> channels, long time) {
		this(victim, operator, channels);
		this.time = time;
	}

	public MutePlayerEvent(IVentureChatPlayer victim, IVentureChatPlayer operator, Set<IChatChannel> channels) {
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

}
