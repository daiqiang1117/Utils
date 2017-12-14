package com.amazon.skylightdesktopmanagerregional.validator;

import com.amazon.skylightdesktopmanagerregional.generator.RangeCreationFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiandai on 12/14/17.
 */

public class RangeValidator
{
    private final static String RANGE_SET_DELIMITER = ",";
    private final static String RANGE_DELIMITER = "\\.\\.";

    private final static List<Character> BOUND_SIGN_LIST = new ArrayList<Character>(){{
        add( RangeCreationFactory.CLOSED_LEFT );
        add( RangeCreationFactory.CLOSED_RIGHT );
        add( RangeCreationFactory.OPEN_LEFT ) ;
        add( RangeCreationFactory.OPEN_RIGHT ) ;
    }};


    private final Log LOG = LogFactory.getLog( WorkspaceVolumeValidator.class );
    
    /**
     * Retrieve guava range set from string
     *
     * Example string = "[10..10],[50..50],[100..1000]"
     * Format = add comma to google guava range set string
     * Use [] for inclusive and () for exclusive settings
     *
     * INCLUSIVE EXAMPLES: [<LOWER1>..<UPPER1>]
     *                     [<LOWER1>..<UPPER1>], [<LOWER2>..<UPPER2>]
     * EXCLUSIVE EXAMPLES: (<LOWER1>..<UPPER1>)
     *                     (<LOWER1>..<UPPER1>), (<LOWER2>..<UPPER2>)
     * COMBINE EXAMPLES: [<LOWER1>..<UPPER1>)
     *                   (<LOWER1>..<UPPER1>]
     * INVALID EXAMPLES: [10..-1],(9..9)
     *
     * @param rangeSetString
     * @return
     */
    @VisibleForTesting
    RangeSet getRangeSetFromConfigString( String rangeSetString )
    {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        if( StringUtils.isBlank( rangeSetString ) ){
            LOG.error( "User/Root volume range global property configuration is empty." );
            return rangeSet;
        }
        Boolean rangeSetStringFormatValid = Boolean.TRUE;
        for ( final String rangeString : rangeSetString.split( RANGE_SET_DELIMITER ) )
        {
            Range<Integer> range = getRangeFromConfigString( rangeString );
            if ( range != null )
            {
                rangeSet.add( range ) ;
            }
            else
            {
                rangeSetStringFormatValid = Boolean.FALSE;
            }
        }
        if( !rangeSetStringFormatValid )
        {
            LOG.error(
                String.format( "User/Root volume range global property configuration %s is invalid", rangeSetString ) );
        }
        return rangeSet;
    }

    /**
     * Retrieve guava range from string
     *
     * Example string = "[10..10]"
     * Format = google guava range set string format;
     *
     * @param rangeString
     * @return guava range from parsing range string, null for invalid range string,
     */
    private Range<Integer> getRangeFromConfigString ( String rangeString )
    {
        if ( !isValidRangeString( rangeString ) )
        {
            return null;
        }

        String[] splitArray = rangeString.split( RANGE_DELIMITER );
        Integer lowerBound, upperBound;
        try
        {
            lowerBound = Integer.valueOf( splitArray[0].substring( 1 ) );
            upperBound = Integer.valueOf( splitArray[1].substring( 0, splitArray[1].length() - 1 ) );
        }
        catch ( NumberFormatException nfe )
        {
            return null;
        }

        char boundSignLeft = splitArray[0].charAt( 0 );
        char boundSignRight = splitArray[1].charAt( splitArray[1].length() - 1 );

        return RangeCreationFactory.createRange(
            boundSignLeft, lowerBound, boundSignRight, upperBound );
    }

    /**
     * validate range string
     *
     * Example string = "[10..10]"
     * Split it as "[10" and "10]"
     * Check bound signs are parentheses or square brackets
     *
     * @param rangeString
     * @return true for valid range string, false for invalid
     */
    private Boolean isValidRangeString( String rangeString )
    {
        if ( StringUtils.isBlank( rangeString ) )
        {
            return Boolean.FALSE;
        }

        String[] splitArray = rangeString.split( RANGE_DELIMITER );

        if ( splitArray.length != 2 || splitArray[0].length() < 2 || splitArray[1].length() < 2 ||
            !BOUND_SIGN_LIST.contains( splitArray[0].charAt( 0 ) ) ||
            !BOUND_SIGN_LIST.contains( splitArray[1].charAt( splitArray[1].length() - 1 ) ) )
        {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
