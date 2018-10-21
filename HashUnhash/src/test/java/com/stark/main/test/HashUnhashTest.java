package com.stark.main.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.stark.main.core.HashUnhash;

public class HashUnhashTest
{
    HashUnhash hashUnhash = null;

    /**
     * Tests the validation of HashUnhash API
     */
    @Test
    public void testValidate()
    {
        hashUnhash = new HashUnhash();

        String args2[] = new String[] {};
        Assert.assertEquals(
                "Enter path of the file as an argument along with either of the actions - Open or Close with latter being optional.",
                hashUnhash.validate( args2 ) );

        String args3[] = new String[] {"src/test/resources/Sample.txt", "close", "open"};
        Assert.assertEquals( "Way more arguments than expected.", hashUnhash.validate( args3 ) );

        String args4[] = new String[] {"src/test/resources/Sample.txt"};
        Assert.assertNull( hashUnhash.validate( args4 ) );

        String args5[] = new String[] {"src/test/resources/Sample.txt", "close"};
        Assert.assertNull( hashUnhash.validate( args5 ) );

        String args6[] = new String[] {"src/test/resources/Sample.txt", "open"};
        Assert.assertNull( hashUnhash.validate( args6 ) );
    }

    /**
     * Tests Hashing\Unhashing logic of HashUnhash API
     */
    @Test
    public void testHashUnhashFile()
    {
        Path path = Paths.get( "src/test/resources/Sample.txt" );
        try
        {
            HashUnhash.hashFile( path );
            byte[] allBytes = Files.readAllBytes( path );

            Assert.assertEquals( 410, allBytes.length );

            HashUnhash.unhashFile( path );
            List<String> allLines = Files.readAllLines( path );

            Assert.assertEquals( 2, allLines.size() );
            Assert.assertEquals( "And then another sample line with @.", allLines.get( 1 ) );
        }
        catch ( IOException | ClassNotFoundException e )
        {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
