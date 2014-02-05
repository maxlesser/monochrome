package mllf.gameengine.collision;

import mllf.gameengine.entity.Entity;
import cs195n.Vec2f;

public class CollisionInfo {
	
	public final Entity entity1, entity2;
	private Vec2f mtv;

	public CollisionInfo(Entity entity1, Entity entity2) {
		this.entity1 = entity1;
		this.entity2 = entity2;
	}
	
	public void setMtv(Vec2f mtv) {
		this.mtv = mtv;
	}
	
	public Vec2f getMtv() {
		return mtv;
	}

}
