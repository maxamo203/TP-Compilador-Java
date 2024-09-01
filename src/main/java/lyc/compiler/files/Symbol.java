package lyc.compiler.files;

public class Symbol {
    private String name;
    private String dataType;
    private String value;
    private String length;

    public Symbol(String name, String dataType, String value, String length) {
        this.name = name;
        this.dataType = dataType;
        this.value = value;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getValue() {
        return value;
    }

    public String getLength() {
        return length;
    }

    @Override
    public String toString() {
        return name + "|" + dataType + "|" + value + "|" + length +  "\n";
    }
}
