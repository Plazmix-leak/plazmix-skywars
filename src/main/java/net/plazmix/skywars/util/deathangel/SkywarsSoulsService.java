package net.plazmix.skywars.util.deathangel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.plazmix.game.GameCache;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.mysql.GameMysqlDatabase;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.database.DeathAngelMysqlDatabase;
import net.plazmix.skywars.util.GameConstants;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkywarsSoulsService {

    private GameMysqlDatabase getDatabase() {
        return GamePlugin.getInstance().getService().getGameDatabase(DeathAngelMysqlDatabase.class);
    }

    private GameCache getPlayerCache(String playerName, String onLoadCacheID) {
        GamePlugin gamePlugin = GamePlugin.getInstance();

        GameUser gameUser = GameUser.from(playerName);
        GameCache gameCache = gameUser.getCache();

        if (!gameCache.contains(onLoadCacheID)) {
            this.getDatabase().onJoinLoad(gamePlugin, gameUser);
        }

        return gameCache;
    }

    public int getSouls(String playerName) {
        String id = (GameConstants.DATABASE_PLAYER_SOULS);
        return this.getPlayerCache(playerName, id).getInt(id);
    }

    public void addSouls(String playerName, int value) {
        String id = (GameConstants.DATABASE_PLAYER_SOULS);

        GameCache gameCache = this.getPlayerCache(playerName, id);
        gameCache.add(id, value);

        this.getDatabase().insert(false, GameUser.from(playerName));
    }

    public void removeSouls(String playerName, int value) {
        this.addSouls(playerName, -value);
    }

}
