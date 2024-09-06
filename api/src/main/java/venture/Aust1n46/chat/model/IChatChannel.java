package venture.Aust1n46.chat.model;

public interface IChatChannel {
    boolean isPermissionRequired();

    String getPermission();

    String getName();

    boolean isBungeeEnabled();

    boolean isDefaultChannel();

    boolean isSpeakPermissionRequired();

    String getSpeakPermission();

    int getCooldown();

    boolean isFiltered();

    String getFormat();

    CharSequence getColor();

    double getDistance();

    String getChatColor();

    String getAlias();

    String getPrefix();
}
