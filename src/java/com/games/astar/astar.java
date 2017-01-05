package com.games.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;

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
		
    public static ArrayList<Tile> buildPath( ArrayList<ArrayList<Tile>> list, Point fromT, Point toT ){
		
		int _xSize = list.get(0).size();
		int _ySize = list.size();
		
		ArrayList<Point> path = new ArrayList<Point>();
		ArrayList<Tile> open = new ArrayList<Tile>();
		
		boolean _closed[][] = new boolean[_xSize][_ySize];
		boolean _open[][] = new boolean[_xSize][_ySize];
		int _gScore[][] = new int[_xSize][_ySize];

		// setup tile distance scores to end tile
        for ( ArrayList<Tile> xTs : list ){
            for ( Tile t : xTs ){
				// H score - estimate cost of movement between
				// tileN and the finish tile
				_gScore[t.X][t.Y] = AStar.getDist( t, toT );
            }
        }

		Tile currentTile = null;
		
        open.add( fromT );
		_open[fromT.X][fromT.Y] = true;

        while ( ! open.isEmpty() ) {
			
            currentTile = PathFinding.getLowestF( open );
			
			if ( currentTile == toT ){
				return AStar.reconstructPath( path, fromT, toT );
			}
			
			open.remove( currentTile );
			_closed[currentTile.X][currentTile.Y] = true;
			_open[currentTile.X][currentTile.Y] = false;
			
			// getNeighbours should be a lazy attribute which returns a list of tiles ( preferably
			// eligible tiles, not solid, not diagonal between two solids ) e.g.
			// o x o <-- this is inaccessible by 1x1
			// o o x
			// o o o
            for ( Point tc : currentTile.getNeighbours( list ) ){
				
				if ( _closed[tc.Y][tc.X] ){ continue; }
                Tile neighbour = list.get( tc.X ).get( tc.Y );
				if ( neighbour.solid ) { continue; } 
				
				int g = _gScore[currentTile.Y][currentTile.X];
				
                if ( ! _open[tc.X][tc.Y] ) {
                    neighbour.G = PathFinding.getGScore( neighbour, currentTile );
                    neighbour.parent = currentTile;
                    neighbour.open = true;
                    open.add( neighbour );
                }
                else {
                    int tmpG = PathFinding.getGScore( neighbour, currentTile );
                    if ( ( neighbour.H + tmpG ) < neighbour.F() ) {
                        neighbour.G = tmpG;
                        neighbour.parent = currentTile;
                    }
                }
            }
        }

        ArrayList<Tile> path = new ArrayList<Tile>();
        path.add( toT );

        while ( currentTile != fromT ){
            if ( currentTile.parent != null ){
                currentTile = currentTile.parent;
                path.add( currentTile );
            }
        }

        Collections.reverse(path);
        if ( Global.world != null && Global.world.map != null ) {
            path = PathFinding.optomisePath( path );
        }

        // HACK HACK HACK - Stop the sprite running to the out of place first tile
        path.remove(0);

        return path;

    }

    private static ArrayList<Tile> optomisePath ( ArrayList<Tile> path ){
        Tile previousTile = null;
        Tile currentTile = null;

//        int maxSkip = 5;
//        int skipC = 0;

        ArrayList<Tile> returnList = new ArrayList<Tile>( path );

        for ( Tile t : path ){

            if ( currentTile == null ){
                currentTile = t;
                continue;
            }

            if ( previousTile == null ) {
                previousTile = t;
                continue;
            }

            if ( LineOfSight.inSight( currentTile, t, true ) ){

/*                if ( skipC == maxSkip ){
                    currentTile = previousTile;
                    previousTile = t;
                    skipC = 0;
                    continue;
                } */

                returnList.remove( previousTile );
                previousTile = t;
//                skipC++;
                continue;
            }
            else {
                currentTile = previousTile;
                previousTile = t;
            }
        }

        return returnList;
    }
    
    private static Tile getLowestF ( ArrayList<Tile> openList ){
        int lowest_f = 0;
        Tile lowest_tile = null;

        for ( Tile t : openList ){
            int f = t.F();
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
            return ptile.G + 14;
        }
        return ptile.G + 10;
    }

    private static int getDist( Tile f, Tile t ){
        int xDiff = Math.abs( f.x - t.x );
        int yDiff = Math.abs( f.y - t.y );

        return ( xDiff + yDiff ) * 10; //A*
        // return 0; // Dijkstra
    }
}