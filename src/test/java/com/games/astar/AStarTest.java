package github.patmooney.astar;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import github.patmooney.astar.Tile;
import github.patmooney.astar.AStar;
import java.util.Arrays;

public class AStarTest {

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
    public void basic () {
        Tile[] path = AStar.buildPath( this.map, this.map[0][0], this.map[6][4] );

        assertArrayEquals(
            "basic none blocked path",
            buildExpected(
                new int[][]{ { 0, 0 }, { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 4, 5 }, { 4, 6 }  }
            ),
            path
        );

    }

    @Test
    public void complexPath () {
        this.map = this.makeMap(
            new int[][] {
                { 1, 1, 1, 1, 1, 1, 1, 0 },
                { 0, 1, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 1, 0, 0, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 1, 1, 0 },
                { 1, 1, 0, 1, 0, 0, 1, 0 },
                { 0, 0, 0, 1, 0, 0, 1, 0 },
                { 0, 1, 1, 0, 1, 0, 1, 0 },
                { 0, 1, 0, 0, 0, 0, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 1, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1 }
            }
        );
        Tile[] path = AStar.buildPath( this.map, this.map[1][0], this.map[8][7] );

        assertArrayEquals(
            "complex map path",
            buildExpected(
                new int[][]{
                    { 0, 1 }, { 0, 2 }, { 1, 3 }, { 2, 4 }, { 1, 5 }, { 0, 6 }, { 0, 7 }, { 1, 8 }, { 2, 8 }, { 3, 8 }, { 4, 7 }, { 5, 6 },
                    { 5, 5 }, { 5, 4 }, { 4, 3 }, { 5, 2 }, { 6, 1 }, { 7, 2 }, { 7, 3 }, { 7, 4 }, { 7, 5 }, { 7, 6 }, { 7, 7 }, { 7, 8 }
                }
            ),
            path
        );
    }

    @Test
    public void impossiblePath () {
        this.map = this.makeMap(
            new int[][] {
                { 0, 0, 0, 0, 1 },
                { 0, 0, 0, 1, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 1, 0, 0, 0 },
                { 1, 0, 0, 0, 0 }
            }
        );

        Tile[] path = AStar.buildPath( this.map, this.map[0][0], this.map[1][4] );

        assertNull( "unable to reach target tile", path );
    }


    public void diagPath( Tile[] path ) {

        String[][] clone = new String[this.map.length][this.map[0].length];
        for ( int y = 0; y < this.map.length; y++ ){
            for ( int x = 0; x < this.map[y].length; x++ ){
                String v = this.map[y][x].solid ? "x" : "0";
                clone[y][x] = v;
            }
        }

        int w = 1;
        for ( Tile p : path ){
            clone[p.y][p.x] = "" + w++;
        }

        for ( int y = 0; y < this.map.length; y++ ){
            for ( int x = 0; x < this.map[y].length; x++ ){
                String spaces = "  ";
                if ( clone[y][x] != "x" && Integer.valueOf(clone[y][x]) > 9 ){
                    spaces = " ";
                }
                System.out.print( clone[y][x] + spaces );
            }
            System.out.println();
        }

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
