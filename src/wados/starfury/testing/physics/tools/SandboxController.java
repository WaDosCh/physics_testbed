package wados.starfury.testing.physics.tools;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyn4j.dynamics.World;

public class SandboxController {

	private final SandboxFrame frame;
	private final World world;
	private long last;
	private volatile boolean stopped = false;

	public SandboxController(final int width, final int height, final double render_scale) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.frame = new SandboxFrame(width, height, render_scale, this::stop);
		this.world = new World();
	}

	public final void stop() {
		this.stopped = true;
	}

	public final void start() {
		this.worldInit(this.world);
		this.frame.setVisible(true);
		this.last = System.nanoTime();
		this.lastSample = this.last;
		Thread thread = new Thread() {
			public void run() {
				while (!stopped) {
					gameStep();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}

	private long lastSample;
	private int subsampleIndex = 0;

	private final NumberFormat formatter = new DecimalFormat("#0.0");

	private final void gameStep() {
		this.frame.render(this.world.getBodies());
		this.customUpdate(world);
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = System.nanoTime();
		long diff = time - this.last;
		long sampleDiff = time - this.lastSample;
		subsampleIndex = (subsampleIndex + 1) % 50;
		if (subsampleIndex == 0) {
			this.frame.setTitle("FPS: " + formatter.format(50.0 / (sampleDiff / 1.0e9)) + " Tick: "
					+ formatter.format((sampleDiff / 1.0e6) / 50) + "ms");
			this.lastSample = time;
		}
		this.last = time;
		double elapsedTime = diff / 1.0e9;
		this.world.updatev(elapsedTime);
	}

	public final SandboxFrame getFrame() {
		return this.frame;
	}

	protected void worldInit(final World world) {
		// default no need
	}

	protected void customUpdate(final World world) {
		// default implementation: no custom update required
	}

}
