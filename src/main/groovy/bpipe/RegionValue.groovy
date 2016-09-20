package bpipe

/**
 * A wrapper for either a RegionSet or a Chr that
 * enables them to be used as either string values or to support a ".bed"
 * extension.
 */
class RegionValue implements Serializable {
    
    public static File REGIONS_DIR = new File(".bpipe/regions")
    
    public static final long serialVersionUID = 0L
    
    String value
    
    def propertyMissing(String name) {
       if(name == "bed") {
           
           if(!REGIONS_DIR.exists()) {
               REGIONS_DIR.mkdirs()
           }
           
           def fn = new File(REGIONS_DIR,Utils.sha1(value)+".bed")
           if(!fn.exists()) {
               
               def regions = value.tokenize(" ")
               fn.text = value.replaceAll("-","\t").replaceAll(":","\t").split(" ").join("\n")
           }
           return fn.absolutePath
       } 
    }
    
    String plus(String arg) {
        return this.toString() + arg
    }
    
    def methodMissing(String name, args) {
        // faux inheritance from String class
        if(name in String.metaClass.methods*.name)
            return String.metaClass.invokeMethod(this.toString(), name, args)
        else {
            throw new MissingMethodException(name, RegionValue, args)
        }
    }
    
    String toString() {
        return value   
    }
}
