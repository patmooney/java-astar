
# o x o o o o 
# o x o x x o 
# o x o o x o 
# o x x o x o
# o o o o x o
# x x x x x o
# o o o o o o

use strict; use warnings;
use Data::Dumper;
use Time::HiRes;

$Data::Dumper::Terse = 1;
my $t0 = [Time::HiRes::gettimeofday];

my $map = [
        [
            { x => 0, y => 0 },
            { x => 1, y => 0, solid => 1 },
            { x => 2, y => 0 },
			{ x => 3, y => 0 },
            { x => 4, y => 0 },
            { x => 5, y => 0 },
        ],
		[
            { x => 0, y => 1 },
            { x => 1, y => 1, solid => 1 },
            { x => 2, y => 1 },
			{ x => 3, y => 1, solid => 1 },
            { x => 4, y => 1, solid => 1 },
            { x => 5, y => 1 },
        ],
		[
            { x => 0, y => 2 },
            { x => 1, y => 2, solid => 1 },
            { x => 2, y => 2 },
			{ x => 3, y => 2 },
            { x => 4, y => 2, solid => 1 },
            { x => 5, y => 2 },
        ],
        [
            { x => 0, y => 3 },
            { x => 1, y => 3, solid => 1 },
            { x => 2, y => 3, solid => 1 },
			{ x => 3, y => 3 },
            { x => 4, y => 3, solid => 1 },
            { x => 5, y => 3 }
        ],
        [
            { x => 0, y => 4 },
            { x => 1, y => 4 },
            { x => 2, y => 4 },
			{ x => 3, y => 4 },
            { x => 4, y => 4, solid => 1 },
            { x => 5, y => 4 }
        ],
        [
            { x => 0, y => 5, solid => 1 },
            { x => 1, y => 5, solid => 1 },
            { x => 2, y => 5, solid => 1 },
			{ x => 3, y => 5, solid => 1 },
            { x => 4, y => 5, solid => 1 },
            { x => 5, y => 5 }
        ],
        [
            { x => 0, y => 6 },
            { x => 1, y => 6 },
            { x => 2, y => 6 },
			{ x => 3, y => 6 },
            { x => 4, y => 6 },
            { x => 5, y => 6 }
        ]
    ];

foreach my $y ( @$map ) {
    foreach my $x ( @$y ) {
        printf( "%s ", $x->{solid} ? 'x' : 'o' );
    }
    print("\n");
}

my ( $path ) = getPath(
    $map,
    {
        x => 0, y => 0
    },
    {
        x => 0, y => 6
    }
);

printf ("%s\n", Time::HiRes::tv_interval($t0) );

foreach my $p ( @$path ) {
    $map->[$p->{y}]->[$p->{x}]->{tread} = 1;
}

foreach my $y ( @$map ) {
    foreach my $x ( @$y ) {
        printf( "%s ", $x->{tread} ? 'P' : $x->{solid} ? 'x' : 'o' );
    }
    print("\n");
}

sub getPath {
	
	my ( $map_ref, $from_ref, $to_ref ) = @_;
	
	my $closed_ref = [];
	my $open_ref = [ $from_ref ];
	my $path = [];
	my $gScore = [];
	my $fScore = [];
	
	$gScore->[$from_ref->{y}]->[$from_ref->{x}] = 0;
	$fScore->[$from_ref->{y}]->[$from_ref->{x}] = getDist($from_ref, $to_ref);
	
	while ( scalar( @$open_ref ) ){
		my $current = lowestF( $open_ref, $fScore );

		if ( $current->{x} == $to_ref->{x} && $current->{y} == $to_ref->{y} ){
			return reconstructPath( $path, $from_ref, $to_ref );
		}

		$open_ref = removeFromOpen( $open_ref, $current );
		$closed_ref->[$current->{y}]->[$current->{x}] = 1;
		#printf( "current: %s x %s\n", $current->{x}, $current->{y} );
		my @neighbours = getNeighbours( $current, $map_ref );
		
		NEIGHBOUR: foreach my $neighbour ( @neighbours ){
			#printf( "N: %s x %s\n", $neighbour->{x}, $neighbour->{y} );
			if ( $closed_ref->[$neighbour->{y}]->[$neighbour->{x}] || $neighbour->{solid} ){
				next NEIGHBOUR;
			}
			
			#printf( "GOOD: %s x %s\n", $neighbour->{x}, $neighbour->{y} );
			
			my $g = $gScore->[$current->{y}]->[$current->{x}] + getG( $current, $neighbour );
			
			unless ( grep { $_->{x} == $neighbour->{x} && $_->{y} == $neighbour->{y} } @$open_ref ){
				push @$open_ref, $neighbour;
			}
			elsif ( $g >= $gScore->[$neighbour->{y}]->[$neighbour->{x}] ){
				next NEIGHBOUR;
			}

			$path->[$neighbour->{y}]->[$neighbour->{x}] = $current;
			$gScore->[$neighbour->{y}]->[$neighbour->{x}] = $g;
			$fScore->[$neighbour->{y}]->[$neighbour->{x}] = $g +  getDist( $neighbour, $to_ref );
		}
	}
}

sub reconstructPath {
    my ( $path, $from, $to ) = @_;
    
    my @return = ();
    push ( @return, $to );
    
    my $c = $to;
    while ( 1 ) {
        if ( $c->{x} == $from->{x} && $c->{y} == $from->{y} ){
            return \@return;
        }
        $c = $path->[$c->{y}]->[$c->{x}];
        unshift( @return, $c );
    }
}

sub getG {
	my ( $from, $to ) = @_;
	if ( $from->{x} != $to->{x} && $from->{y} != $to->{y} ){
		return 14; # diagonal
	}
	return 10;
}

sub lowestF {
	my ( $open_ref, $fScore ) = @_;
	my ( $lowestF, $lowestT );
	
	foreach my $o ( @$open_ref ) {
		my $f = $fScore->[$o->{y}]->[$o->{x}];
		if ( ! defined( $lowestT ) || $fScore->[$o->{y}]->[$o->{x}] < $lowestF ){
			$lowestT = $o;
			$lowestF = $f;
		}
	}
	
	return $lowestT;
}

sub removeFromOpen {
	my ( $open_ref, $tile ) = @_;
	return [ grep {
		! ( $_->{x} == $tile->{x} && $_->{y} == $tile->{y} )
	} @$open_ref ];
}

sub getDist {
	my ( $f, $t ) = @_;
	my $xDiff = abs($f->{x} - $t->{x});
	my $yDiff = abs($f->{y} - $t->{y});
	return ( $xDiff + $yDiff ) * 10;
}

sub getNeighbours {
	my ( $t, $map_ref ) = @_;
	
	my @neighbours = ();
	
	my $hasX = $t->{x} > 0;
	my $hasY = $t->{y} > 0;
	my $hasW = $t->{x} < ( scalar(@{$map_ref->[0]}) - 1 );
	my $hasH = $t->{y} < ( scalar(@{$map_ref}) - 1 );
	
	if ( $hasX ){ 
		push @neighbours, $map_ref->[$t->{y}]->[$t->{x}-1]; # left
		
		if ( $hasY ){
			push @neighbours, $map_ref->[$t->{y}-1]->[$t->{x}-1]; # top left
		}
		if ( $hasH ) {
			push @neighbours, $map_ref->[$t->{y}+1]->[$t->{x}-1]; # bottom left
		}
	}
	if ( $hasW ) {
		push @neighbours, $map_ref->[$t->{y}]->[$t->{x}+1]; # right
		
		if ( $hasY ){
			push @neighbours, $map_ref->[$t->{y}-1]->[$t->{x}+1]; # top right
		}
		if ( $hasH ) {
			push @neighbours, $map_ref->[$t->{y}+1]->[$t->{x}+1]; # bottom right
		}
	}
	if ( $hasH ) {
	    push @neighbours, $map_ref->[$t->{y}+1]->[$t->{x}]; # bottom
	}
	if ( $hasY ) {
	    push @neighbours, $map_ref->[$t->{y}-1]->[$t->{x}]; # top
	}
	
	return @neighbours;
}