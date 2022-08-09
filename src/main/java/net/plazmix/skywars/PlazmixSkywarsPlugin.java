package net.plazmix.skywars;

import net.plazmix.game.GamePlugin;
import net.plazmix.game.installer.GameInstaller;
import net.plazmix.game.installer.GameInstallerTask;
import net.plazmix.game.mysql.type.BasedGameItemsMysqlDatabase;
import net.plazmix.game.mysql.type.UpgradeGameItemsMysqlDatabase;
import net.plazmix.game.utility.GameSchedulers;
import net.plazmix.skywars.database.DeathAngelMysqlDatabase;
import net.plazmix.skywars.database.SkywarsStatsMysqlDatabase;
import net.plazmix.skywars.item.cage.CageCategory;
import net.plazmix.skywars.item.dance.DanceCategory;
import net.plazmix.skywars.item.killeffect.KillEffectCategory;
import net.plazmix.skywars.item.kit.KitCategory;
import net.plazmix.skywars.item.perk.PerkCategory;
import net.plazmix.skywars.item.predeath.PreDeathCategory;
import net.plazmix.skywars.listener.StaticChatListener;
import net.plazmix.skywars.state.EndingState;
import net.plazmix.skywars.state.IngameState;
import net.plazmix.skywars.state.WaitingState;
import net.plazmix.skywars.util.GameConstants;
import net.plazmix.skywars.util.SkywarsMode;
import net.plazmix.skywars.util.deathangel.SkywarsDamnService;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;


/*  Leaked by https://t.me/leak_mine
    - Все слитые материалы вы используете на свой страх и риск.

    - Мы настоятельно рекомендуем проверять код плагинов на хаки!
    - Список софта для декопиляции плагинов:
    1. Luyten (последнюю версию можно скачать можно тут https://github.com/deathmarine/Luyten/releases);
    2. Bytecode-Viewer (последнюю версию можно скачать можно тут https://github.com/Konloch/bytecode-viewer/releases);
    3. Онлайн декомпиляторы https://jdec.app или http://www.javadecompilers.com/

    - Предложить свой слив вы можете по ссылке @leakmine_send_bot или https://t.me/leakmine_send_bot
*/


public final class PlazmixSkywarsPlugin extends GamePlugin {

    @Override
    public GameInstallerTask getInstallerTask() {
        return new PlazmixSkywarsInstaller(SkywarsMode.getCurrentMode(this), this);
    }

    @Override
    protected void handleEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new StaticChatListener(), this);

        // Applying game service settings.
        SkywarsMode skywarsMode = SkywarsMode.valueOf(getConfig().getString("mode", "SOLO"));

        service.setGameName(GameConstants.TITLE);

        service.setServerMode(skywarsMode.getTitle());
        service.setMaxPlayers(skywarsMode.getMaxPlayersInTeam() * getCache().getInt(GameConstants.ISLANDS_COUNT_CACHE));

        // Applying skywars states.
        service.registerState(new WaitingState(this));
        service.registerState(new IngameState(this));
        service.registerState(new EndingState(this));

        // Applying skywars statistic database.
        service.addGameDatabase(new BasedGameItemsMysqlDatabase("BSkywars"));
        service.addGameDatabase(new UpgradeGameItemsMysqlDatabase("USkywars"));
        service.addGameDatabase(new SkywarsStatsMysqlDatabase(skywarsMode));
        service.addGameDatabase(new DeathAngelMysqlDatabase());

        // Register skywars shop items.
        service.registerItemsCategory(new KitCategory());
        service.registerItemsCategory(new PerkCategory());
        service.registerItemsCategory(new CageCategory());
        service.registerItemsCategory(new PreDeathCategory());
        service.registerItemsCategory(new KillEffectCategory());
        service.registerItemsCategory(new DanceCategory());

        // Executing skywars arena map installer.
        GameInstaller.create().executeInstall(getInstallerTask());

        // Start world time ticker
        GameSchedulers.runTimer(20, 20, () -> {

            for (Player player : getServer().getOnlinePlayers()) {
                player.setPlayerWeather(WeatherType.CLEAR);

                if (!SkywarsDamnService.INSTANCE.isActive()) {
                    player.setPlayerTime(1_200, false);
                }
                else {
                    player.setPlayerTime(18_000, false);
                }
            }
        });
    }

    @Override
    protected void handleDisable() {
        // nothing.
    }

}
