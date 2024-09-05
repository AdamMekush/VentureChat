package venture.Aust1n46.chat.model;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
	private WrappedChatComponent component;
	private String message;
	private String coloredMessage;
	private int hash;
}
