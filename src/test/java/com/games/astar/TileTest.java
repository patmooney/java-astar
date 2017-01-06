package com.games.astar;

import static org.junit.Assert.*;
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

        Tile t = new Tile( 0, 0 );
        Tile[] neighbours = t.getNeighbours( map );

        assertEquals( neighbours.length, 3 );
    }
}
