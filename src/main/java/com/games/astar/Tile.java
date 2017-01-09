package com.games.astar;

import java.awt.Point;
import java.util.ArrayList;

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

public class Tile extends Point {
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


    public Tile[] getNeighbours ( Tile[][] map ) {

        if ( this.neighbours != null ) {
            return this.neighbours;
        }

        boolean isFirst = ( this.x == 0 );
        boolean isLast = ( this.x == ( map[this.y].length - 1 ) );
        boolean isTop = ( this.y == 0 );
        boolean isBottom = ( this.y == ( map.length - 1 ) );

        ArrayList<Tile> neighbours = new ArrayList<Tile>();

        if ( ! isFirst ) {
            if ( ! map[this.y][this.x-1].solid ) { // left
                neighbours.add(map[this.y][this.x-1]);
            }
            if ( ! isTop && ! map[this.y-1][this.x-1].solid ) { // top left
                neighbours.add( map[this.y-1][this.x-1] );
            }
            if ( ! isBottom && ! map[this.y+1][this.x-1].solid ) { // bottom left
                neighbours.add( map[this.y+1][this.x-1] );
            }
        }

        if ( ! isLast ) {
            if ( ! map[this.y][this.x+1].solid ) { // right
                neighbours.add( map[this.y][this.x+1] );
            }
            if ( ! isTop && ! map[this.y-1][this.x+1].solid ) { // top right
                neighbours.add( map[this.y-1][this.x+1] );
            }
            if ( ! isBottom && ! map[this.y+1][this.x+1].solid ) { // bottom right
                neighbours.add( map[this.y+1][this.x+1] );
            }
        }

        if ( ! isTop && ! map[this.y-1][this.x].solid ) { // top
            neighbours.add( map[this.y-1][this.x] );
        }

        if ( ! isBottom && ! map[this.y+1][this.x].solid ) { // bottom
            neighbours.add( map[this.y+1][this.x] );
        }

        this.neighbours = neighbours.toArray( new Tile[neighbours.size()] );

        return this.neighbours;
    }
}
