package venture.Aust1n46.chat.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import venture.Aust1n46.chat.model.ChatChannel;

import java.util.HashSet;
import java.util.Set;

/**
 * Event called when player has been unmuted.
 * This event can be cancelled.
 *
 * @author AdamMekush
 */
public class UnmutePlayerEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private Player victim;
    private Player operator;
    private Set<ChatChannel> channels;

    public UnmutePlayerEvent(Player victim, Player operator, Set<ChatChannel> channels) {
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

    public void setChannel(Set<ChatChannel> channels) {
        this.channels = channels;
    }

    public Set<ChatChannel> getChannels() {
        return this.channels;
    }
}
