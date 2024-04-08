package be.minelabs.recipe.molecules;

import be.minelabs.science.Atom;
import be.minelabs.util.Graph;

import java.util.ArrayList;

public class MoleculeGraph extends Graph<Atom, Bond> {
    /**
     * Computes the amount of remaining bonds an atom can make based on its already made bonds.
     */
    public int getOpenConnections(Vertex v) {
        int maxBonds = v.data.getMaxPossibleBonds();
        return maxBonds - v.getEdgesData().stream().map(bond -> bond.bondOrder).mapToInt(Integer::intValue).sum();
    }

    /**
     * Increments the bondOrder between to neighbouring atoms. Returns whether any change was made.
     */
    public boolean incrementBond(Vertex v1, Vertex v2) {
        if (v1 == null || v2 == null) return false;
        Edge edge = getEdge(v1, v2);
        if (edge == null)
            throw new IllegalArgumentException("Can only increment bonds between neighbouring atoms");
        try {
            edge.data = edge.data.higher();
        } catch (UnsupportedOperationException e) {
            // Couldn't increment bond -> nothing changed.
            return false;
        }
        return true;
    }

    public void removeZeroBonds() {
        for (Edge edge : new ArrayList<>(getEdges())) {
            if (edge.data == Bond.COVALENT_ZERO) {
                removeEdge(edge);
            }
        }
    }


    @Override
    public boolean isIsomorphicTo(Graph<Atom, Bond> other) {
        if (getVertices().size() != other.getVertices().size())
            return false;
        if (getEdges().size() != other.getEdges().size())
            return false;

        // for the simple graphs we deal with, comparing the canonical string representation should suffice.
        return toCanonical().equals(other.toCanonical());
    }

    public int getTotalOpenConnections() {
        int totalOpenConnections = 0;
        for (Vertex vertex : new ArrayList<>(getVertices())) {
            totalOpenConnections += getOpenConnections(vertex);
        }
        return totalOpenConnections;
    }
}
