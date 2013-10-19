package map;

import java.awt.Color;

import list.LinkedList;
import message.AddBlock;
import message.AddParticle;
import message.MovePlayer;
import player.Camera;
import player.Panel;
import resource.ColorList;
import world.Battle;
import world.MathUtil;
import world.block.Block;
import world.block.Earth;
import world.block.Laser;
import world.block.Rocket;
import world.particle.Bood;
import world.particle.Soil;
import draw.elements.EImage;
import draw.elements.Rectangle;
import draw.painter.Painter;

public class MiniPlayer {

	public static final float WIDTH = .8f, HEIGHT = 1.8f;

	// private static final float RUN_SPEED = .22f;
	// private static final float FRICTIONX = .9f;
	// private static final float FRICTION = .98f;
	private static final float GRAVITY = .02f;
	public static final byte DAMAGE_DURATION = 100;
	private static final short MAX_LIFE = 100;
	private static final float X_ACCEL_AIR = .007f;
	private static final float X_ACCEL_GROUND = .04f;
	private static final float JUMP_X_BOOST = 1.08f;
	private static final float JUMP_POWER = -.45f;
	private static final float SLIDE_FRICTION = .6f;

	private static final byte MAX_WALK_ANIM = 20;
	private byte curWalkAnim;
	private byte curWalkState;

	public float x, y; // left top corner
	private float vx, vy;
	private boolean knock;
	private float knockx, knocky;
	byte dir;
	public static final byte LEFT_DIR = 0, RIGHT_DIR = 1;
	byte state;
	private byte color;
	short life;

	byte ncommand, command;
	float commandx, commandy;

	private boolean hangingLeft, hangingRight;
	private boolean slam;
	private byte slamPower;
	private boolean jumping;
	private boolean doubleJump;
	private boolean onGround;

	private float spawnx, spawny;

	public static final byte LAZOR = 0, ROCKET = 1;
	public static final short LASER_RELOAD_TIME = 10;
	public static final short ROCKET_RELOAD_TIME = 100;
	private short[] curReload;

	// public byte damageDuration;
	private boolean damaged;
	private boolean changed;

	public MiniPlayer(float x, float y, byte color) {
		spawnx = x;
		spawny = y;
		this.x = x;
		this.y = y;
		state = ColorList.LOOKING;
		curWalkAnim = MAX_WALK_ANIM;
		this.color = color;
		this.life = MAX_LIFE;
		ncommand = -1;
		command = -1;
		curReload = new short[Panel.LENGTH];
	}

	public void paint(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x + WIDTH / 2, y + HEIGHT / 2);
		float[] wh = camera.magnitudeToFrame(WIDTH, HEIGHT);

		painter.addMidground(new Rectangle(xy[0] - wh[1] / 2, xy[1] - wh[1],
				wh[1], wh[1] / 8, (byte) 1, Color.WHITE, false));
		painter.addMidground(new Rectangle(xy[0] - wh[1] / 2, xy[1] - wh[1],
				wh[1] * life / MAX_LIFE, wh[1] / 8, (byte) 1, Color.GREEN, true));
		painter.addMidground(new EImage(ColorList.PLAYER_IMAGE[state][dir],
				xy[0] - wh[1] / 2, xy[1] - wh[1] / 2, wh[1], wh[1]));

		// painter.addMidground(new Rectangle(xy[0], xy[1], wh[0], wh[1],
		// (byte) 1, Battle.getColor(color), true));
		// painter.addMidground(new Rectangle(xy[0], xy[1], wh[0], wh[1],
		// (byte) 1, ColorList.RED, true));
		// painter.addMidground(new Rectangle(xy[0], xy[1], wh[0], wh[1],
		// (byte) 3, ColorList.OUTLINE, false));
	}

	public void update(Battle battle, Map map, LinkedList changes) {
		if (knock) {
			vx = knockx;
			vy = knocky;
			knockx *= .9f;
			knocky = (knocky + GRAVITY) * .9f;
			if (knockx * knockx < .001f) {
				knock = false;
			}
		}

		if (vx < 0)
			dir = ColorList.LEFT_DIR;
		if (vx > 0)
			dir = ColorList.RIGHT_DIR;

		if (vx < 0.1f && vx > -.1f)
			state = ColorList.LOOKING;
		else {
			curWalkAnim--;
			if (curWalkAnim == 0) {
				curWalkAnim = MAX_WALK_ANIM;
				curWalkState++;
				if (curWalkState == 4)
					curWalkState = 0;
			}
			if (curWalkState == 1)
				state = ColorList.WALKING_POST;
			else if (curWalkState == 3)
				state = ColorList.WALKING_PRE;
			else
				state = ColorList.LOOKING;
		}

		hangingRight = false;
		hangingLeft = false;

		// horizontal movement
		float newx = x + vx;
		float tx = map.intersectBubbleRight(battle, changes, color, newx, y, y
				+ HEIGHT);
		if (tx != -1) {
			newx = tx;
			vx = 0;
			if (vy > 0) {
				vy *= SLIDE_FRICTION;
				hangingLeft = true;
			}
		}
		tx = map.intersectBubbleLeft(battle, changes, color, newx + WIDTH, y, y
				+ HEIGHT);
		if (tx != -1) {
			newx = tx - WIDTH;
			vx = 0;
			if (vy > 0) {
				vy *= SLIDE_FRICTION;
				hangingRight = true;
			}
		}

		// vertical movement
		float newy = y + vy;
		onGround = map.intersectPoint(battle, changes, color, newx, newx
				+ WIDTH, newy + HEIGHT);
		if (!onGround) {
			if (jumping && vy > 0) { // gliding
				vy += GRAVITY / 8;
				state = ColorList.JUMPING;
			} else {
				vy += GRAVITY;
			}
			vx *= .99f;
		} else {
			hangingLeft = false;
			hangingRight = false;
			vx *= .9f;
			jumping = false;
			if (slam && slamPower > 10)
				map.slam((x + WIDTH / 2), (y + HEIGHT),
						(byte) (slamPower - 10), changes, color);
			slam = false;
			slamPower = 0;
		}

		if (hangingLeft || hangingRight) {
			state = ColorList.HANGING;
		}

		if (!jumping && vy < 0)
			vy = vy * .75f;

		vy *= .99f;

		float ty = map.intersectBubbleUp(battle, changes, color, newx, newx
				+ WIDTH, newy + HEIGHT);
		if (ty != -1) {
			newy = ty - HEIGHT;
			// vy *= -.5f; // bounce
			if (vy > -.015f && vy < .015f)
				vy = 0;
		}

		ty = map.intersectBubbleDown(battle, changes, color, newx,
				newx + WIDTH, newy);
		if (ty != -1) {
			newy = ty;
			vy = 0;
		}

		if (slam) {
			if (slamPower < 100)
				slamPower++;
			state = ColorList.SLAM;
			if (vy < .5f)
				vy = .5f;
		}

		if (life < 10 && Math.random() < .05f)
			changes.add(new AddParticle(Bood.CODE, x, y, (byte) 1, (byte) 0));

		if (life <= 0) {
			changes.add(battle.addScore(Battle.getOtherColor(color)));
			changes.add(new AddParticle(Bood.CODE, x, y, (byte) 100,
					Bood.SUPER_BLOODY));
			newx = spawnx;
			newy = spawny;
			life = MAX_LIFE;
			changed = true;
		}

		// send changes

		if (newx != x || newy != y) {
			changes.add(new AddParticle(Soil.CODE, (x + WIDTH / 2), y,
					(byte) 1, (byte) 0));
			x = newx;
			y = newy;
			changed = true;
		}

		switch (command) {
			case LAZOR:
				if (curReload[LAZOR] > 0) {
					curReload[LAZOR]--;
					changed = true;
				} else if (ncommand == -3) {
					shootLaser(battle, changes);
					curReload[LAZOR] = LASER_RELOAD_TIME;
				}
				break;
			case ROCKET:
				if (curReload[ROCKET] > ROCKET) {
					curReload[ROCKET]--;
					changed = true;
				} else if (ncommand == -3) {
					shootRocket(battle, changes);
					curReload[ROCKET] = ROCKET_RELOAD_TIME;
				}
				break;

		}

		if (changed) {
			changed = false;
			MovePlayer mp = new MovePlayer(color, x, y, state, dir, life,
					command, command >= 0 ? curReload[command] : 0, damaged);
			changes.add(mp);
		}

		if (ncommand >= 0)
			command = ncommand;

	}

	public void moveLeft() {
		if (knock)
			return;
		hangingRight = false;
		if (onGround)
			vx -= X_ACCEL_GROUND;
		// else if (jumping)
		// vx -= X_ACCEL_GROUND * 1.1f;
		else
			vx -= X_ACCEL_AIR;
	}

	public void moveRight() {
		if (knock)
			return;
		hangingLeft = false;
		if (onGround)
			vx += X_ACCEL_GROUND;
		// else if (jumping)
		// vx += X_ACCEL_GROUND * 1.1f;
		else
			vx += X_ACCEL_AIR;
	}

	public void jump() {
		if (knock || slam)
			return;
		if (onGround || (!jumping && doubleJump)) {
			doubleJump = onGround;
			jumping = true;
			vy = JUMP_POWER;
			vx *= JUMP_X_BOOST;
			hangingLeft = false;
			hangingRight = false;
		} else if (!jumping && hangingLeft) {
			jumping = true;
			vy = JUMP_POWER;
			vx = 8 * X_ACCEL_GROUND;
			hangingLeft = false;
			hangingRight = false;
		} else if (!jumping && hangingRight) {
			jumping = true;
			vy = JUMP_POWER;
			vx -= 8 * X_ACCEL_GROUND;
			hangingLeft = false;
			hangingRight = false;
		}
	}

	public void stopJump() {
		jumping = false;
	}

	public void down() {
		if (!onGround)
			slam = true;
	}

	public void damage(LinkedList changes, short amount) {
		if (amount > 100)
			amount = 100;
		changes.add(new AddParticle(Bood.CODE, x, y, (byte) amount, (byte) 0));
		life -= amount;
		damaged = true;
		changed = true;
	}

	public void doCommand(byte command, float commandx, float commandy) {
		this.commandx = commandx;
		this.commandy = commandy;
		if (command > 0) {
			this.command = command;
			ncommand = -1;
		} else
			ncommand = command;
	}

	private void createBlock(Battle battle, LinkedList changes) {
		float xy[] = MathUtil.inBoundsFloat(commandx - .5f, commandy - .5f);
		Block b = Block.createBlock(Earth.CODE, xy[0], xy[1]);
		battle.addStaticBlock(b);
		changes.add(new AddBlock(b.id, Earth.CODE, b.x, b.y));
	}

	private void shootLaser(Battle battle, LinkedList changes) {
		Laser laser = (Laser) Block.createBlock(Laser.CODE, x, y);
		laser.setVelocityColor(commandx, commandy, color);
		battle.addDynamicBlock(laser);
		changes.add(new AddBlock(laser.id, Laser.CODE, laser.x, laser.y));
	}

	private void shootRocket(Battle battle, LinkedList changes) {
		Rocket rocket = (Rocket) Block.createBlock(Rocket.CODE, x, y);
		rocket.setVelocityColor(commandx, commandy, color);
		battle.addDynamicBlock(rocket);
		changes.add(new AddBlock(rocket.id, Rocket.CODE, rocket.x, rocket.y));
	}

	public void knockback(float x, float y) {
		// float scale = 2;
		// // knockx = x * scale;
		// knocky = y * scale;
		// if (knockx < -.8f)
		// knockx = -.8f;
		// if (knocky < -.8f)
		// knocky = -.8f;
		// if (knockx > .8f)
		// knockx = .8f;
		// if (knocky > .8f)
		// knocky = .8f;
		// knock = true;
	}

}

// optimize order of bubble calls (bubble for on groudn and bubble later thats
// simialr)