package venture.Aust1n46.chat.api.interfaces;

import org.bukkit.entity.Player;

import java.util.Map;

public interface IVentureChatPlayer {
    String getName();

    Object getUuid();

    boolean isBungeeToggle();

    Player getPlayer();

    Map<String, IMuteContainer> getMutes();

    void setQuickChat(boolean b);
}
