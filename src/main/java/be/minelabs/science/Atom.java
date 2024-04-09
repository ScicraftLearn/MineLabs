package be.minelabs.science;

import be.minelabs.Minelabs;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Atom {
    HYDROGEN(1, "H", 2.1, AtomType.NON_METAL, 1, 0, Color.WHITE, new int[]{1}),
    HELIUM(2, "He", 0, AtomType.NOBLE_GAS, 2, 2, Atom.Color.CYAN, new int[]{0}),
    LITHIUM(3, "Li", 1.0, AtomType.ALKALI_METAL, 1, 4, Color.VIOLET, new int[]{1}),
    BERYLLIUM(4, "Be", 1.5, AtomType.ALKALINE_EARTH_METAL, 2, 5, Color.DARK_GREEN, new int[]{2}),
    BORON(5, "B", 2.0, AtomType.NON_METAL, 3, 6, Color.BEIGE, new int[]{3}),
    CARBON(6, "C", 2.5, AtomType.NON_METAL, 4, 6, Atom.Color.BLACK, new int[]{4}),
    NITROGEN(7, "N", 3.0, AtomType.NON_METAL, 5, 7, Color.BLUE, new int[]{3, 4}),  // TODO CHECK
    OXYGEN(8, "O", 3.5, AtomType.NON_METAL, 6, 8, Color.RED, new int[]{2}),
    FLUORINE(9, "F", 4.0, AtomType.NON_METAL, 7, 10, Color.GREEN, new int[]{1}),
    NEON(10, "Ne", 0, AtomType.NOBLE_GAS, 8, 10, Atom.Color.CYAN, new int[]{0}),
    SODIUM(11, "Na", 0.9, AtomType.ALKALI_METAL, 1, 12, Color.VIOLET, new int[]{1}),
    MAGNESIUM(12, "Mg", 1.2, AtomType.ALKALINE_EARTH_METAL, 2, 12, Color.DARK_GREEN, new int[]{2}),
    ALUMINIUM(13, "Al", 1.5, AtomType.POST_TRANSITION_METAL, 3, 14, Color.GRAY, new int[]{3}),
    SILICON(14, "Si", 1.8, AtomType.NON_METAL, 4, 14, Color.BEIGE, new int[]{4}),
    PHOSPHORUS(15, "P", 2.1, AtomType.NON_METAL, 5, 16, Color.ORANGE, new int[]{3, 5}),
    SULFUR(16, "S", 2.5, AtomType.NON_METAL, 6, 16, Color.YELLOW, new int[]{6, 2}),
    CHLORINE(17, "Cl", 3.0, AtomType.NON_METAL, 7, 18, Color.GREEN, new int[]{1, 7}),
    ARGON(18, "Ar", 0, AtomType.NOBLE_GAS, 8, 22, Atom.Color.CYAN, new int[]{0}),
    POTASSIUM(19, "K", 0.8, AtomType.ALKALI_METAL, 1, 20, Color.VIOLET, new int[]{1}),
    CALCIUM(20, "Ca", 1.0, AtomType.ALKALINE_EARTH_METAL, 2, 20, Color.DARK_GREEN, new int[]{2}),
    SCANDIUM(21, "Sc", 1.36, AtomType.TRANSITION_METAL, 2, 24, Color.GRAY, new int[]{3}),
    TITANIUM(22, "Ti", 1.54, AtomType.TRANSITION_METAL, 2, 26, Color.GRAY, new int[]{4}), // TODO CHECK
    VANADIUM(23, "V", 1.63, AtomType.TRANSITION_METAL, 2, 28, Color.GRAY, new int[]{1}), // TODO CHECK
    CHROMIUM(24, "Cr", 1.66, AtomType.TRANSITION_METAL, 2, 28, Color.GRAY, new int[]{6}), // TODO CHECK ?
    MANGANESE(25, "Mn", 1.55, AtomType.TRANSITION_METAL, 2, 30, Color.GRAY, new int[]{1}), // TODO CHECK
    IRON(26, "Fe", 1.8, AtomType.TRANSITION_METAL, 2, 30, Color.GRAY, new int[]{2, 3}),
    COBALT(27, "Co", 1.88, AtomType.TRANSITION_METAL, 2, 32, Color.GRAY, new int[]{1}), // TODO CHECK
    NICKEL(28, "Ni", 1.91, AtomType.TRANSITION_METAL, 2, 31, Color.GRAY, new int[]{1}), // TODO CHECK
    COPPER(29, "Cu", 1.9, AtomType.TRANSITION_METAL, 1, 34, Color.ORANGE, new int[]{1}), // TODO CHECK
    ZINC(30, "Zn", 1.6, AtomType.TRANSITION_METAL, 2, 34, Color.GRAY, new int[]{1}), // TODO CHECK
    GALLIUM(31, "Ga", 1.81, AtomType.POST_TRANSITION_METAL, 3, 39, Color.GRAY, new int[]{3}),
    GERMANIUM(32, "Ge", 2.01, AtomType.POST_TRANSITION_METAL, 4, 41, Color.GRAY, new int[]{4, 2}),
    ARSENIC(33, "As", 2.18, AtomType.METALLOID, 5, 42, Color.GRAY, new int[]{3, 5}),
    SELENIUM(34, "Se", 2.55, AtomType.NON_METAL, 6, 45, Color.GRAY, new int[]{6, 2}),
    BROMINE(35, "Br", 2.8, AtomType.NON_METAL, 7, 44, Color.GREEN, new int[]{1, 7}), // TODO CHECK
    KRYPTON(36, "Kr", 0, AtomType.NOBLE_GAS, 8, 48, Atom.Color.CYAN, new int[]{0}),
    RUBIDIUM(37, "Rb", 0.82, AtomType.ALKALI_METAL, 1, 48, Color.VIOLET, new int[]{1}),
    STRONTIUM(38, "Sr", 0.59, AtomType.ALKALINE_EARTH_METAL, 2, 50, Color.DARK_GREEN, new int[]{2}),
    YTTRIUM(39, "Y", 1.22, AtomType.TRANSITION_METAL, 2, 50, Color.GRAY, new int[]{3}),
    ZIRCONIUM(40, "Zr", 1.33, AtomType.TRANSITION_METAL, 2, 51, Color.GRAY, new int[]{1}), // TODO CHECK
    NIOBIUM(41, "Nb", 1.6, AtomType.TRANSITION_METAL, 1, 52, Color.GRAY, new int[]{1}), // TODO CHECK
    MOLYBDENUM(42, "Mo", 2.16, AtomType.TRANSITION_METAL, 1, 54, Color.GRAY, new int[]{1}), // TODO CHECK
    TECHNETIUM(43, "Tc", 1.9, AtomType.TRANSITION_METAL, 2, 55, Color.GRAY, new int[]{1}), // TODO CHECK
    RUTHENIUM(44, "Ru", 2.2, AtomType.TRANSITION_METAL, 2, 57, Color.GRAY, new int[]{1}), // TODO CHECK
    RHODIUM(45, "Rh", 2.28, AtomType.TRANSITION_METAL, 1, 58, Color.GRAY, new int[]{1}), // TODO CHECK
    PALLADIUM(46, "Pd", 2.2, AtomType.TRANSITION_METAL, 2, 60, Color.GRAY, new int[]{1}), // TODO CHECK
    SILVER(47, "Ag", 1.9, AtomType.TRANSITION_METAL, 1, 60, Color.GRAY, new int[]{1}),
    CADMIUM(48, "Cd", 1.7, AtomType.TRANSITION_METAL, 2, 66, Color.BEIGE, new int[]{1}), // TODO CHECK
    INDIUM(49, "In", 1.78, AtomType.POST_TRANSITION_METAL, 3, 66, Color.GRAY, new int[]{3}),
    TIN(50, "Sn", 1.8, AtomType.POST_TRANSITION_METAL, 4, 70, Color.GRAY, new int[]{4}),
    ANTIMONY(51, "Sb", 2.05, AtomType.METALLOID, 5, 74, Color.GRAY, new int[]{3, 5}),
    TELLURIUM(52, "Te", 2.1, AtomType.METALLOID, 6, 75, Color.GRAY, new int[]{6, 2}),
    IODINE(53, "I", 2.5, AtomType.NON_METAL, 7, 74, Color.GREEN, new int[]{1, 7}),
    XENON(54, "Xe", 0, AtomType.NOBLE_GAS, 8, 77, Atom.Color.CYAN, new int[]{0}),
    CAESIUM(55, "Cs", 0.79, AtomType.ALKALI_METAL, 1, 78, Color.VIOLET, new int[]{1}),
    BARIUM(56, "Ba", 0.89, AtomType.ALKALINE_EARTH_METAL, 2, 81, Color.DARK_GREEN, new int[]{2}),
    LANTHANUM(57, "La", 1.1, AtomType.LANTHANOID, 3, 81, Color.GRAY, new int[]{3}),
    CERIUM(58, "Ce", 1.12, AtomType.LANTHANOID, 3, 82, Color.GRAY, new int[]{1}), // TODO CHECK
    PRASEODYMIUM(59, "Pr", 1.13, AtomType.LANTHANOID, 3, 82, Color.GRAY, new int[]{1}), // TODO CHECK
    NEODYMIUM(60, "Nd", 1.14, AtomType.LANTHANOID, 3, 84, Color.GRAY, new int[]{1}), // TODO CHECK
    PROMETHIUM(61, "Pm", 1.13, AtomType.LANTHANOID, 3, 84, Color.GRAY, new int[]{1}), // TODO CHECK
    SAMARIUM(62, "Sm", 1.17, AtomType.LANTHANOID, 3, 88, Color.GRAY, new int[]{1}), // TODO CHECK
    EUROPIUM(63, "Eu", 1.2, AtomType.LANTHANOID, 3, 89, Color.GRAY, new int[]{1}), // TODO CHECK
    GADOLINIUM(64, "Gd", 1.2, AtomType.LANTHANOID, 3, 93, Color.GRAY, new int[]{1}), // TODO CHECK
    TERBIUM(65, "Tb", 1.2, AtomType.LANTHANOID, 3, 94, Color.GRAY, new int[]{1}), // TODO CHECK
    DYSPROSIUM(66, "Dy", 1.22, AtomType.LANTHANOID, 3, 97, Color.GRAY, new int[]{1}), // TODO CHECK
    HOLMIUM(67, "Ho", 1.23, AtomType.LANTHANOID, 3, 98, Color.GRAY, new int[]{1}), // TODO CHECK
    ERBIUM(68, "Er", 1.24, AtomType.LANTHANOID, 3, 99, Color.GRAY, new int[]{1}), // TODO CHECK
    THULIUM(69, "Tm", 1.25, AtomType.LANTHANOID, 3, 100, Color.GRAY, new int[]{1}), // TODO CHECK
    YTTERBIUM(70, "Yb", 1.1, AtomType.LANTHANOID, 3, 103, Color.GRAY, new int[]{1}), // TODO CHECK
    LUTETIUM(71, "Lu", 1.27, AtomType.LANTHANOID, 3, 104, Color.GRAY, new int[]{1}), // TODO CHECK
    HAFNIUM(72, "Hf", 1.3, AtomType.TRANSITION_METAL, 2, 106, Color.GRAY, new int[]{1}), // TODO CHECK
    TANTALUM(73, "Ta", 1.5, AtomType.TRANSITION_METAL, 2, 108, Color.GRAY, new int[]{1}), // TODO CHECK
    TUNGSTEN(74, "W", 2.36, AtomType.TRANSITION_METAL, 2, 110, Atom.Color.CYAN, new int[]{1}), // TODO CHECK
    RHENIUM(75, "Re", 1.9, AtomType.TRANSITION_METAL, 2, 111, Color.GRAY, new int[]{1}), // TODO CHECK
    OSMIUM(76, "Os", 2.2, AtomType.TRANSITION_METAL, 2, 114, Color.GRAY, new int[]{1}), // TODO CHECK
    IRIDIUM(77, "Ir", 2.2, AtomType.TRANSITION_METAL, 2, 115, Color.GRAY, new int[]{1}), // TODO CHECK
    PLATINUM(78, "Pt", 2.28, AtomType.TRANSITION_METAL, 2, 117, Color.GRAY, new int[]{1}), // TODO CHECK
    GOLD(79, "Au", 2.4, AtomType.TRANSITION_METAL, 1, 118, Color.YELLOW, new int[]{1}), // TODO CHECK
    MERCURY(80, "Hg", 2.0, AtomType.TRANSITION_METAL, 2, 121, Color.GRAY, new int[]{1}), // TODO CHECK
    THALLIUM(81, "Tl", 1.62, AtomType.POST_TRANSITION_METAL, 3, 123, Color.GRAY, new int[]{1}), // TODO CHECK
    LEAD(82, "Pb", 1.8, AtomType.POST_TRANSITION_METAL, 4, 126, Color.GRAY, new int[]{1}), // TODO CHECK
    BISMUTH(83, "Bi", 1.9, AtomType.POST_TRANSITION_METAL, 5, 126, Color.GRAY, new int[]{1}), // TODO CHECK
    POLONIUM(84, "Po", 2.0, AtomType.POST_TRANSITION_METAL, 6, 125, Color.GRAY, new int[]{1}), // TODO CHECK
    ASTATINE(85, "At", 2.2, AtomType.METALLOID, 7, 125, Color.GRAY, new int[]{1}), // TODO CHECK
    RADON(86, "Rn", 0, AtomType.NOBLE_GAS, 8, 136, Atom.Color.CYAN, new int[]{0}),
    FRANCIUM(87, "Fr", 0.7, AtomType.ALKALI_METAL, 1, 136, Color.VIOLET, new int[]{1}),
    RADIUM(88, "Ra", 0.9, AtomType.ALKALINE_EARTH_METAL, 2, 138, Color.DARK_GREEN, new int[]{2}),
    ACTINIUM(89, "Ac", 1.1, AtomType.ACTINOID, 3, 138, Color.GRAY, new int[]{3}), // TODO CHECK ?
    THORIUM(90, "Th", 1.3, AtomType.ACTINOID, 4, 142, Color.GRAY, new int[]{1}), // TODO CHECK
    PROTACTINIUM(91, "Pa", 1.5, AtomType.ACTINOID, 5, 140, Color.GRAY, new int[]{1}), // TODO CHECK
    URANIUM(92, "U", 1.7, AtomType.ACTINOID, 6, 146, Color.PINK, new int[]{1}), // TODO CHECK
    NEPTUNIUM(93, "Np", 1.36, AtomType.ACTINOID, 7, 144, Color.GRAY, new int[]{1}), // TODO CHECK
    PLUTONIUM(94, "Pu", 1.28, AtomType.ACTINOID, 8, 150, Color.GRAY, new int[]{1}), // TODO CHECK
    AMERICIUM(95, "Am", 1.3, AtomType.ACTINOID, 8, 148, Color.GRAY, new int[]{1}), // TODO CHECK
    CURIUM(96, "Cm", 0, AtomType.ACTINOID, 8, 151, Color.GRAY, new int[]{1}), // TODO CHECK
    BERKELIUM(97, "Bk", 0, AtomType.ACTINOID, 8, 150, Color.GRAY, new int[]{1}), // TODO CHECK
    CALIFORNIUM(98, "Cf", 0, AtomType.ACTINOID, 8, 153, Color.GRAY, new int[]{1}), // TODO CHECK
    EINSTEINIUM(99, "Es", 0, AtomType.ACTINOID, 8, 153, Color.GRAY, new int[]{1}), // TODO CHECK
    FERMIUM(100, "Fm", 0, AtomType.ACTINOID, 8, 157, Color.GRAY, new int[]{1}), // TODO CHECK
    MENDELEVIUM(101, "Md", 0, AtomType.ACTINOID, 8, 157, Color.GRAY, new int[]{1}), // TODO CHECK
    NOBELIUM(102, "No", 0, AtomType.ACTINOID, 8, 157, Color.GRAY, new int[]{1}), // TODO CHECK
    LAWRENCIUM(103, "Lr", 0, AtomType.ACTINOID, 8, 163, Color.GRAY, new int[]{1}), // TODO CHECK
    RUTHERFORDIUM(104, "Rf", 0, AtomType.TRANSITION_METAL, 4, 163, Color.GRAY, new int[]{1}), // TODO CHECK
    DUBNIUM(105, "Db", 0, AtomType.TRANSITION_METAL, 5, 163, Color.GRAY, new int[]{1}), // TODO CHECK
    SEABORGIUM(106, "Sg", 0, AtomType.TRANSITION_METAL, 6, 163, Color.GRAY, new int[]{1}), // TODO CHECK
    BOHRIUM(107, "Bh", 0, AtomType.TRANSITION_METAL, 7, 163, Color.GRAY, new int[]{1}), // TODO CHECK
    HASSIUM(108, "Hs", 0, AtomType.TRANSITION_METAL, 8, 169, Color.GRAY, new int[]{1}), // TODO CHECK
    MEITNERIUM(109, "Mt", 0, AtomType.UNKNOWN, 0, 169, Color.GRAY, new int[]{1}), // TODO CHECK
    DARMSTADTIUM(110, "Ds", 0, AtomType.UNKNOWN, 0, 171, Color.GRAY, new int[]{1}), // TODO CHECK
    ROENTGENIUM(111, "Rg", 0, AtomType.UNKNOWN, 0, 171, Color.GRAY, new int[]{1}), // TODO CHECK
    COPERNICIUM(112, "Cn", 0, AtomType.UNKNOWN, 0, 172, Color.GRAY, new int[]{1}), // TODO CHECK
    NIHONIUM(113, "Nh", 0, AtomType.UNKNOWN, 0, 173, Color.GRAY, new int[]{1}), // TODO CHECK
    FLEROVIUM(114, "Fl", 0, AtomType.POST_TRANSITION_METAL, 0, 174, Color.GRAY, new int[]{1}), // TODO CHECK
    MOSCOVIUM(115, "Mc", 0, AtomType.UNKNOWN, 0, 175, Color.GRAY, new int[]{1}), // TODO CHECK
    LIVERMORIUM(116, "Lv", 0, AtomType.UNKNOWN, 0, 177, Color.GRAY, new int[]{1}), // TODO CHECK
    TENNESSINE(117, "Ts", 0, AtomType.METALLOID, 0, 177, Color.GRAY, new int[]{1}), // TODO CHECK
    OGANESSON(118, "Og", 0, AtomType.NOBLE_GAS, 0, 176, Color.GRAY, new int[]{0});

    private static class Color {
        public static final int WHITE = 0xFFFFFF;
        public static final int BLACK = 0x121215;
        public static final int CYAN = 0x0091D4;
        public static final int VIOLET = 0x5700A3;
        public static final int DARK_GREEN = 0x113900;
        public static final int BEIGE = 0x7B6B46;
        public static final int BLUE = 0x00079D;
        public static final int RED = 0x910000;
        public static final int GREEN = 0x518141;
        public static final int GRAY = 0x4A4A50;
        public static final int ORANGE = 0x8E5A0D;
        public static final int YELLOW = 0xADA10F;
        public static final int PINK = 0x9B4D7A;
    }

    private static final Atom[] ATOM_BY_PROTONS = createAtomProtonIndex();
    private static final Map<String, Atom> ATOM_BY_SYMBOL = createAtomSymbolIndex();

    // set in constructor
    private final int atomNumber;
    private final String symbol;
    private final double electronegativity;
    private final AtomType type;
    private final int initialValenceElectrons;
    private final int initialNeutrons;

    private final int[] bonds;
    private final int color;

    // set when item is created
    private Item item;

    public enum AtomType {
        ALKALI_METAL,
        ALKALINE_EARTH_METAL,
        LANTHANOID,
        ACTINOID,
        TRANSITION_METAL,
        POST_TRANSITION_METAL,
        NON_METAL,
        UNKNOWN,
        METALLOID,
        NOBLE_GAS
    }

    Atom(int p, String symbol, double en, AtomType type, int ve, int n, int color, int[] bonds) {
        this.atomNumber = p;
        this.symbol = symbol;
        this.electronegativity = en;
        this.type = type;
        this.initialValenceElectrons = ve;
        this.initialNeutrons = n;
        this.color = color;
        this.bonds = bonds;
    }

    private static Atom[] createAtomProtonIndex() {
        Atom[] array = new Atom[OGANESSON.getAtomNumber() + 1];
        for (Atom atom : values())
            array[atom.getAtomNumber()] = atom;
        return array;
    }

    private static Map<String, Atom> createAtomSymbolIndex() {
        return Arrays.stream(values()).collect(Collectors.toMap(Atom::getSymbol, a -> a));
    }

    @Nullable
    public static Atom getBySymbol(String symbol) {
        return ATOM_BY_SYMBOL.getOrDefault(symbol, null);
    }

    @Nullable
    public static Atom getByNumber(int atomNumber) {
        if (atomNumber < 1 || atomNumber >= ATOM_BY_PROTONS.length)
            return null;
        return ATOM_BY_PROTONS[atomNumber];
    }

    public int getAtomNumber() {
        return atomNumber;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return Text.translatable(getItem().getTranslationKey()).getString();
    }

    public double getElectronegativity() {
        return electronegativity;
    }

    public AtomType getType() {
        return type;
    }

    public int getInitialValenceElectrons() {
        return initialValenceElectrons;
    }

    public int getInitialNeutrons() {
        return initialNeutrons;
    }

    public Identifier getItemId() {
        return new Identifier(Minelabs.MOD_ID, "atom/" + name().toLowerCase() + "_atom");
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getMaxPossibleBonds() {
        if (this == SULFUR || this == SELENIUM) {
            return 6;
        }
        if (type == AtomType.NOBLE_GAS)
            return 0;
        else
            return Math.min(initialValenceElectrons, 8 - initialValenceElectrons);
    }

    public int[] getBonds(){
        return bonds;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public int getColor() {
        return this.color;
    }

}
