package jboxGlue;

import jgame.JGColor;
import jgame.JGObject;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.common.Vec2;

public class PhysicalObjectCircle extends PhysicalObject
{
	private double myRadius;
	
	public PhysicalObjectCircle( String id, int collisionId, JGColor color, double radius )
	{
		this( id, collisionId, color, radius, 0 );
	}
	
	public PhysicalObjectCircle( String id, int collisionId, JGColor color, double radius, double mass )
	{
		super( id, collisionId, color );
		init( radius, mass );
	}
	
	public PhysicalObjectCircle( String id, int collisionId, String gfxname, double radius )
	{
		this( id, collisionId, gfxname, radius, 0 );
	}
	
	public PhysicalObjectCircle( String id, int collisionId, String gfxname, double radius, double mass )
	{
		super( id, collisionId, gfxname );
		init( radius, mass );
	}
	
	private void init( double radius, double mass )
	{
		// save arguments
		myRadius = radius;
		
		// make it a circle
		CircleDef shape = new CircleDef();
		shape.radius = (float)radius;
		shape.density = (float)mass;
		createBody( shape );
		setBBox( -(int)radius, -(int)radius, 2*(int)radius, 2*(int)radius );
	}
	
	@Override
	public void paintShape( )
	{
		myEngine.setColor( myColor );
		myEngine.drawOval( x, y, (float)myRadius*2, (float)myRadius*2, true, true );
	}
	
	@Override
	public void move(){
		Vec2 position = myBody.getPosition();
		System.out.println(position.x + "|" + position.y);
		x = position.x;
		y = position.y;
		
	//	Vec2 force = new Vec2(10,10);
	//	myBody.applyForce(force, myBody.getLocalCenter());
		myRotation = -myBody.getAngle();
	}
	
	@Override
	public void hit( JGObject other )
	{
		// we hit something! bounce off it!
		Vec2 velocity = myBody.getLinearVelocity();
		
		// is it a tall wall?
		final double DAMPING_FACTOR = 0.8;
		boolean isSide = other.getBBox().height > other.getBBox().width;
		if( isSide )
		{
			velocity.x *= -DAMPING_FACTOR;
		}
		else
		{
			velocity.y *= -DAMPING_FACTOR;
		}
		
		// apply the change
		myBody.setLinearVelocity( velocity );
	}
}
