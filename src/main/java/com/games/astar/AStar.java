package com.games.astar;

import java.util.ArrayList;
import java.util.Collections;
import com.games.astar.Tile;

class AStar {

    /* buildPath

        Given a 2d array of com.games.astar.Tile with a from and to Tile
        this will return an ArrayList<Tile> containing waypoints of the path

        {@code
            Tile[][] map = new Tile[][] {
                { new Tile( 0, 0 ), new Tile( 1, 0 ), new Tile( 2, 0 ) },
                { new Tile( 0, 1 ), new Tile( 1, 1 ), new Tile( 2, 1 ) },
                ...
            }

            Tile fromTile = new Tile( 0, 0 );
            Tile toTile = new Tile( 1, 2 );

            Tile[] waypoints = AStar.buildPath( map, fromTile, toTile );

            // iterate over waypoints to follow path
        }

    */

    public static Tile[] buildPath( Tile[][] list, Tile fromT, Tile toT ){

        int _xSize = list[0].length;
        int _ySize = list.length;

        ArrayList<Tile> open = new ArrayList<Tile>();

        boolean _closed[][] = new boolean[_ySize][_xSize];
        boolean _open[][] = new boolean[_ySize][_xSize];
        int _gScore[][] = new int[_ySize][_xSize];
        int _fScore[][] = new int[_ySize][_xSize];
        Tile _path[][] = new Tile[_ySize][_xSize];

        Tile currentTile = null;

        open.add( fromT );
        _open[fromT.y][fromT.x] = true;
        _gScore[fromT.y][fromT.x] = 0;
        _fScore[fromT.y][fromT.x] = getDist(fromT, toT);

        while ( ! open.isEmpty() ) {

            currentTile = getLowestF( open, _fScore );

            if ( currentTile == toT ){
                return reconstructPath( _path, fromT, toT );
            }

            open.remove( currentTile );
            _open[currentTile.y][currentTile.x] = false;
            _closed[currentTile.y][currentTile.x] = true;

            // getNeighbours should be a lazy attribute which returns a list of tiles ( preferably
            // eligible tiles, not solid, not diagonal between two solids ) e.g.
            // o x o <-- this is inaccessible by 1x1
            // o o x
            // o o o
            for ( Tile tc : currentTile.getNeighbours( list ) ){

                if ( _closed[tc.y][tc.x] ){ continue; }
                Tile neighbour = list[tc.y][tc.x];
                if ( neighbour.solid ) { continue; }

                int g = _gScore[currentTile.y][currentTile.x] + getGScore(currentTile, neighbour);

                if ( ! _open[tc.y][tc.x] ) {
                    open.add( neighbour );
                }
                else if ( g >= _gScore[tc.y][tc.x] ) {
                    continue;
                }

                _path[tc.y][tc.x] = currentTile;
                _gScore[tc.y][tc.x] = g;
                _fScore[tc.y][tc.x] = g + getDist( neighbour, toT );
            }
        }

        return null;

    }

    private static Tile[] reconstructPath ( Tile[][] pathMap, Tile from, Tile to ) {
        ArrayList<Tile> path = new ArrayList<Tile>();
        path.add(to);

        Tile c = to;
        while( true ){
            if ( c.x == from.x && c.y == from.y ) {
                Collections.reverse( path );
                return path.toArray( new Tile[path.size()] );
            }
            c = pathMap[c.y][c.x];
            path.add(c);
        }
    }

    private static Tile getLowestF ( ArrayList<Tile> openList, int[][] fScore ){
        int lowest_f = 0;
        Tile lowest_tile = null;

        for ( Tile t : openList ){
            int f = fScore[t.y][t.x];
            if ( lowest_tile != null && f >= lowest_f ) continue;
            lowest_tile = t;
            lowest_f = f;
        }

        openList.remove( lowest_tile );
        return lowest_tile;
    }

    // if the tile is adjacent then the score to move there is lower
    // than diagonally
    private static int getGScore ( Tile tile, Tile ptile ) {
        if ( ptile.x != tile.x && ptile.y != tile.y ){
            return 14;
        }
        return 10;
    }

    private static int getDist( Tile f, Tile t ){
        int xDiff = Math.abs( f.x - t.x );
        int yDiff = Math.abs( f.y - t.y );

        return ( xDiff + yDiff ) * 10; //A*
    }
}
