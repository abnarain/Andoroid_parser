package dkohl.gatech.typing.classification;

public class ClassificationStreamSingleton {

    private static ClassificationStream stream;

    public static void setStream(ClassificationStream stream) {
	ClassificationStreamSingleton.stream = stream;
    }

    public static ClassificationStream getStream() {
	return stream;
    }
    
}
