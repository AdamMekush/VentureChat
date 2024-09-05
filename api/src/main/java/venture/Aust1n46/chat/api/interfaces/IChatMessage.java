package venture.Aust1n46.chat.api.interfaces;

import com.comphenix.protocol.wrappers.WrappedChatComponent;

public interface IChatMessage {
    int getHash();

    void setComponent(com.comphenix.protocol.wrappers.WrappedChatComponent removedComponent);

    void setHash(int i);

    String getMessage();

    WrappedChatComponent getComponent();

    String getColoredMessage();
}
