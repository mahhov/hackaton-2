package resource;

public class MusicPlayer {

	private boolean mutedMusic, mutedSound;

	public MusicPlayer(boolean mutedMusic, boolean mutedSound) {
		this.mutedMusic = mutedMusic;
		this.mutedSound = mutedSound;
	}

	/*
	 * public void playProjectileFireSound(byte projectileCode, short dx, short
	 * dy) { if (mutedSound) return;
	 * 
	 * if (projectileCode == ProjectileData.RAY.CODE) Music.RAY.play(dx, dy); if
	 * (projectileCode == ProjectileData.SMALL_SHELL.CODE)
	 * Music.SMALL_SHELL.play(dx, dy); if (projectileCode ==
	 * ProjectileData.ROCKET.CODE) Music.ROCKET.play(dx, dy); if (projectileCode
	 * == ProjectileData.RAIL.CODE) Music.RAIL.play(dx, dy); if (projectileCode
	 * == ProjectileData.BIG_SHELL.CODE) Music.BIG_SHELL.play(dx, dy);
	 * 
	 * }
	 * 
	 * public void playUnitSound(boolean create, short dx, short dy) { if
	 * (mutedSound) return;
	 * 
	 * if (create) Music.UNIT_CREATE.play(dx, dy); else
	 * Music.UNIT_DESTROY.play(dx, dy); }
	 * 
	 * public void playCommandSound(boolean select, short dx, short dy) { if
	 * (mutedSound) return;
	 * 
	 * if (select) Music.PROMPT_COMMAND.play(dx, dy); else
	 * Music.CONFIRM_COMMAND.play(dx, dy); }
	 */

	public void playBackroundMusic() {
		if (!mutedMusic)
			Music.BGMUSIC.play();
	}

}
