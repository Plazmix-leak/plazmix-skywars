package net.plazmix.skywars.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SkywarsHelper {

    public <T> List<T>[] chunkify(List<T> list, int chunkSize) {
        int elementsLength = list.size() / chunkSize;

        List<T>[] result = new List[chunkSize];

        for (int index = 0; index < result.length; index++) {
            result[index] = list.stream()
                    .skip((long) index * elementsLength)
                    .limit(elementsLength)

                    .collect(Collectors.toList());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Location>[] splitLocations(int elementsLength, List<Location> array) {
        int arraysCount = (array.size() / elementsLength)
                + (array.size() % elementsLength != 0 ? array.size() % elementsLength : 0);

        List<Location>[] result = new List[arraysCount];

        for (int index = 0; index < result.length; index++) {
            result[index] = array.stream()
                    .skip((long) index * elementsLength)
                    .limit(elementsLength)

                    .collect(Collectors.toList());
        }

        return result;
    }

}
