package ai;

import shared.model.Position;

/**
 * Class that represents a possible card placement, containing the position, rotation,
 * meeplePlacement, score and priority.
 */
public class PossiblePlacement {

	private int rotation;
	private Position pos;
	private int meeplePlacement;
	private int score;
	private int priority;

	public PossiblePlacement(int rotation, Position pos) {
		this.rotation = rotation;
		this.pos = pos;
		this.meeplePlacement = -1;
		this.score = 0;
		this.priority = 0;
	}

	public PossiblePlacement(int rotation, Position pos, int meeplePlacement) {
		this.rotation = rotation;
		this.pos = pos;
		this.meeplePlacement = meeplePlacement;
		this.score = 0;
		this.priority = 0;
	}

	public int getRotation() {
		return rotation;
	}

	public Position getPos() {
		return pos;
	}

	public int getMeeplePlacements() {
		return meeplePlacement;
	}
	
	public int getPriority(){
		return priority;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + meeplePlacement;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + priority;
		result = prime * result + rotation;
		result = prime * result + score;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PossiblePlacement other = (PossiblePlacement) obj;
		if (meeplePlacement != other.meeplePlacement)
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (priority != other.priority)
			return false;
		if (rotation != other.rotation)
			return false;
		if (score != other.score)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "rotation: " + String.valueOf(rotation) + " Pos: "  + pos.toString() + " Score: " + score + " meeplePlacement: " + meeplePlacement;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public void setPlacement(int placement){
		this.meeplePlacement = placement;
	}
	
	public void setPriority(int priority){
		this.priority = priority;
	}
}
