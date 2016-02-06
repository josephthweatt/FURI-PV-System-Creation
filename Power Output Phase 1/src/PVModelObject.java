public class PVModelObject {
    public static String modelName;
    public static double sysCapacity; //this is in KW
    public static double percentLost;
    public static int moduleType;
    
    public PVModelObject(String modelName, double sysCapacity, 
                            double percentLost, int moduleType) {
        this.modelName = modelName;
        this.sysCapacity = sysCapacity;
        this.percentLost = percentLost;
        this.moduleType = moduleType;
    }
}