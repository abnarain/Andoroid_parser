package dkohl.gatech.typing.error;

/**
 * Possible errors from
 * the paper
 * 
 * @author Daniel Kohlsdorf
 */
public enum ErrorTypes {
    COR_NO_ERR,
    COR_OMISSION,
    COR_INSERTION,
    COR_SUBS, 
    
    UNC_OMISSION,
    UNC_INSERTION,
    UNC_SUBS,
    UNC_NO_ERR,
    
    OBO_ROLLON,
    OBO_ROLLOFF,
    OBO_NOERR,
    OBO_MULTIPLE,
    
    FT_ROLLON_CONTEXT,
    FT_ROLLON,
    FT_ROLLOFF,
    FT_EQUALDT, 
    FT_NOERR,
    
    WRONG_DET_ROLLON,
    WRONG_DET_ROLLOFF,
    WRONG_DET_EQUALDT,
    
    DET_NOERR,
    DET_ROLLOFF,
    DET_ROLLON,
    DET_EQUALDT,
    
    UNDET_MULTIPLE,
    UNDET_ROLLOFF,
    UNDET_ROLLON,
    UNDET_FIRST_FOUR,
    
    FALSE_TRIGGER,
    ROLLON_AS_ROLLOFF,
    ROLLOFF_AS_ROLLON,
    TOTAL_STROKES, 
    NUM_FILES,
    NUM_UNEQUAL_ALIGNMENTS, 
    
    AVG_WPM,
    AVG_ACC,
    
    FT_EQUALDT_ROLLON, 
    FT_EQUALDT_ROLLOFF, 
    
}
