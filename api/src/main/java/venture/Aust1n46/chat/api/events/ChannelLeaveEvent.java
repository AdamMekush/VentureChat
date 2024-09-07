package venture.Aust1n46.chat.api.events;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.model.IChatChannel;
import venture.Aust1n46.chat.model.IVentureChatPlayer;

// TODO
public class ChannelLeaveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    @Getter
    private IVentureChatPlayer player;
    @Getter
    private IChatChannel channel;

    public ChannelLeaveEvent(IVentureChatPlayer player, IChatChannel channel) {
        this.player = player;
        this.channel = channel;

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