package com.games.astar;

import static org.junit.Assert.*;
import org.junit.Test;
import com.games.astar.Tile;

public class TileTest {

    @Test
    public void testGetNeighbours () {


        // create a 10x10 walkable map
        Tile[][] map = new Tile[10][10];
        for ( int y = 0; y < 10; y++ ){
            for ( int x = 0; x < 10; x++ ){
                map[y][x] = new Tile ( x, y, false );
            }
        }

        // test 1, top left corner
        runNeighbourTest(
            "Neighbours of a top left tile",
            map,
            new Tile( 0, 0 ),
            new int[][]{ { 1, 0 }, { 1, 1 }, { 0, 1 } }
        );

        // test 2, top right corner
        runNeighbourTest(
            "Neighbours of a top right tile",
            map,
            new Tile( 9, 0 ),
            new int[][]{ { 8, 0 }, { 8, 1 }, { 9, 1 } }
        );
    }


    /* runNeighboursTest

        Convenience method for running a test and for comparing array based outcomes
        Basically saving wear and tear on my basic microsoft keyboard

        @param String - a description for the test
        @param Tile[][] - the map to extract neighbours from
        @param tile - the tile to be tested
        @param int[][] - a 2D array of points which we expect to be comparable to the
            output of getNeighbours, made up of int arrays of int X int Y

    */

    public void runNeighbourTest( String description, Tile[][] map, Tile tile, int[][] n ){

        Tile expected[] = new Tile[n.length];
        for ( int i = 0; i < expected.length; i++ ){
            expected[i] = new Tile( n[i][0], n[i][1] );
        }

        assertArrayEquals( description, expected, tile.getNeighbours( map ) );
    }
}
