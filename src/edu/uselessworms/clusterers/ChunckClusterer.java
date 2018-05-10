package edu.uselessworms.clusterers;

import edu.uselessworms.locations.House;

import java.util.ArrayList;
import java.util.List;

public class ChunckClusterer {
    public static <T>ArrayList<ArrayList<T>> chopIntoParts(final ArrayList<T> ls, final int iParts ) {
        final ArrayList<ArrayList<T>> lsParts = new ArrayList<ArrayList<T>>();
        final int iChunkSize = ls.size() / iParts;
        int iLeftOver = ls.size() % iParts;
        int iTake = iChunkSize;

        for (int i = 0, iT = ls.size(); i < iT; i += iTake) {
            if (iLeftOver > 0) {
                iLeftOver--;

                iTake = iChunkSize + 1;
            } else {
                iTake = iChunkSize;
            }

            lsParts.add(new ArrayList<T>(ls.subList(i, Math.min(iT, i + iTake))));
        }

        return lsParts;
    }
}
