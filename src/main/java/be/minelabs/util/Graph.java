package be.minelabs.util;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic implementation of undirected graph.
 * Intended to be used for Molecule structure.
 *
 * @param <V> Vertex data class
 * @param <E> Edge data class
 */
public class Graph<V, E> {

    public class Vertex {
        public V data;

        private final List<Edge> edges = new ArrayList<>();

        private Vertex(V data) {
            this.data = data;
        }

        public List<Vertex> getNeighbours() {
            return edges.stream().flatMap(edge -> edge.getVertices().stream().filter(vertex -> vertex != this)).collect(Collectors.toList());
        }

        public List<V> getNeighboursData() {
            return getNeighbours().stream().map(vertex -> vertex.data).collect(Collectors.toList());
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public List<E> getEdgesData() {
            return edges.stream().map(edge -> edge.data).collect(Collectors.toList());
        }

        /**
         * Return canonical representation of vertex with edges.
         * Only works if toString methods of edge and vertex data return canonical representations
         */
        public String toCanonical() {
            StringBuilder builder = new StringBuilder();
            builder.append(data.toString()).append("\n");
            edges.stream().map(edge ->
                            "\t-- (" + edge.data.toString() + ")\t" + edge.getOtherVertex(this).data.toString() + "\n")
                    .sorted().forEachOrdered(builder::append);
            return builder.toString();
        }

        @Override
        public String toString() {
            return toCanonical();
        }
    }

    public class Edge {
        private final LinkedHashSet<Vertex> vertices;
        public E data;

        protected Edge(Set<Vertex> vertices, E data) {
            assert vertices.size() == 2;
            this.vertices = new LinkedHashSet<>(vertices);
            this.data = data;
        }

        public Collection<Vertex> getVertices() {
            return vertices;
        }

        public Vertex getFirst() {
            return vertices.stream().toList().get(0);
        }

        public Vertex getSecond() {
            return vertices.stream().toList().get(1);
        }

        private Vertex getOtherVertex(Vertex ignore) {
            if (!vertices.contains(ignore))
                throw new RuntimeException("Tried to find other end of edge from a vertex that isn't connected.");
            return vertices.stream().filter(v -> v != ignore).findFirst().orElseThrow(() -> new RuntimeException("Invalid edge: has less than 2 entries"));
        }
    }

    protected final List<Vertex> vertices = new ArrayList<>();

    protected final LinkedHashMap<Set<Vertex>, Edge> edges = new LinkedHashMap<>();

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Collection<Edge> getEdges() {
        return edges.values();
    }

    @Nullable
    public Edge getEdge(Vertex v1, Vertex v2) {
        Set<Vertex> vertices = Set.of(v1, v2);
        return edges.getOrDefault(vertices, null);
    }

    public Vertex addVertex(V data) {
        Vertex v = new Vertex(data);
        vertices.add(v);
        return v;
    }

    public Edge addEdge(Vertex v1, Vertex v2, E data) throws IllegalArgumentException {
        Set<Vertex> vertices = Set.of(v1, v2);
        if (edges.containsKey(vertices))
            throw new IllegalArgumentException("Edge already exists");
        Edge edge = new Edge(vertices, data);

        // keep vertices consistent
        v1.edges.add(edge);
        v2.edges.add(edge);

        // store in graph
        edges.put(vertices, edge);
        return edge;
    }

    public void removeEdge(Edge e) {
        for (Vertex v : e.vertices) {
            v.edges.remove(e);
        }
        edges.remove(e.vertices);
    }

    public Collection<V> getVertexData() {
        return vertices.stream().map(vertex -> vertex.data).collect(Collectors.toList());
    }

    public boolean isIsomorphicTo(Graph<V, E> other) {
        if (vertices.size() != other.vertices.size())
            return false;
        if (edges.size() != other.edges.size())
            return false;

        // Missing structure check
        // Missing edges check
        throw new UnsupportedOperationException("Generic graph isomorphism isn't implemented");
    }

    /**
     * Create canonical string representation of graph.
     */
    public String toCanonical() {
        StringBuilder builder = new StringBuilder();
        builder.append("Undirected graph\n");
        vertices.stream().map(vertex -> vertex.toCanonical() + "\n").sorted().forEachOrdered(builder::append);
        return builder.toString();
    }

    @Override
    public String toString() {
        return toCanonical();
    }

    public boolean isConnectedManagerFunctieOmdatJoeyZaagtZoalsVaak() {
        // Dit is de beste functie die sowiezo nooit gaat crashen <3 LIEEEESSSSSS
        // we've been bamboozled!!!!!!!!!!!!!
        // NEVERRRRRRRRRRRRRRR! I AM ALL POWERFUL! I AM GOD

        if (vertices.size() == 0)
            return true;

        List<Vertex> vertices = new ArrayList<>();
        vertices.add(this.vertices.get(0));
        boolean hasAdded = true;
        while (hasAdded) {
            hasAdded = false;
            List<Vertex> copy = new ArrayList<>(vertices);
            for (Vertex v : copy) {
                for (Vertex w : v.getNeighbours()) {
                    if (!vertices.contains(w)) {
                        vertices.add(w);
                        hasAdded = true;
                    }
                }
            }
        }
        return (vertices.size() == this.vertices.size());
    }
}
