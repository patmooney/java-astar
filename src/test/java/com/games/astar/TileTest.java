package github.patmooney.astar;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import github.patmooney.astar.Tile;

public class TileTest {

    Tile[][] map;

    public Tile[][] blankMap () {
        return this.makeMap(
            new int[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
            }
        );
    }

    @Before
    public void init () {
        this.map = this.blankMap();
    }

    @Test
    public void topLeftCorner () {
        // test 1, top left corner
        assertArrayEquals(
            "Neighbours of a top left tile",
            buildExpected(
                new int[][]{ { 1, 0 }, { 1, 1 }, { 0, 1 } }
            ),
            this.map[0][0].getNeighbours( this.map )
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
            this.map[0][9].getNeighbours( this.map )
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
            this.map[9][0].getNeighbours( this.map )
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
            this.map[9][9].getNeighbours( this.map )
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
            this.map[4][0].getNeighbours( this.map )
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
            this.map[4][9].getNeighbours( this.map )
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
            this.map[0][4].getNeighbours( this.map )
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
            this.map[9][4].getNeighbours( this.map )
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
            this.map[4][4].getNeighbours( this.map )
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
            this.map[4][4].getNeighbours( this.map )
        );

        this.map = this.blankMap();
        this.map[3][3].solid = true;
        this.map[5][3].solid = true;
        this.map[3][5].solid = true;
        this.map[5][5].solid = true;

        // test 10, middle - solid sanity test
        assertArrayEquals(
            "Neighbours of a middle tile",
            buildExpected(
                new int[][]{ { 3, 4 }, { 5, 4 }, { 4, 3 }, { 4, 5 } }
            ),
            this.map[4][4].getNeighbours( this.map )
        );

    }

    public Tile[] buildExpected( int[][] n ){

        Tile expected[] = new Tile[n.length];
        for ( int i = 0; i < expected.length; i++ ){
            expected[i] = this.map[n[i][1]][n[i][0]];
        }

        return expected;
    }

    public Tile[][] makeMap ( int[][] s ) {
        Tile[][] map = new Tile[s.length][s[0].length];

        for ( int y = 0; y < s.length; y++ ){
            for ( int x = 0; x < s[y].length; x++ ){
                boolean solid = s[y][x] == 1 ? true : false;
                map[y][x] = new Tile( x, y, solid );
            }
        }

        return map;
    }
}
