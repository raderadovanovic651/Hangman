package phantom_hangman;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Prikaz ponudjenih slova slova u glavnom prozoru
 */
public class LetterRack extends JPanel
{
	
    private final int RACK_COLS;
    
    private final int RACK_ROWS;
    
    private final GridLayout LETTER_RACK_LAYOUT;
    
    private final int CAPACITY;

    private final String IMAGE_DIRECTORY;

    private final String IMAGE_TYPE;

    private final String password;

    private final ArrayList<LetterTile> rack; 

    public LetterRack()
    {
        this("password", "images/", ".png");
    }
    
    /**
     * Kreiranje niza slova na osnovu trazene reci za pogadjanje 
     */
    public LetterRack(String inPassword, String imageDirectory, 
            String imageType)
    {
        RACK_COLS = 8;                                            // slova u redu
        RACK_ROWS = 2;                                            // redova
        LETTER_RACK_LAYOUT = new GridLayout(RACK_ROWS, RACK_COLS);
        LETTER_RACK_LAYOUT.setVgap(10);
        CAPACITY = RACK_ROWS * RACK_COLS;
        
        IMAGE_DIRECTORY = imageDirectory;
        IMAGE_TYPE = imageType;
        
        rack = new ArrayList<>();
        password = inPassword;
        
        setBorder(BorderFactory.createEmptyBorder(10, 17, 10, 10));
        setLayout(LETTER_RACK_LAYOUT);
        loadRack();
    }
    
    /**
     * Ucitavnje slova
     */
    private void loadRack()
    {
        buildRack();
        for (LetterTile tile : rack)
            add(tile);
    }
    
    /**
     * Ucitavanje slova na ekren koja ce sadrzati trazenu rec i nasumicna slova do maksimalnog kapaciteta
     */
    private void buildRack()
    {
        StringBuilder passwordBuilder = 
                new StringBuilder(password.toLowerCase());
        ArrayList<Character> tiles = new ArrayList<>(); 
        Random rand = new Random();
        int i = 0, j = 0;
        
        
        // dodavanje trazene reci
        while (passwordBuilder.length() > 0)
        {
            // Da se slova ne bi ponavljala
            if (!tiles.contains(passwordBuilder.charAt(0)))
            {
                tiles.add(passwordBuilder.charAt(0));
                i++;
            }
            passwordBuilder.deleteCharAt(0);
        }
        
        // dodavanje nasumicnih slova da bi se popunio kapacitet
        for (; i < CAPACITY; i++)
        {
        	
            Character c = 'a'; 
            do
            {
            	String  abc ="АБВГДЂЕЖЗИЈКЛЉМНЊОПРСТЋУФХЦЧЏШ";    

                c = abc.charAt(rand.nextInt(abc.length()));
            } while (tiles.contains(c));
            tiles.add(c);
        }
        
        
        for (i = 0; i < CAPACITY; i++)
        {
            j = rand.nextInt(tiles.size());
            rack.add(new LetterTile(tiles.get(j), 
                    IMAGE_DIRECTORY, 
                    IMAGE_TYPE));
            tiles.remove(j);
        }
    }
    
    /**
     * dodaj svakom slovu slusac
     */
    public void attachListeners(MouseListener l)
    {
        for (LetterTile tile : rack)
            tile.addTileListener(l);
    }
    
    /**
     * izbrisi sve slusace
     */
    public void removeListeners()
    {
        for (LetterTile tile : rack)
            tile.removeTileListener();
    }
}