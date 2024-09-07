package venture.Aust1n46.chat.api.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.model.IChatChannel;
import venture.Aust1n46.chat.model.IVentureChatPlayer;


/**
 * Event called when a player attempts to join a valid channel
 */
public class ChannelJoinEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @Getter
    private IVentureChatPlayer player;
    @Getter
    @Setter
    private IChatChannel channel;
    @Setter
    @Getter
    private String message;

    public ChannelJoinEvent(IVentureChatPlayer player, IChatChannel channel, String message) {
        this.player = player;
        this.channel = channel;
        this.message = message;
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