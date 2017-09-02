public class CocolStructure {

    private String letter="a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z";
    private String digit="0|1|2|3|4|5|6|7|8|9";
    private String symbols = "°|!|#|\"|\\|%|/|&|=|¿|?|¡|[|]|-|_|<|>|:|;|~|{|}| ";
    private String any = symbols + "|" + letter + "|" + digit;
    private String anyButQuote;
    private String anyButApostrophe;

    public CocolStructure() {
    }

    public String getLetter() {
        return letter;
    }

    public String getDigit() {
        return digit;
    }

    public String getSymbols() {
        return symbols;
    }

    public String getAny() {
        return any;
    }

    public String getAnyButQuote() {
        anyButQuote=any.replace("|\"", "");
        return anyButQuote;
    }

    public String getAnyButApostrophe() {
        anyButApostrophe=any.replace("|\\", "");
        return anyButApostrophe;
    }
}
