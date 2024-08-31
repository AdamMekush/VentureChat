package venture.Aust1n46.chat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.model.ChatChannel;

/**
 * Event called when a player leaves a channel
 * This event can be cancelled.
 *
 * @author AdamMekush
 */
public class ChannelLeaveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Player player;
    private ChatChannel channel;

    public ChannelLeaveEvent(Player player, ChatChannel channel) {
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

    public Player getPlayer() {
        return this.player;
    }

    public ChatChannel getChannel() {
        return this.channel;
    }
}