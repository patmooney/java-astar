package com.games.astar;

import java.util.Arrays;

/* Tile

An extension of java.at.Tile with added attributes
and methods making it more suitable for use with AStar pathfinding

A normal "map" will contain a 2D array of Tiles, the first dimension being
the Y coordinate and secondary being the X coordinate such that the array,
when visuallised, is representive of the physical location of each tile as they
appear within a map

e.g.

  [
    [ X0, X1, X2, X3, X4, X5 ], # Y1
    [ X0, X1, X2, X3, X4, X5 ], # Y2
    ...
  ]
*/

public class Tile extends Tile {
    public boolean solid = false;
    public Tile[] neighbours = null;

    public Tile ( int X, int Y ) {
        super( X, Y );
    }

    public Tile ( int X, int Y, boolean solid ) {
        super( X, Y );
        this.solid = solid;
    }

    /* getNeighbours
        Accepts a Tile[][] 2D array and will return a flat Array of Tile[]
        containing array coordinates of Tiles which are not solid and exist within
        a 1 Tile range of this Tile

        @param Tile[][]
        @return Tile[]

        {@code
            Tile[][] neighbours = myTile.getNeighbours( map );
        }
    */


    public Tile[neighbours] getNeighbours ( Tile[][] map ) {

        if ( this.neighbours != null ) {
            return this.neighbours;
        }

        boolean isFirst = ( this.X > 0 );
        boolean isLast = ( this.X < ( map[this.Y].length - 1 ) );
        boolean isTop = ( this.Y > 0 );
        boolean isBottom = ( this.Y < ( map.length - 1 ) );

        int i = 0;
        Tile[] neighbours = new Tile[8];

        if ( ! isFirst ) {
            if ( ! map[this.Y][this.X-1].solid ) { // left
                neighbours[i++] = new Tile( this.X-1, this.Y );
            }
            if ( ! isTop && ! map[this.Y-1][this.X-1].solid ) { // top left
                neighbours[i++] = new Tile( this.X-1, this.Y-1 );
            }
            if ( ! isBottom && ! map[this.Y+1][this.X-1].solid ) { // bottom left
                neighbours[i++] = new Tile( this.X-1, this.Y+1 );
            }
        }

        if ( ! isLast ) {
            if ( ! map[this.Y][this.X+1].solid ) { // right
                neighbours[i++] = new Tile( this.X+1, this.Y );
            }
            if ( ! isTop && ! map[this.Y-1][this.X+1].solid ) { // top right
                neighbours[i++] = new Tile( this.X+1, this.Y-1 );
            }
            if ( ! isBottom && ! map[this.Y+1][this.X+1].solid ) { // bottom right
                neighbours[i++] = new Tile( this.X+1, this.Y+1 );
            }
        }

        if ( ! isTop && ! map[this.Y-1][this.X].solid ) { // top
            neighbours[i++] = new Tile( this.X, this.Y-1 );
        }

        if ( ! isBottom && ! map[this.Y+1][this.X].solid ) {
            neighbours[i++] = new Tile( this.X, this.Y+1 );
        }

        this.neighbours = Arrays.copyOfRange( neighbours, 0, i-1 );

        return this.neighbours;
    }
}
