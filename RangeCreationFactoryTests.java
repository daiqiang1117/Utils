package com.amazon.skylightdesktopmanagerregional.generator;

import com.google.common.collect.Range;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class RangeCreationFactoryTests
{
    private static final int LOWER_BOUND = 10;
    private static final int UPPER_BOUND = 100;
    @Test
    public void shouldReturnNullForInvalidArguments()
    {
        int lowerBound = 10;
        int upperBound = 5;
        Range<Integer> range = RangeCreationFactory.createRange(
            RangeCreationFactory.CLOSED_LEFT,
            lowerBound,
            RangeCreationFactory.CLOSED_RIGHT,
            upperBound );
        Assert.assertNull("Range must be null for invalid arguments", range);

        lowerBound = 10;
        upperBound = 100;
        range = RangeCreationFactory.createRange(
            'x',
            lowerBound,
            RangeCreationFactory.CLOSED_RIGHT,
            upperBound );
        assertNull("Range must be null for invalid arguments", range);
    }

    @Test
    public void shouldReturnExpectedRangeForClosedRange()
    {
        Range range = RangeCreationFactory.createRange(
            RangeCreationFactory.CLOSED_LEFT,
            LOWER_BOUND,
            RangeCreationFactory.CLOSED_RIGHT,
            UPPER_BOUND );
        assertEquals("range must equal expected", range, Range.closed( LOWER_BOUND, UPPER_BOUND ));
    }

    @Test
    public void shouldReturnExpectedRangeForOpenRange()
    {
        Range range = RangeCreationFactory.createRange(
            RangeCreationFactory.OPEN_LEFT,
            LOWER_BOUND,
            RangeCreationFactory.OPEN_RIGHT,
            UPPER_BOUND );
        assertEquals("range must equal expected", range, Range.open( LOWER_BOUND, UPPER_BOUND ));
    }

    @Test
    public void shouldReturnExpectedRangeForOpenClosedRange()
    {
        Range range = RangeCreationFactory.createRange(
            RangeCreationFactory.OPEN_LEFT,
            LOWER_BOUND,
            RangeCreationFactory.CLOSED_RIGHT,
            UPPER_BOUND );
        assertEquals("range must equal expected", range, Range.openClosed( LOWER_BOUND, UPPER_BOUND ));
    }

    @Test
    public void shouldReturnExpectedRangeForClosedOpenRange()
    {
        Range range = RangeCreationFactory.createRange(
            RangeCreationFactory.CLOSED_LEFT,
            LOWER_BOUND,
            RangeCreationFactory.OPEN_RIGHT,
            UPPER_BOUND );
        assertEquals("range must equal expected", range, Range.closedOpen( LOWER_BOUND, UPPER_BOUND ));
    }
}
