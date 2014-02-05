package mllf.gameengine.entity;

import mllf.gameengine.resourcemanagement.SoundGenerator;
import cs195n.Vec2f;

public class SoundSource {

	private Vec2f position;
	private String clip;
	
	public SoundSource(Vec2f position, String clipPath) {
		this.position = position;
		this.clip = clipPath;
	}
	
	public float calculateVolume(PhysicsEntity entity) {
		return 1f - (position.dist2(entity.getCenterPosition())/100);
	}
	
	public void play() {
		SoundGenerator.playClip(clip, 1f);
	}
	
	public void play(PhysicsEntity entity) {
        float volume = calculateVolume(entity);
		SoundGenerator.playClip(clip, volume);
	}
}
