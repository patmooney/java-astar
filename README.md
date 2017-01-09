# AStar - for pathfinding

A library for pathfinding using A Star

### Usage

    import github.patmooney.astar.AStar;
    import github.patmooney.astar.Tile;

    /*
        A Tile has three attributes, x (int) + y (int) coordinates are it's array indexes
        and solid ( boolean ) signifies whether it is walkable
    */

    // Tile array is treated as Tile[y][x]
    Tile[][] map = new Tile[][]{
        { ... }, // 2d array of tiles to represent map
    };

    Tile fromTile = map[0][0]; // must be a reference to a tile in map
    Tile toTile = map[9][9];

    Tile[] waypoints = AStar.buildPath( map, fromTile, toTile );

### Speed

With 1000 iterations over a map of 100 X 100 tiles from one corner to the other, the
pathfinding was an average of 1.42ms

### How to develop

##### build

gradle build

##### test

gradle test
