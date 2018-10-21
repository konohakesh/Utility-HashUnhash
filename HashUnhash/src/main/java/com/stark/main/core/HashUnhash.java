package com.stark.main.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This API can encrypt and then decrypt the content of a file
 */
public class HashUnhash
{
    String filePath = null;

    boolean isCloseOption, isOpenOption;

    public static void main( String[] args )
    {
        HashUnhash main = new HashUnhash();

        try
        {
            main.hashUnHash( args );
        }
        catch ( ClassNotFoundException | IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Method that actually hashes or unhashes
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void hashUnHash( String[] args ) throws IOException, ClassNotFoundException
    {
        if ( validate( args ) != null )
            return;

        Path path = Paths.get( filePath );

        if ( isCloseOption )
        {
            hashFile( path );
        }
        else if ( isOpenOption )
        {
            unhashFile( path );
        }
    }

    /**
     * Validates the arguments
     * @param arguments
     * @return
     */
    public String validate( String[] arguments )
    {
        if ( arguments.length == 0 )
        {
            return "Enter path of the file as an argument along with either of the actions - Open or Close with latter being optional.";
        }
        else if ( arguments.length > 2 )
        {
            return "Way more arguments than expected.";
        }

        filePath = arguments[0];

        Path path = Paths.get( filePath );
        if ( !Files.exists( path, LinkOption.NOFOLLOW_LINKS ) )
        {
            return "File does not exist at: " + filePath;
        }

        if ( arguments.length == 1 || arguments[1].equalsIgnoreCase( "Close" ) )
        {
            isCloseOption = true;
        }
        else if ( arguments[1].equalsIgnoreCase( "Open" ) )
        {
            isOpenOption = true;
        }

        return null;
    }

    /**
     * Reading the encrypted lines from the file and writing back their unencrypted versions
     * @param path Files which is to be unencrypted
     * @throws ClassNotFoundException
     */
    public static void unhashFile( Path path ) throws IOException, ClassNotFoundException
    {
        List<Byte> bytesList = new ArrayList<Byte>();

        try (ObjectInputStream stream = new ObjectInputStream( Files.newInputStream( path ) ))
        {
            // reading the object from the file and converting them into bytes as we know it were bytes that were encrypted
            Byte b = (Byte) stream.readObject();
            while ( b != null )
            {
                b = (byte) ( b - 1 );
                bytesList.add( b );
                b = (Byte) stream.readObject();
            }
        }
        catch ( EOFException e )
        {
            // Writing back the bytesList to the file
            byte[] bytesArray = new byte[bytesList.size()];

            int i = 0;
            for ( byte b : bytesList )
            {
                bytesArray[i] = b;
                i++;
            }

            Files.write( path, bytesArray );
        }
    }

    /**
     * Reading the lines from the file and writing back their encrypted versions
     * @param path Files which is to be encrypted
     * @throws IOException
     */
    public static void hashFile( Path path ) throws IOException
    {
        // reading each line in the form of bytes
        byte[] allLines = Files.readAllBytes( path );

        try (ObjectOutputStream stream = new ObjectOutputStream( Files.newOutputStream( path ) ))
        {
            // writing the objects of bytes post some processing (secret to encryption)
            for ( byte b : allLines )
            {
                b = (byte) ( b + 1 );
                stream.writeObject( b );
            }
        }
    }
}
