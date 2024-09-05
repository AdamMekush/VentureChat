package venture.Aust1n46.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import venture.Aust1n46.chat.api.interfaces.IMuteContainer;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MuteContainer implements IMuteContainer {
	private String channel;
	private long duration;
	private String reason;

	@Override
	public String getReason() {
		return reason;
	}
}
