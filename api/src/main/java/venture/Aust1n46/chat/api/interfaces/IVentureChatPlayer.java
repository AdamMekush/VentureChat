package venture.Aust1n46.chat.api.interfaces;

import org.bukkit.entity.Player;

import java.util.*;

public interface IVentureChatPlayer {
    String getName();

    UUID getUuid();

    boolean isBungeeToggle();

    Player getPlayer();

    Map<String, IMuteContainer> getMutes();

    void setQuickChat(boolean b);

    String getJsonFormat();

    UUID getConversation();

    boolean isFilterEnabled();

    void setReplyPlayer(UUID player);

    boolean isNotifications();

    boolean isOnline();

    Collection<UUID> getIgnores();

    boolean isMessageToggle();

    void setSpy(boolean b);

    UUID getParty();

    boolean isEditing();

    boolean isSpy();

    Set<String> getListening();

    List<IChatMessage> getMessages();

    boolean isHasPlayed();

    void setNotifications(boolean b);

    IChatChannel getCurrentChannel();

    void setCurrentChannel(IChatChannel defaultChannel);

    void setHasPlayed(boolean b);

    void setMessageToggle(boolean b);

    void setCommandSpy(boolean b);

    void setBungeeToggle(boolean b);

    Set<String> getBlockedCommands();

    void setRangedSpy(boolean b);

    boolean isCommandSpy();

    void setFilterEnabled(boolean b);

    boolean isRangedSpy();

    boolean isQuickChat();

    IChatChannel getQuickChannel();

    UUID getReplyPlayer();

    void setConversation(UUID player);

    void setQuickChannel(IChatChannel channel);

    boolean isHost();

    void setHost(boolean b);

    //void setPlayer(Player player);
    void setPlayer(UUID player);

    boolean isPartyChat();

    void setEditing(boolean b);

    void setOnline(boolean b);

    boolean isModified();

    void setPlayer(Player player);

    void setParty(UUID uuid);

    void setModified(boolean b);

    void setPartyChat(boolean b);

    Map<IChatChannel, Long> getCooldowns();

    Map<IChatChannel, List<Long>> getSpam();
}
