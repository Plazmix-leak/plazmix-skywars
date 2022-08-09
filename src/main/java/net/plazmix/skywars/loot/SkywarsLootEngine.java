package net.plazmix.skywars.loot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.plazmix.utility.PercentUtil;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkywarsLootEngine {

    @Getter
    private static final SkywarsLootEngine instance = new SkywarsLootEngine();

    public SkywarsChestManager createChestManager(List<Chest> chestList) {
        return new SkywarsChestManager(chestList);
    }

    private void handleExactlyElements(SkywarsChestManager skywarsChestManager, List<SkywarsLootUnit> exactlyElementsList) {
        List<SkywarsLootUnit> copiedExactlyList = new ArrayList<>(exactlyElementsList);

        while (!copiedExactlyList.isEmpty()) {
            SkywarsLootUnit randomElement = copiedExactlyList.stream().skip((long) (Math.random() * copiedExactlyList.size()))
                    .findFirst()
                    .orElse(null);

            if (randomElement == null) {
                break;
            }

            copiedExactlyList.remove(randomElement);

            try {
                skywarsChestManager.addLootElement(randomElement);
            }
            catch (SkywarsLootException exception) {
                continue;
            }

            for (SkywarsLootUnit excludeElement : randomElement.getExcludeList()) {
                copiedExactlyList.remove(excludeElement);
            }
        }
    }

    private void handleProbablyElements(SkywarsChestManager skywarsChestManager, List<SkywarsLootUnit> probablyElementsList) {
        List<SkywarsLootUnit> copiedProbablyList = new ArrayList<>(probablyElementsList);

        while (!copiedProbablyList.isEmpty()) {
            SkywarsLootUnit randomElement = copiedProbablyList.stream().skip((long) (Math.random() * copiedProbablyList.size()))
                    .findFirst()
                    .orElse(null);

            if (randomElement == null) {
                break;
            }

            copiedProbablyList.remove(randomElement);

            if (!PercentUtil.acceptRandomPercent(randomElement.getChance())) {
                continue;
            }

            for (SkywarsLootUnit excludeElement : randomElement.getExcludeList()) {
                copiedProbablyList.remove(excludeElement);
            }

            try {
                skywarsChestManager.addLootElement(randomElement);
            }
            catch (SkywarsLootException ignored) {
            }
        }
    }

    public void fillChestManager(
            SkywarsChestManager skywarsChestManager,

            List<SkywarsLootUnit> exactlyElementsList,
            List<SkywarsLootUnit> probablyElementsList
    ) {
        this.handleExactlyElements(skywarsChestManager, exactlyElementsList);
        this.handleProbablyElements(skywarsChestManager, probablyElementsList);
    }

}
