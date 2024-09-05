package venture.Aust1n46.chat.model;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.*;
import venture.Aust1n46.chat.api.interfaces.IChatMessage;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements IChatMessage {
	private WrappedChatComponent component;
	private String message;
	private String coloredMessage;
	private int hash;
}
