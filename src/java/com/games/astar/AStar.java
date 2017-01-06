package com.games.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;
import com.games.astar.Tile;

class AStar {

	/* buildPath

	Given a 2d array of com.games.astar.Tile with a from and to Tile
	this will return an ArrayList<Tile> containing waypoints of the path

		ArrayList<Point> map = new ArrayList<Point>(
			Arrays.asList(
				Arrays.asList(
					new Tile( 0, 0 ),
					new Tile( 0, 1 ),
					new Tile( 0, 2 ),
					...
				),
				Arrays.asList(
					new Tile( 1, 0 ),
					new Tile( 1, 1 ),
					new Tile( 1, 2 ),
					...
				),
				...
			)
		);

		Point fromTile = new Point( 0, 0 );
		Point toTile = new Point( 1, 2 );

		ArrayList<Tile> waypoints = AStar.buildPath( map, fromTile, toTile );

		// iterate over waypoints to follow path

	*/

    public static ArrayList<Tile> buildPath( ArrayList<ArrayList<Tile>> list, Tile fromT, Tile toT ){

		int _xSize = list.get(0).size();
		int _ySize = list.size();

		ArrayList<Tile> open = new ArrayList<Tile>();

		boolean _closed[][] = new boolean[_ySize][_xSize];
		boolean _open[][] = new boolean[_ySize][_xSize];
		int _gScore[][] = new int[_ySize][_xSize];
		int _fScore[][] = new int[_ySize][_xSize];
        Tile _path[][] = new Tile[_ySize][_xSize];

		Tile currentTile = null;

        open.add( fromT );
		_open[fromT.Y][fromT.X] = true;
        _gScore[fromT.Y][fromT.X] = 0;
        _fScore[fromT.Y][fromT.X] = getDist(fromT, toT);

        while ( ! open.isEmpty() ) {

            currentTile = getLowestF( open, _fScore );

			if ( currentTile == toT ){
				return reconstructPath( _path, fromT, toT );
			}

			open.remove( currentTile );
			_open[currentTile.Y][currentTile.X] = false;
			_closed[currentTile.Y][currentTile.X] = true;

			// getNeighbours should be a lazy attribute which returns a list of tiles ( preferably
			// eligible tiles, not solid, not diagonal between two solids ) e.g.
			// o x o <-- this is inaccessible by 1x1
			// o o x
			// o o o
            for ( Point tc : currentTile.getNeighbours( list ) ){

				if ( _closed[tc.Y][tc.X] ){ continue; }
                Tile neighbour = list.get( tc.Y ).get( tc.X );
				if ( neighbour.solid ) { continue; }

				int g = _gScore[currentTile.Y][currentTile.X] + getGScore(currentTile, neighbour);

                if ( ! _open[tc.Y][tc.X] ) {
                    open.add( neighbour );
                }
                else if ( g >= _gScore[tc.Y][tc.X] ) {
                    continue;
                }

                _path[tc.Y][tc.X] = currentTile;
                _gScore[tc.Y][tc.X] = g;
                _fScore[tc.Y][tc.X] = g + getDist( neighbour, toT );
            }
        }

        return null;

    }

    private static ArrayList<Tile> reconstructPath ( Tile[][] pathMap, Tile from, Tile to ) {
        ArrayList<Tile> path = new ArrayList<Tile>();
        path.add(to);

        Tile c = to;
        while( true ){
            if ( c.X == from.X && c.Y == from.Y ) {
                return Collections.reverse( path );
            }
            c = pathMap[c.Y][c.X];
            path.add(c);
        }
    }

    private static Tile getLowestF ( ArrayList<Tile> openList, int[][] fScore ){
        int lowest_f = 0;
        Tile lowest_tile = null;

        for ( Tile t : openList ){
            int f = fScore[t.Y][t.X];
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
        if ( ptile.X != tile.X && ptile.Y != tile.Y ){
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
