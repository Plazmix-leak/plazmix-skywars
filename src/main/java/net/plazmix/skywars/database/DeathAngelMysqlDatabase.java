package net.plazmix.skywars.database;

import lombok.NonNull;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.mysql.GameMysqlDatabase;
import net.plazmix.game.mysql.RemoteDatabaseRowType;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.skywars.util.SkywarsMode;

public class DeathAngelMysqlDatabase extends GameMysqlDatabase {

    public DeathAngelMysqlDatabase() {
        super("SkywarsDeathAngel", true);
    }

    @Override
    public void initialize() {
        addColumn(GameConstants.DATABASE_PLAYER_SOULS, RemoteDatabaseRowType.INT, user -> user.getCache().getInt(GameConstants.DATABASE_PLAYER_SOULS));
        addColumn(GameConstants.DATABASE_PLAYER_DAMN_PERCENT, RemoteDatabaseRowType.INT, user -> user.getCache().getInt(GameConstants.DATABASE_PLAYER_DAMN_PERCENT));
    }

    @Override
    public void onJoinLoad(@NonNull GamePlugin gamePlugin, @NonNull GameUser gameUser) {
        loadPrimary(true, gameUser, gameUser.getCache()::set);

        if (gameUser.getCache().getInt(GameConstants.DATABASE_PLAYER_DAMN_PERCENT) <= 0) {
            gameUser.getCache().set(GameConstants.DATABASE_PLAYER_DAMN_PERCENT, 1);
        }
    }
}
