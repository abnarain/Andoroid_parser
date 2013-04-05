package dkohl.gatech.histogram;

public class Hist {

    private static final String classes[] = {
	"NOERR",
	"CORERR",
	"INCERR"
    };
    
    private static CombinedHistorgam histogram_singleton;
    
    public static CombinedHistorgam hist() {
	if(histogram_singleton == null) {
	    histogram_singleton = new CombinedHistorgam(classes);
	}
	return histogram_singleton;
    }
    
}
