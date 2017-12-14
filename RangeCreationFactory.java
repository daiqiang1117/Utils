package com.amazon.skylightdesktopmanagerregional.generator;

import com.amazon.skylightdesktopmanagerregional.validator.WorkspaceVolumeValidator;
import com.google.common.collect.Range;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RangeCreationFactory
{
    public final static char CLOSED_LEFT  = '[';
    public final static char CLOSED_RIGHT = ']';
    public final static char OPEN_LEFT    = '(';
    public final static char OPEN_RIGHT   = ')';

    private final static Log log = LogFactory.getLog( WorkspaceVolumeValidator.class );

    private RangeCreationFactory()
    {
        throw new RuntimeException( "Class should not be instantiated" );
    }

    public static Range<Integer> createRange( char boundLeftSign, Integer lowerBound, char boundRightSign, Integer upperBound)
    {
        try
        {
            if ( boundLeftSign == CLOSED_LEFT && boundRightSign == CLOSED_RIGHT )
            {
                return Range.closed( lowerBound,  upperBound );
            }
            if ( boundLeftSign == CLOSED_LEFT && boundRightSign == OPEN_RIGHT )
            {
                return Range.closedOpen( lowerBound,  upperBound );
            }
            if ( boundLeftSign == OPEN_LEFT && boundRightSign == CLOSED_RIGHT )
            {
                return Range.openClosed( lowerBound,  upperBound );
            }
            if ( boundLeftSign == OPEN_LEFT && boundRightSign == OPEN_RIGHT )
            {
                return Range.open( lowerBound,  upperBound );
            }
        }
        catch( IllegalArgumentException e )
        {
            log.error( String.format( "Range creation failed for lower bound %d and upper bound %d.",
                lowerBound, upperBound ) );
        }
        return null;
    }
}
