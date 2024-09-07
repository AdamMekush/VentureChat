package venture.Aust1n46.chat.api.events;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.model.IChatChannel;
import venture.Aust1n46.chat.model.IVentureChatPlayer;

import java.util.Set;

// TODO
public class KickChannelPlayerEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @Getter
    private IVentureChatPlayer victim;
    @Getter
    private IVentureChatPlayer operator;
    @Getter
    private Set<IChatChannel> channels;

    public KickChannelPlayerEvent(IVentureChatPlayer victim, IVentureChatPlayer operator, Set<IChatChannel> channels) {
        this.victim = victim;
        this.operator = operator;
        this.channels = channels;
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
