package venture.Aust1n46.chat.service;

import com.google.inject.Singleton;
import venture.Aust1n46.chat.model.SynchronizedVentureChatPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

@Singleton
public class ProxyPlayerApiService {
	private final HashMap<UUID, SynchronizedVentureChatPlayer> proxyPlayerMap = new HashMap<>();

	public void addSynchronizedMineverseChatPlayerToMap(SynchronizedVentureChatPlayer smcp) {
		proxyPlayerMap.put(smcp.getUuid(), smcp);
	}

	public void clearProxyPlayerMap() {
		proxyPlayerMap.clear();
	}

	public Collection<SynchronizedVentureChatPlayer> getSynchronizedMineverseChatPlayers() {
		return proxyPlayerMap.values();
	}

	/**
	 * Get a SynchronizedMineverseChatPlayer from a UUID.
	 *
	 * @param uuid {@link UUID}
	 * @return {@link SynchronizedVentureChatPlayer}
	 */
	public SynchronizedVentureChatPlayer getSynchronizedMineverseChatPlayer(UUID uuid) {
		return proxyPlayerMap.get(uuid);
	}
}
