package com.games.astar;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;
import com.games.astar.Tile;

public class TileTest {

    private static Tile[][] map;

    @BeforeClass
    public static void init () {

        // create a 10x10 walkable map
        Tile[][] map = new Tile[10][10];
        for ( int y = 0; y < 10; y++ ){
            for ( int x = 0; x < 10; x++ ){
                map[y][x] = new Tile ( x, y, false );
            }
        }

        TileTest.map = map;
    }


    @Test
    public void topLeftCorner () {
        // test 1, top left corner
        assertArrayEquals(
            "Neighbours of a top left tile",
            buildExpected(
                new int[][]{ { 1, 0 }, { 1, 1 }, { 0, 1 } }
            ),
            (new Tile( 0, 0 )).getNeighbours( this.map )
        );
    }

    @Test
    public void topRightCorner () {
        // test 2, top right corner
        assertArrayEquals(
            "Neighbours of a top right tile",
            buildExpected(
                new int[][]{ { 8, 0 }, { 8, 1 }, { 9, 1 } }
            ),
            (new Tile( 9, 0 )).getNeighbours( this.map )
        );
    }

    @Test
    public void bottomLeftCorner () {
        // test 3, bottom left corner
        assertArrayEquals(
            "Neighbours of a bottom left tile",
            buildExpected(
                new int[][]{ { 1, 9 }, { 1, 8 }, { 0, 8 } }
            ),
            (new Tile( 0, 9 )).getNeighbours( this.map )
        );
    }

    @Test
    public void bottomRighttCorner () {
        // test 4, bottom right corner
        assertArrayEquals(
            "Neighbours of a bottom right tile",
            buildExpected(
                new int[][]{ { 8, 9 }, { 8, 8 }, { 9, 8 } }
            ),
            (new Tile( 9, 9 )).getNeighbours( this.map )
        );
    }

    @Test
    public void leftEdge () {
        // test 5, left edge
        assertArrayEquals(
            "Neighbours of a left edge tile",
            buildExpected(
                new int[][]{ { 1, 4 }, { 1, 3 }, { 1, 5 }, { 0, 3 }, { 0, 5 } }
            ),
            (new Tile( 0, 4 )).getNeighbours( this.map )
        );
    }

    @Test
    public void rightEdge () {
        // test 6, right edge
        assertArrayEquals(
            "Neighbours of a right edge tile",
            buildExpected(
                new int[][]{ { 8, 4 }, { 8, 3 }, { 8, 5 }, { 9, 3 }, { 9, 5 } }
            ),
            (new Tile( 9, 4 )).getNeighbours( this.map )
        );
    }

    @Test
    public void topEdge () {
        // test 7, top edge
        assertArrayEquals(
            "Neighbours of a top edge tile",
            buildExpected(
                new int[][]{ { 3, 0 }, { 3, 1 }, { 5, 0 }, { 5, 1 }, { 4, 1 } }
            ),
            (new Tile( 4, 0 )).getNeighbours( this.map )
        );
    }

    @Test
    public void bottomEdge () {
        // test 8, bottom edge
        assertArrayEquals(
            "Neighbours of a bottom edge tile",
            buildExpected( 
                new int[][]{ { 3, 9 }, { 3, 8 }, { 5, 9 }, { 5, 8 }, { 4, 8 } }
            ),
            (new Tile( 4, 9 )).getNeighbours( this.map )
        );
    }

    @Test
    public void middle () {
        // test 8, middle
        assertArrayEquals(
            "Neighbours of a middle tile",
            buildExpected(
                new int[][]{ { 3, 4 }, { 3, 3 }, { 3, 5 }, { 5, 4 }, { 5, 3 }, { 5, 5 }, { 4, 3 }, { 4, 5 } }
            ),
            (new Tile( 4, 4 )).getNeighbours( this.map )
        );
    }

    @Test
    public void middleSolids () {

        // test 9, middle - all solid
        this.map[4][3].solid = true;
        this.map[3][3].solid = true;
        this.map[5][3].solid = true;
        this.map[4][5].solid = true;
        this.map[3][5].solid = true;
        this.map[5][5].solid = true;
        this.map[3][4].solid = true;
        this.map[5][4].solid = true;

        assertArrayEquals(
            "Neighbours of a middle tile",
            buildExpected(
                new int[][]{}
            ),
            (new Tile( 4, 4 )).getNeighbours( this.map )
        );

        // test 10, middle - solid sanity test
        this.map[4][3].solid = false;
        this.map[4][5].solid = false;
        this.map[3][4].solid = false;
        this.map[5][4].solid = false;
        assertArrayEquals(
            "Neighbours of a middle tile",
            buildExpected(
                new int[][]{ { 3, 4 }, { 5, 4 }, { 4, 3 }, { 4, 5 } }
            ),
            (new Tile( 4, 4 )).getNeighbours( this.map )
        );

    }

    public static Tile[] buildExpected( int[][] n ){

        Tile expected[] = new Tile[n.length];
        for ( int i = 0; i < expected.length; i++ ){
            expected[i] = new Tile( n[i][0], n[i][1] );
        }
        
        return expected;
    }
}
