package solutions.gutta.weatheradvisory.api.taskrouter;

public abstract class Instruction {
	
	private String instruction;
	
	public Instruction(String instruction) {
		this.instruction = instruction;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
}
