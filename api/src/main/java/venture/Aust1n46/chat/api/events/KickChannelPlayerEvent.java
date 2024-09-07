package venture.Aust1n46.chat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.model.IChatChannel;

import java.util.Set;

// TODO
public class KickChannelPlayerEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Player victim;
    private Player operator;
    private Set<IChatChannel> channels;

    public KickChannelPlayerEvent(Player victim, Player operator, Set<IChatChannel> channels) {
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

    public Player getVictim() {
        return this.victim;
    }

    public Player getOperator() {
        return this.operator;
    }

    public Set<IChatChannel> getChannels() {
        return this.channels;
    }
}
