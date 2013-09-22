package jboxGlue;

import java.util.HashMap;

import jgame.JGColor;
import jgame.JGObject;

import org.jbox2d.collision.PolygonDef;
import org.jbox2d.common.Vec2;

import springies.BoardSetup;
import springies.Springies;

public abstract class Wall extends PhysicalObject {
	public double myWidth;
	public double myHeight;
	private double[] myPolyx = null;
	private double[] myPolyy = null;
	private Springies mySpringies;
	private double myWallMag;
	private double myWallExp;
	private boolean myWallForceOn = true;

	public Wall(Springies s, double width, double height, double wallMag,
			double wallExp) {
		super("Wall", 2, JGColor.green);
		myWidth = width;
		myHeight = height;
		initWall();
		mySpringies = s;
		myWallMag = wallMag;
		myWallExp = wallExp;
	}

	public void initWall() {
		// make it a rect
		PolygonDef shape = new PolygonDef();
		shape.setAsBox((float) myWidth, (float) myHeight);
		createBody(shape);
		setBBox(-(int) myWidth/2, -(int) myHeight / 2, (int) myWidth,
				(int) myHeight);		
	}

	@Override
	public void paintShape() {
		if (myPolyx == null || myPolyy == null) {
			// allocate memory for the polygon
			myPolyx = new double[4];
			myPolyy = new double[4];
		}

		if (myWidth > 0 && myHeight > 0) {
			// draw a rotated polygon
			myEngine.setColor(myColor);
			double cos = Math.cos(myRotation);
			double sin = Math.sin(myRotation);
			double halfWidth = myWidth / 2;
			double halfHeight = myHeight / 2;
			myPolyx[0] = (int) (x - halfWidth * cos - halfHeight * sin);
			myPolyy[0] = (int) (y + halfWidth * sin - halfHeight * cos);
			myPolyx[1] = (int) (x + halfWidth * cos - halfHeight * sin);
			myPolyy[1] = (int) (y - halfWidth * sin - halfHeight * cos);
			myPolyx[2] = (int) (x + halfWidth * cos + halfHeight * sin);
			myPolyy[2] = (int) (y - halfWidth * sin + halfHeight * cos);
			myPolyx[3] = (int) (x - halfWidth * cos + halfHeight * sin);
			myPolyy[3] = (int) (y + halfWidth * sin + halfHeight * cos);
			myEngine.drawPolygon(myPolyx, myPolyy, null, 4, true, true);
		}
	}

	@Override
	public void move() {
		// copy the position and rotation from the JBox world to the JGame world
		Vec2 position = myBody.getPosition();
		x = position.x;
		y = position.y;

		// Wall repulsion force
		if (myWallForceOn) {
			for (HashMap<String, Mass> massMap : mySpringies.getMassMaps()) {
				for (Mass m : massMap.values()) {
					m.applyForce(new Vec2((float) (myWallMag / Math.pow(m.x,
							myWallExp)), 0));
				}
			}
		}
		for (HashMap<String, Mass> massMap : mySpringies.getMassMaps()) {
			for (Mass m : massMap.values()) {
				//ballHitsWall(m);
			}
		}

	}
	
	public void ballHitsWall(Mass m) {
		//right
		if(this.checkCollision(1, 0, 0) == 1  && myBody.getPosition().x >= myWidth-10) {
			//System.out.println("hits wall");
			m.myBody.m_linearVelocity = (new Vec2(-1f,0));
		}
		//left
		if(this.checkCollision(1, 0, 0) == 1  && (myBody.getPosition().x <= 10+BoardSetup.wall_margin)) {
			//System.out.println("hits wall");
			m.myBody.m_linearVelocity = (new Vec2(1f, 0));
		}
		//bottom
		if(this.checkCollision(1, 0, 0) == 1  && (myBody.getPosition().y >= myHeight-10)) {
			m.myBody.m_linearVelocity = (new Vec2(0, -1f));
		}
		//top
		if(this.checkCollision(1, 0, 0) == 1  && (myBody.getPosition().y <= 10+BoardSetup.wall_margin)) {
			//System.out.println("hits wall");
			m.myBody.m_linearVelocity = (new Vec2(0, 1f));
		}
	}
	public void toggleWallForce() {
		myWallForceOn = myWallForceOn ^ true;
	}

	public abstract void setThickness(int delta);

}