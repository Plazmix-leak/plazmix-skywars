package net.plazmix.skywars.database;

import lombok.NonNull;
import net.plazmix.game.GamePlugin;
import net.plazmix.game.mysql.GameMysqlDatabase;
import net.plazmix.game.mysql.RemoteDatabaseRowType;
import net.plazmix.game.user.GameUser;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.skywars.util.SkywarsMode;

public class SkywarsStatsMysqlDatabase extends GameMysqlDatabase {

    private final SkywarsMode skywarsMode;

    public SkywarsStatsMysqlDatabase(@NonNull SkywarsMode skywarsMode) {
        super("Skywars" + skywarsMode.getTitle(), true);

        this.skywarsMode = skywarsMode;
    }

    @Override
    public void initialize() {
        addColumn(GameConstants.DATABASE_PLAYER_EXP, RemoteDatabaseRowType.INT, user -> user.getCache().getInt(GameConstants.DATABASE_PLAYER_EXP));

        addColumn(GameConstants.DATABASE_PLAYER_WINS, RemoteDatabaseRowType.INT, user -> user.getCache().getInt(GameConstants.DATABASE_PLAYER_KILLS));
        addColumn(GameConstants.DATABASE_PLAYER_KILLS, RemoteDatabaseRowType.INT, user -> user.getCache().getInt(GameConstants.DATABASE_PLAYER_KILLS));
        addColumn(GameConstants.DATABASE_PLAYER_CHEST_FOUND, RemoteDatabaseRowType.INT, user -> user.getCache().getInt(GameConstants.DATABASE_PLAYER_CHEST_FOUND));

        if (skywarsMode == SkywarsMode.RANKED) {
            addColumn(GameConstants.DATABASE_PLAYER_RATING, RemoteDatabaseRowType.INT, user -> user.getCache().getInt(GameConstants.DATABASE_PLAYER_RATING));
        }
    }

    @Override
    public void onJoinLoad(@NonNull GamePlugin gamePlugin, @NonNull GameUser gameUser) {
        loadPrimary(true, gameUser, gameUser.getCache()::set);
    }
}
