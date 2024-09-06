package venture.Aust1n46.chat.model;

import com.comphenix.protocol.wrappers.WrappedChatComponent;

public interface IChatMessage {
    int getHash();

    void setComponent(WrappedChatComponent removedComponent);

    void setHash(int i);

    String getMessage();

    WrappedChatComponent getComponent();

    String getColoredMessage();
}
