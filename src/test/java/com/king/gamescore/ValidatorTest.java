package com.king.gamescore;

import com.king.gamescore.util.Validator;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.mockito.Mockito;

public class ValidatorTest {

    @Test
    public void testIsUnsignedInteger31BitTrue() {
        assertTrue(Validator.isUnsignedInteger31Bit(String.valueOf(Integer.MAX_VALUE)));
        assertTrue(Validator.isUnsignedInteger31Bit(String.valueOf(0)));
    }

    @Test
    public void testIsUnsignedInteger31BitFalse() {
        assertFalse(Validator.isUnsignedInteger31Bit(String.valueOf(Integer.MAX_VALUE + 1)));
        assertFalse(Validator.isUnsignedInteger31Bit(String.valueOf(-1)));
    }

    @Test
    public void testIsUnsignedInteger31BitValueNotANumber() {
        assertFalse(Validator.isUnsignedInteger31Bit(Mockito.any(String.class)));
    }
}
