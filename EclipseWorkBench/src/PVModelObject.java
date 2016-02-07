
public class PVModelObject {
    public String modelName;
    public double sysCapacity; //this is in KW
    public double percentLost;
    public int moduleType;
    
    public PVModelObject(String modelName, double sysCapacity, 
                            double percentLost, int moduleType) {
        this.modelName = modelName;
        this.sysCapacity = sysCapacity;
        this.percentLost = percentLost;
        this.moduleType = moduleType;
    }
}