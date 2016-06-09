package wados.starfury.testing.physics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.ContinuousDetectionMode;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Slice;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

import wados.starfury.testing.physics.tools.ColoredBody;
import wados.starfury.testing.physics.tools.SandboxController;


/**
 * Demo
 * 
 * Hold A,S,D,F to control direction of gravity
 */
public class Demo {

	static boolean left, right, up, down, gravUpdate;

	public static void main(String[] args) {
		
		System.out.println("DEMO");
		System.out.println("==============================");
		System.out.println("Hold [ASDF] to control gravity");

		SandboxController b = new SandboxController(800, 600, 45) {
			@Override
			protected void worldInit(World world) {
				world.getSettings().setContinuousDetectionMode(ContinuousDetectionMode.BULLETS_ONLY);
				Rectangle floorRect = new Rectangle(15.0, 1.0);
				ColoredBody floor = new ColoredBody();
				floor.addFixture(new BodyFixture(floorRect));
				floor.setMass(MassType.INFINITE);
				// move the floor down a bit
				floor.translate(0.0, -6.5);
				world.addBody(floor);

				ColoredBody roof = new ColoredBody();
				roof.addFixture(new BodyFixture(floorRect));
				roof.setMass(MassType.INFINITE);
				roof.translate(0.0, 6.5);
				world.addBody(roof);

				Rectangle sideRect = new Rectangle(1.0, 15.0);
				ColoredBody left = new ColoredBody();
				left.addFixture(new BodyFixture(sideRect));
				left.setMass(MassType.INFINITE);
				left.translate(-8.0, 0.0);
				world.addBody(left);

				ColoredBody right = new ColoredBody();
				right.addFixture(new BodyFixture(sideRect));
				right.setMass(MassType.INFINITE);
				right.translate(8.0, 0.0);
				world.addBody(right);

				// create a triangle object
				Triangle triShape = new Triangle(new Vector2(0.0, 0.5), new Vector2(-0.5, -0.5),
						new Vector2(0.5, -0.5));
				ColoredBody triangle = new ColoredBody();
				triangle.addFixture(triShape);
				triangle.setMass(MassType.NORMAL);
				triangle.translate(-1.0, 2.0);
				// test having a velocity
				triangle.getLinearVelocity().set(5.0, 0.0);
				world.addBody(triangle);

				// create a circle
				Circle cirShape = new Circle(0.5);
				ColoredBody circle = new ColoredBody();
				circle.addFixture(cirShape);
				circle.setMass(MassType.NORMAL);
				circle.translate(2.0, 2.0);
				// test adding some force
				circle.applyForce(new Vector2(-100.0, 0.0));
				// set some linear damping to simulate rolling friction
				circle.setLinearDamping(0.05);
				world.addBody(circle);

				// try a rectangle
				Rectangle rectShape = new Rectangle(1.0, 1.0);
				ColoredBody rectangle = new ColoredBody();
				rectangle.addFixture(rectShape);
				rectangle.setMass(MassType.NORMAL);
				rectangle.translate(0.0, 2.0);
				rectangle.getLinearVelocity().set(-5.0, 0.0);
				world.addBody(rectangle);

				// try a polygon with lots of vertices
				Polygon polyShape = Geometry.createUnitCirclePolygon(10, 1.0);
				ColoredBody polygon = new ColoredBody();
				polygon.addFixture(polyShape);
				polygon.setMass(MassType.NORMAL);
				polygon.translate(-2.5, 2.0);
				// set the angular velocity
				polygon.setAngularVelocity(Math.toRadians(-20.0));
				world.addBody(polygon);

				// try a compound object
				Circle c1 = new Circle(0.5);
				BodyFixture c1Fixture = new BodyFixture(c1);
				c1Fixture.setDensity(0.5);
				Circle c2 = new Circle(0.5);
				BodyFixture c2Fixture = new BodyFixture(c2);
				c2Fixture.setDensity(0.5);
				Rectangle rm = new Rectangle(2.0, 1.0);
				// translate the circles in local coordinates
				c1.translate(-1.0, 0.0);
				c2.translate(1.0, 0.0);
				ColoredBody capsule = new ColoredBody();
				capsule.addFixture(c1Fixture);
				capsule.addFixture(c2Fixture);
				capsule.addFixture(rm);
				capsule.setMass(MassType.NORMAL);
				capsule.translate(0.0, 4.0);
				world.addBody(capsule);

				ColoredBody issTri = new ColoredBody();
				issTri.addFixture(Geometry.createIsoscelesTriangle(1.0, 3.0));
				issTri.setMass(MassType.NORMAL);
				issTri.translate(2.0, 3.0);
				world.addBody(issTri);

				ColoredBody equTri = new ColoredBody();
				equTri.addFixture(Geometry.createEquilateralTriangle(2.0));
				equTri.setMass(MassType.NORMAL);
				equTri.translate(3.0, 3.0);
				world.addBody(equTri);

				ColoredBody rightTri = new ColoredBody();
				rightTri.addFixture(Geometry.createRightTriangle(2.0, 1.0));
				rightTri.setMass(MassType.NORMAL);
				rightTri.translate(4.0, 3.0);
				world.addBody(rightTri);

				ColoredBody cap = new ColoredBody();
				cap.addFixture(new Capsule(1.0, 0.5));
				cap.setMass(MassType.NORMAL);
				cap.translate(-3.0, 3.0);
				world.addBody(cap);

				ColoredBody slice = new ColoredBody();
				slice.addFixture(new Slice(0.5, Math.toRadians(120)));
				slice.setMass(MassType.NORMAL);
				slice.translate(-3.0, 3.0);
				world.addBody(slice);
			}

			@Override
			protected void customUpdate(World world) {
				if (!gravUpdate)
					return;
				gravUpdate = false;

				int x = 0, y = 0;

				if (left)
					x--;
				if (right)
					x++;
				if (up)
					y++;
				if (down)
					y--;

				world.setGravity(new Vector2(x * 9.81, y * 9.81));
				world.getBodies().forEach(b -> b.setAsleep(false));
			}
		};

		b.getFrame().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					left = true;
					break;
				case KeyEvent.VK_S:
					down = true;
					break;
				case KeyEvent.VK_D:
					right = true;
					break;
				case KeyEvent.VK_W:
					up = true;
					break;
				default:
					return;
				}
				gravUpdate = true;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:
					left = false;
					break;
				case KeyEvent.VK_S:
					down = false;
					break;
				case KeyEvent.VK_D:
					right = false;
					break;
				case KeyEvent.VK_W:
					up = false;
					break;
				default:
					return;
				}
				gravUpdate = true;
			}
		});

		b.start();

	}

}
